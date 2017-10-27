package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.file.FileComponent;
import com.hsystems.lms.repository.entity.lesson.Lesson;
import com.hsystems.lms.repository.entity.lesson.LessonComponent;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionComponent;
import com.hsystems.lms.repository.entity.quiz.Quiz;
import com.hsystems.lms.repository.entity.quiz.QuizComponent;
import com.hsystems.lms.repository.entity.quiz.SectionComponent;
import com.hsystems.lms.repository.entity.special.UnknownComponent;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseComponentMapper extends HBaseAbstractMapper<Component> {

  @Override
  public List<Component> getEntities (
      List<Result> results, List<Mutation> mutations) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<Component> rootComponents = new ArrayList<>();
    Map<String, List<Component>> childComponents = new HashMap<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      String id = Bytes.toString(result.getRow());
      Optional<Mutation> mutationOptional = getMutationById(mutations, id);

      if (mutationOptional.isPresent()) {
        Mutation mutation = mutationOptional.get();
        long timestamp = mutation.getTimestamp();
        Optional<Component> componentOptional
            = getEntity(result, results, timestamp);

        if (componentOptional.isPresent()) {
          Component component = componentOptional.get();
          String parentId = getParent(result, timestamp);

          if (StringUtils.isNotEmpty(parentId)) {
            if (childComponents.containsKey(parentId)) {
              childComponents.get(parentId).add(component);

            } else {
              List<Component> components = new ArrayList<>(
                  Arrays.asList(component));
              childComponents.put(parentId, components);
            }
          } else {
            rootComponents.add(component);
          }
        }
      }
    });

    addChildToParent(rootComponents, childComponents);
    return rootComponents;
  }

  protected Optional<Component> getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    String componentType = getType(mainResult, timestamp);
    Component component;

    switch (componentType) {
      case "LessonComponent":
        component = getLessonComponent(mainResult, results, timestamp);
        break;
      case "QuizComponent":
        component = getQuizComponent(mainResult, results, timestamp);
        break;
      case "SectionComponent":
        component = getSectionComponent(mainResult, timestamp);
        break;
      case "QuestionComponent":
        component = getQuestionComponent(mainResult, results, timestamp);
        break;
      case "FileComponent":
        component = getFileComponent(mainResult, results, timestamp);
        break;
      default:
        component = new UnknownComponent();
        break;
    }

    return Optional.of(component);
  }

  protected LessonComponent getLessonComponent(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    Lesson lesson = getLesson(results, id, timestamp);
    int order = getOrder(mainResult, timestamp);

    return new LessonComponent(
        id,
        lesson,
        order
    );
  }

  protected Lesson getLesson(
      List<Result> results, String prefix, long timestamp) {

    Result result = results.stream()
        .filter(isLessonResult(prefix)).findFirst().get();
    String title = getTitle(result, timestamp);
    String instructions = getInstructions(result, timestamp);

    return new Lesson(
        title,
        instructions,
        Collections.emptyList()
    );
  }

  protected QuizComponent getQuizComponent(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    Quiz quiz = getQuiz(results, id, timestamp);
    int order = getOrder(mainResult, timestamp);

    return new QuizComponent(
        id,
        quiz,
        order
    );
  }

  protected Quiz getQuiz(
      List<Result> results, String prefix, long timestamp) {

    Result result = results.stream()
        .filter(isQuizResult(prefix)).findFirst().get();
    String title = getTitle(result, timestamp);
    String instructions = getInstructions(result, timestamp);

    return new Quiz(
        title,
        instructions,
        Collections.emptyList()
    );
  }

  protected SectionComponent getSectionComponent(
      Result mainResult, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    String title = getTitle(mainResult, timestamp);
    String instructions = getInstructions(mainResult, timestamp);
    int order = getOrder(mainResult, timestamp);
    List<Component> components = Collections.emptyList();

    return new SectionComponent(
        id,
        title,
        instructions,
        order,
        components
    );
  }

  @Override
  protected QuestionComponent getQuestionComponent(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    Question question = getQuestion(mainResult, results, timestamp);
    long score = getScore(mainResult, timestamp);
    int order = getOrder(mainResult, timestamp);

    return new QuestionComponent(
        id,
        question,
        score,
        order
    );
  }

  protected FileComponent getFileComponent(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    int order = getOrder(mainResult, timestamp);

    return new FileComponent(
        id,
        null,
        order
    );
  }

  protected void addChildToParent(
      List<Component> rootComponents,
      Map<String, List<Component>> childComponents) {

    if (CollectionUtils.isEmpty(rootComponents)) {
      return;
    }

    rootComponents.forEach(rootComponent ->
        childComponents.keySet().forEach(parentId -> {
          if (parentId.equals(rootComponent.getId())) {
            List<Component> components = childComponents.get(parentId);

            if (rootComponent instanceof LessonComponent) {
              LessonComponent lessonComponent
                  = (LessonComponent) rootComponent;
              lessonComponent.getLesson().addComponent(
                  components.toArray(new Component[0]));
              addChildToParent(components, childComponents);

            } else if (rootComponent instanceof QuizComponent) {
              QuizComponent quizComponent
                  = (QuizComponent) rootComponent;
              quizComponent.getQuiz().addComponent(
                  components.toArray(new Component[0]));
              addChildToParent(components, childComponents);

            } else if (rootComponent instanceof SectionComponent) {
              SectionComponent sectionComponent
                  = (SectionComponent) rootComponent;
              sectionComponent.addComponent(
                  components.toArray(new Component[0]));
              addChildToParent(components, childComponents);
            }
          }
        }));
  }

  @Override
  public Optional<Component> getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results, 0);
  }

  @Override
  public List<Put> getPuts(Component entity, long timestamp) {
    return Collections.emptyList();
  }

  @Override
  public List<Delete> getDeletes(Component entity, long timestamp) {
    return Collections.emptyList();
  }
}
