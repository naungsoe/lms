package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.ComponentType;
import com.hsystems.lms.repository.entity.FileComponent;
import com.hsystems.lms.repository.entity.Lesson;
import com.hsystems.lms.repository.entity.LessonComponent;
import com.hsystems.lms.repository.entity.LessonSectionComponent;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionComponent;
import com.hsystems.lms.repository.entity.Quiz;
import com.hsystems.lms.repository.entity.QuizComponent;
import com.hsystems.lms.repository.entity.QuizSectionComponent;
import com.hsystems.lms.repository.entity.SectionComponent;
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

    List<Component> parentComponents = new ArrayList<>();
    Map<String, List<Component>> childComponents = new HashMap<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      String id = Bytes.toString(result.getRow());
      Optional<Mutation> mutationOptional = getMutationById(mutations, id);

      if (mutationOptional.isPresent()) {
        long timestamp = mutationOptional.get().getTimestamp();
        String parentId = getParent(result, timestamp);
        Component component = getEntity(result, results, timestamp);

        if (StringUtils.isNotEmpty(parentId)) {
          if (childComponents.containsKey(parentId)) {
            childComponents.get(parentId).add(component);

          } else {
            childComponents.put(parentId, Arrays.asList(component));
          }
        } else {
          parentComponents.add(component);
        }
      }
    });

    addChildToParent(parentComponents, childComponents);
    return parentComponents;
  }

  private Component getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    int order = getOrder(mainResult, timestamp);
    ComponentType type = getType(mainResult, timestamp, ComponentType.class);
    Component component;

    switch (type) {
      case LESSON:
        Lesson lesson = getLesson(results, id, timestamp);
        component = new LessonComponent(
            id,
            order,
            lesson
        );
        break;
      case QUIZ:
        Quiz quiz = getQuiz(results, id, timestamp);
        component = new QuizComponent(
            id,
            order,
            quiz
        );
        break;
      case SECTION:
        String title = getTitle(mainResult, timestamp);
        String instructions = getInstructions(mainResult, timestamp);
        ComponentType sectionType = getSectionType(
            mainResult, timestamp, ComponentType.class);

        switch (sectionType) {
          case LESSON:
            component = new LessonSectionComponent(
                id,
                title,
                instructions,
                order,
                Collections.emptyList()
            );
            break;
          case QUIZ:
            component = new QuizSectionComponent(
                id,
                title,
                instructions,
                order,
                Collections.emptyList()
            );
            break;
          default:
            component = new UnknownComponent();
            break;
        }
        break;
      case QUESTION:
        Question question = getQuestion(results, id, timestamp);
        component = new QuestionComponent(
            id,
            order,
            question
        );
        break;
      case FILE:
        component = new FileComponent(
            id,
            order,
            null
        );
        break;
      default:
        component = new UnknownComponent();
        break;
    }

    return component;
  }

  private void addChildToParent(
      List<Component> parentComponents,
      Map<String, List<Component>> childComponents) {

    if (CollectionUtils.isEmpty(parentComponents)) {
      return;
    }

    parentComponents.forEach(parentComponent -> {
      childComponents.keySet().forEach(parentId -> {
        if (parentId.equals(parentComponent.getId())) {
          List<Component> components = childComponents.get(parentId);

          switch (parentComponent.getType()) {
            case LESSON:
              LessonComponent lessonComponent
                  = (LessonComponent) parentComponent;
              lessonComponent.getLesson().addComponent(
                  components.toArray(new Component[0]));
              addChildToParent(components, childComponents);
              break;
            case QUIZ:
              QuizComponent quizComponent
                  = (QuizComponent) parentComponent;
              quizComponent.getQuiz().addComponent(
                  components.toArray(new Component[0]));
              addChildToParent(components, childComponents);
              break;
            case SECTION:
              SectionComponent sectionComponent
                  = (SectionComponent) parentComponent;
              sectionComponent.addComponent(
                  components.toArray(new Component[0]));
              addChildToParent(components, childComponents);
              break;
            default:
              break;
          }
        }
      });
    });
  }

  @Override
  public Component getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results, 0);
  }

  @Override
  public List<Put> getPuts(Component entity, long timestamp) {
    return new ArrayList<>();
  }

  @Override
  public List<Delete> getDeletes(Component entity, long timestamp) {
    return new ArrayList<>();
  }
}
