package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.file.FileComponent;
import com.hsystems.lms.repository.entity.lesson.Lesson;
import com.hsystems.lms.repository.entity.lesson.LessonComponent;
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
public class HBaseComponentMapper extends HBaseMapper<Component> {

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
        Component component = getEntity(result, results, timestamp);
        String parentId = getParent(result, timestamp);

        if (StringUtils.isNotEmpty(parentId)) {
          if (childComponents.containsKey(parentId)) {
            childComponents.get(parentId).add(component);

          } else {
            List<Component> components = Arrays.asList(component);
            childComponents.put(parentId, components);
          }
        } else {
          rootComponents.add(component);
        }
      }
    });

    addChildToParent(rootComponents, childComponents);
    return rootComponents;
  }

  private Component getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    String componentType = getType(mainResult, timestamp);

    switch (componentType) {
      case "LessonComponent":
        return getLessonComponent(mainResult, results, timestamp);
      case "QuizComponent":
        return getQuizComponent(mainResult, results, timestamp);
      case "SectionComponent":
        return getSectionComponent(mainResult, timestamp);
      case "CompositeQuestionComponent":
        return getCompositeQuestionComponent(mainResult, results, timestamp);
      case "MultipleChoiceComponent":
        return getMultipleChoiceComponent(mainResult, results, timestamp);
      case "FileComponent":
        return getFileComponent(mainResult, results, timestamp);
      default:
        return new UnknownComponent();
    }
  }

  private LessonComponent getLessonComponent(
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

  private QuizComponent getQuizComponent(
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

  private SectionComponent getSectionComponent(
      Result mainResult, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    String title = getTitle(mainResult, timestamp);
    String instructions = getInstructions(mainResult, timestamp);
    int order = getOrder(mainResult, timestamp);

    return new SectionComponent(
        id,
        title,
        instructions,
        Collections.emptyList(),
        order
    );
  }

  private FileComponent getFileComponent(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    int order = getOrder(mainResult, timestamp);

    return new FileComponent(
        id,
        null,
        order
    );
  }

  private void addChildToParent(
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
  public Component getEntity(List<Result> results) {
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
