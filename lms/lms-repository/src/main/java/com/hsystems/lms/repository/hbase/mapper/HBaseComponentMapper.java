package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.course.TopicComponent;
import com.hsystems.lms.repository.entity.file.FileComponent;
import com.hsystems.lms.repository.entity.lesson.ActivityComponent;
import com.hsystems.lms.repository.entity.lesson.ContentComponent;
import com.hsystems.lms.repository.entity.lesson.Lesson;
import com.hsystems.lms.repository.entity.lesson.LessonComponent;
import com.hsystems.lms.repository.entity.quiz.Quiz;
import com.hsystems.lms.repository.entity.quiz.QuizComponent;
import com.hsystems.lms.repository.entity.quiz.SectionComponent;
import com.hsystems.lms.repository.entity.special.UnknownComponent;

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
  public List<Component> getEntities (List<Result> results) {
    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<Component> rootComponents = new ArrayList<>();
    Map<String, List<Component>> childComponents = new HashMap<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      Optional<Component> componentOptional = getEntity(result, results);

      if (componentOptional.isPresent()) {
        Component component = componentOptional.get();
        String parentId = getParent(result);

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
    });

    addChildToParent(rootComponents, childComponents);
    return rootComponents;
  }

  protected Optional<Component> getEntity(
      Result mainResult, List<Result> results) {

    String componentType = getType(mainResult);
    Component component;

    switch (componentType) {
      case "TopicComponent":
        component = getTopicComponent(mainResult);
        break;
      case "LessonComponent":
        component = getLessonComponent(mainResult);
        break;
      case "QuizComponent":
        component = getQuizComponent(mainResult);
        break;
      case "ActivityComponent":
        component = getActivityComponent(mainResult);
        break;
      case "SectionComponent":
        component = getSectionComponent(mainResult);
        break;
      case "QuestionComponent":
        component = getQuestionComponent(mainResult, results);
        break;
      case "FileComponent":
        component = getFileComponent(mainResult, results);
        break;
      case "ContentComponent":
        component = getContentComponent(mainResult, results);
        break;
      default:
        component = new UnknownComponent();
        break;
    }

    return Optional.of(component);
  }

  protected TopicComponent getTopicComponent(Result mainResult) {
    String id = Bytes.toString(mainResult.getRow());
    String title = getTitle(mainResult);
    String instructions = getInstructions(mainResult);
    int order = getOrder(mainResult);
    List<Component> components = Collections.emptyList();

    return new TopicComponent(
        id,
        title,
        instructions,
        order,
        components
    );
  }

  protected LessonComponent getLessonComponent(Result mainResult) {
    String id = Bytes.toString(mainResult.getRow());
    Lesson lesson = getLesson(mainResult);
    int order = getOrder(mainResult);
    String resourceId = getResourceId(mainResult);

    return new LessonComponent(
        id,
        lesson,
        order,
        resourceId
    );
  }

  protected Lesson getLesson(Result mainResult) {
    String title = getTitle(mainResult);
    String description = getDescription(mainResult);

    return new Lesson(
        title,
        description,
        Collections.emptyList()
    );
  }

  protected QuizComponent getQuizComponent(Result mainResult) {
    String id = Bytes.toString(mainResult.getRow());
    Quiz quiz = getQuiz(mainResult);
    int order = getOrder(mainResult);
    String resourceId = getResourceId(mainResult);

    return new QuizComponent(
        id,
        quiz,
        order,
        resourceId
    );
  }

  protected Quiz getQuiz(Result mainResult) {
    String title = getTitle(mainResult);
    String description = getDescription(mainResult);

    return new Quiz(
        title,
        description,
        Collections.emptyList()
    );
  }

  protected ActivityComponent getActivityComponent(Result mainResult) {
    String id = Bytes.toString(mainResult.getRow());
    String title = getTitle(mainResult);
    String instructions = getInstructions(mainResult);
    int order = getOrder(mainResult);
    List<Component> components = Collections.emptyList();

    return new ActivityComponent(
        id,
        title,
        instructions,
        order,
        components
    );
  }

  protected SectionComponent getSectionComponent(Result mainResult) {
    String id = Bytes.toString(mainResult.getRow());
    String title = getTitle(mainResult);
    String instructions = getInstructions(mainResult);
    int order = getOrder(mainResult);
    List<Component> components = Collections.emptyList();

    return new SectionComponent(
        id,
        title,
        instructions,
        order,
        components
    );
  }

  protected FileComponent getFileComponent(
      Result mainResult, List<Result> results) {

    String id = Bytes.toString(mainResult.getRow());
    int order = getOrder(mainResult);

    return new FileComponent(
        id,
        null,
        order
    );
  }

  protected ContentComponent getContentComponent(
      Result mainResult, List<Result> results) {

    String id = Bytes.toString(mainResult.getRow());
    String content = getContent(mainResult);
    int order = getOrder(mainResult);

    return new ContentComponent(
        id,
        content,
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

            if (rootComponent instanceof TopicComponent) {
              TopicComponent topicComponent
                  = (TopicComponent) rootComponent;
              topicComponent.addComponent(
                  components.toArray(new Component[0]));
              addChildToParent(components, childComponents);

            } else if (rootComponent instanceof ActivityComponent) {
              ActivityComponent activityComponent
                  = (ActivityComponent) rootComponent;
              activityComponent.addComponent(
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
    return getEntity(mainResult, results);
  }
}
