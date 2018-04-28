package com.hsystems.lms.service.indexing;

import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.beans.ActivityComponentBean;
import com.hsystems.lms.repository.entity.beans.ComponentBean;
import com.hsystems.lms.repository.entity.beans.ContentComponentBean;
import com.hsystems.lms.repository.entity.beans.LessonComponentBean;
import com.hsystems.lms.repository.entity.beans.QuestionComponentBean;
import com.hsystems.lms.repository.entity.beans.QuizComponentBean;
import com.hsystems.lms.repository.entity.beans.SectionComponentBean;
import com.hsystems.lms.repository.entity.beans.TopicComponentBean;
import com.hsystems.lms.repository.entity.course.TopicComponent;
import com.hsystems.lms.repository.entity.lesson.ActivityComponent;
import com.hsystems.lms.repository.entity.lesson.ContentComponent;
import com.hsystems.lms.repository.entity.lesson.LessonComponent;
import com.hsystems.lms.repository.entity.question.QuestionComponent;
import com.hsystems.lms.repository.entity.quiz.QuizComponent;
import com.hsystems.lms.repository.entity.quiz.SectionComponent;
import com.hsystems.lms.service.AbstractService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 15/10/16.
 */
public abstract class IndexAbstractService extends AbstractService {

  protected static final int INDEX_LIMIT = 50;

  protected boolean isLastPage(int numFound) {
    return (numFound == 0) || (numFound < INDEX_LIMIT);
  }

  protected List<ComponentBean> getComponentBeans(
      List<Component> components, String resourceId, String parentId)
      throws IOException {

    List<ComponentBean> componentBeans = new ArrayList<>();

    for (Component component : components) {
      if (component instanceof TopicComponent) {
        TopicComponent topicComponent = (TopicComponent) component;
        String topicId = topicComponent.getId();
        TopicComponentBean topicComponentBean
            = new TopicComponentBean(topicComponent, resourceId, parentId);
        componentBeans.add(topicComponentBean);

        List<Component> childComponents
            = Collections.list(topicComponent.getComponents());
        List<ComponentBean> childComponentBeans
            = getComponentBeans(childComponents, resourceId, topicId);
        componentBeans.addAll(childComponentBeans);

      } else if (component instanceof LessonComponent) {
        LessonComponent lessonComponent = (LessonComponent) component;
        LessonComponentBean lessonComponentBean
            = new LessonComponentBean(lessonComponent, resourceId, parentId);
        componentBeans.add(lessonComponentBean);

      } else if (component instanceof QuizComponent) {
        QuizComponent quizComponent = (QuizComponent) component;
        QuizComponentBean quizComponentBean
            = new QuizComponentBean(quizComponent, resourceId, parentId);
        componentBeans.add(quizComponentBean);

      } else if (component instanceof ActivityComponent) {
        ActivityComponent activityComponent = (ActivityComponent) component;
        String activityId = activityComponent.getId();
        ActivityComponentBean activityComponentBean
            = new ActivityComponentBean(
            activityComponent, resourceId, parentId);
        componentBeans.add(activityComponentBean);

        List<Component> childComponents
            = Collections.list(activityComponent.getComponents());
        List<ComponentBean> childComponentBeans
            = getComponentBeans(childComponents, resourceId, activityId);
        componentBeans.addAll(childComponentBeans);

      } else if (component instanceof SectionComponent) {
        SectionComponent sectionComponent = (SectionComponent) component;
        String sectionId = sectionComponent.getId();
        SectionComponentBean sectionComponentBean
            = new SectionComponentBean(
            sectionComponent, resourceId, parentId);
        componentBeans.add(sectionComponentBean);

        List<Component> childComponents
            = Collections.list(sectionComponent.getComponents());
        List<ComponentBean> childComponentBeans
            = getComponentBeans(childComponents, resourceId, sectionId);
        componentBeans.addAll(childComponentBeans);

      } else if (component instanceof QuestionComponent) {
        QuestionComponent questionComponent = (QuestionComponent) component;
        QuestionComponentBean questionComponentBean
            = new QuestionComponentBean(
            questionComponent, resourceId, parentId);
        componentBeans.add(questionComponentBean);

      } else if (component instanceof ContentComponent) {
        ContentComponent contentComponent = (ContentComponent) component;
        ContentComponentBean contentComponentBean
            = new ContentComponentBean(
            contentComponent, resourceId, parentId);
        componentBeans.add(contentComponentBean);
      }
    }

    return componentBeans;
  }
}