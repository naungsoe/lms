package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.ComponentRepository;
import com.hsystems.lms.repository.CourseRepository;
import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.LessonRepository;
import com.hsystems.lms.repository.LevelRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.QuizRepository;
import com.hsystems.lms.repository.SubjectRepository;
import com.hsystems.lms.repository.SubscriptionRepository;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.Subscription;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.beans.ActivityComponentBean;
import com.hsystems.lms.repository.entity.beans.ComponentBean;
import com.hsystems.lms.repository.entity.beans.ContentComponentBean;
import com.hsystems.lms.repository.entity.beans.LessonComponentBean;
import com.hsystems.lms.repository.entity.beans.QuestionComponentBean;
import com.hsystems.lms.repository.entity.beans.QuizComponentBean;
import com.hsystems.lms.repository.entity.beans.SectionComponentBean;
import com.hsystems.lms.repository.entity.beans.TopicComponentBean;
import com.hsystems.lms.repository.entity.course.CourseResource;
import com.hsystems.lms.repository.entity.course.TopicComponent;
import com.hsystems.lms.repository.entity.lesson.ActivityComponent;
import com.hsystems.lms.repository.entity.lesson.ContentComponent;
import com.hsystems.lms.repository.entity.lesson.LessonComponent;
import com.hsystems.lms.repository.entity.lesson.LessonResource;
import com.hsystems.lms.repository.entity.question.QuestionComponent;
import com.hsystems.lms.repository.entity.question.QuestionResource;
import com.hsystems.lms.repository.entity.quiz.QuizComponent;
import com.hsystems.lms.repository.entity.quiz.QuizResource;
import com.hsystems.lms.repository.entity.quiz.SectionComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class IndexService extends AbstractService {

  private static final String COLLECTION_LEVEL = "levels";
  private static final String COLLECTION_SUBJECT = "subjects";
  private static final String COLLECTION_GROUP = "groups";
  private static final String COLLECTION_USER = "users";
  private static final String COLLECTION_COURSE = "courses";
  private static final String COLLECTION_LESSON = "lessons";
  private static final String COLLECTION_QUIZ = "quizzes";
  private static final String COLLECTION_QUESTION = "questions";

  private static final int INDEX_LIMIT = 50;

  private final IndexRepository indexRepository;

  private final LevelRepository levelRepository;

  private final SubjectRepository subjectRepository;

  private final GroupRepository groupRepository;

  private final UserRepository userRepository;

  private final SubscriptionRepository subscriptionRepository;

  private final CourseRepository courseRepository;

  private final LessonRepository lessonRepository;

  private final QuizRepository quizRepository;

  private final ComponentRepository componentRepository;

  private final QuestionRepository questionRepository;

  @Inject
  IndexService(
      IndexRepository indexRepository,
      LevelRepository levelRepository,
      SubjectRepository subjectRepository,
      GroupRepository groupRepository,
      UserRepository userRepository,
      SubscriptionRepository subscriptionRepository,
      CourseRepository courseRepository,
      LessonRepository lessonRepository,
      QuizRepository quizRepository,
      ComponentRepository componentRepository,
      QuestionRepository questionRepository) {

    this.indexRepository = indexRepository;
    this.levelRepository = levelRepository;
    this.subjectRepository = subjectRepository;
    this.groupRepository = groupRepository;
    this.userRepository = userRepository;
    this.subscriptionRepository = subscriptionRepository;
    this.courseRepository = courseRepository;
    this.lessonRepository = lessonRepository;
    this.quizRepository = quizRepository;
    this.componentRepository = componentRepository;
    this.questionRepository = questionRepository;
  }

  @Log
  public void indexAll(String collection, String schoolId)
      throws IOException {

    switch (collection) {
      case COLLECTION_LEVEL:
        indexAllLevels(schoolId);
        break;
      case COLLECTION_SUBJECT:
        indexAllSubjects(schoolId);
        break;
      case COLLECTION_GROUP:
        indexAllGroups(schoolId);
        break;
      case COLLECTION_USER:
        indexAllUsers(schoolId);
        break;
      case COLLECTION_COURSE:
        indexAllCourses(schoolId);
        break;
      case COLLECTION_LESSON:
        indexAllLessons(schoolId);
        break;
      case COLLECTION_QUIZ:
        indexAllQuizzes(schoolId);
        break;
      case COLLECTION_QUESTION:
        indexAllQuestions(schoolId);
        break;
      default:
        break;
    }
  }

  private void indexAllLevels(String schoolId)
      throws IOException {

    List<Level> levels = levelRepository.findAllBy(schoolId);
    indexRepository.save(levels);
  }

  private void indexAllSubjects(String schoolId)
      throws IOException {

    List<Subject> subjects = subjectRepository.findAllBy(schoolId);
    indexRepository.save(subjects);
  }

  private void indexAllGroups(String schoolId)
      throws IOException {

    String lastId = schoolId;
    int numFound;

    do {
      List<Group> groups = groupRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);
      indexRepository.save(groups);

      numFound = groups.size();
      lastId = groups.get(numFound - 1).getId();

    } while (!isLastPage(numFound));
  }

  private boolean isLastPage(int numFound) {
    return (numFound == 0) || (numFound < INDEX_LIMIT);
  }

  private void indexAllUsers(String schoolId)
      throws IOException {

    String lastId = schoolId;
    int numFound;

    do {
      List<User> users = userRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);

      for (User user : users) {
        List<Subscription> subscriptions
            = subscriptionRepository.findAllBy(user.getId());
        indexRepository.save(subscriptions);
      }

      indexRepository.save(users);

      numFound = users.size();
      lastId = users.get(numFound - 1).getId();

    } while (!isLastPage(numFound));
  }

  private void indexAllCourses(String schoolId)
      throws IOException {

    String lastId = schoolId;
    int numFound;

    do {
      List<CourseResource> courseResources = courseRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);

      for (CourseResource courseResource : courseResources) {
        String resourceId = courseResource.getId();
        List<Component> components
            = componentRepository.findAllBy(resourceId);
        indexComponents(components, resourceId, resourceId);
      }

      indexRepository.save(courseResources);

      numFound = courseResources.size();
      lastId = courseResources.get(numFound - 1).getId();

    } while (!isLastPage(numFound));
  }

  private void indexAllLessons(String schoolId)
      throws IOException {

    String lastId = schoolId;
    int numFound;

    do {
      List<LessonResource> lessonResources = lessonRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);

      for (LessonResource lessonResource : lessonResources) {
        String resourceId = lessonResource.getId();
        List<Component> components
            = componentRepository.findAllBy(resourceId);
        indexComponents(components, resourceId, resourceId);
      }

      indexRepository.save(lessonResources);

      numFound = lessonResources.size();
      lastId = lessonResources.get(numFound - 1).getId();

    } while (!isLastPage(numFound));
  }

  private void indexComponents(
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
        indexComponents(childComponents, resourceId, topicId);

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
        indexComponents(childComponents, resourceId, activityId);

      } else if (component instanceof SectionComponent) {
        SectionComponent sectionComponent = (SectionComponent) component;
        String sectionId = sectionComponent.getId();
        SectionComponentBean sectionComponentBean
            = new SectionComponentBean(
                sectionComponent, resourceId, parentId);
        componentBeans.add(sectionComponentBean);

        List<Component> childComponents
            = Collections.list(sectionComponent.getComponents());
        indexComponents(childComponents, resourceId, sectionId);

      } else if (component instanceof QuestionComponent) {
        QuestionComponent<?> questionComponent
            = (QuestionComponent<?>) component;
        QuestionComponentBean<?> questionComponentBean
            = new QuestionComponentBean(
                questionComponent, resourceId, parentId);
        componentBeans.add(questionComponentBean);

      } else if (component instanceof ContentComponent) {
        ContentComponent contentComponent
            = (ContentComponent) component;
        ContentComponentBean contentComponentBean
            = new ContentComponentBean(
                contentComponent, resourceId, parentId);
        componentBeans.add(contentComponentBean);
      }
    }

    if (CollectionUtils.isNotEmpty(componentBeans)) {
      indexRepository.save(componentBeans);
    }
  }

  private void indexAllQuizzes(String schoolId)
      throws IOException {

    String lastId = schoolId;
    int numFound;

    do {
      List<QuizResource> quizResources = quizRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);

      for (QuizResource quizResource : quizResources) {
        String resourceId = quizResource.getId();
        List<Component> components
            = componentRepository.findAllBy(resourceId);
        indexComponents(components, resourceId, resourceId);
      }

      indexRepository.save(quizResources);

      numFound = quizResources.size();
      lastId = quizResources.get(numFound - 1).getId();

    } while (!isLastPage(numFound));
  }

  private void indexAllQuestions(String schoolId)
      throws IOException {

    String lastId = schoolId;
    List<QuestionResource> questionResources;
    int numFound;

    do {
      questionResources = questionRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);
      indexRepository.save(questionResources);

      numFound = questionResources.size();
      lastId = questionResources.get(numFound - 1).getId();

    } while (!isLastPage(numFound));
  }

  @Log
  public void index(String collection, String id)
      throws IOException {

    switch (collection) {
      case COLLECTION_LEVEL:
        indexLevel(id);
        break;
      case COLLECTION_SUBJECT:
        indexSubject(id);
        break;
      case COLLECTION_GROUP:
        indexGroup(id);
        break;
      case COLLECTION_USER:
        indexUser(id);
        break;
      case COLLECTION_COURSE:
        indexCourse(id);
        break;
      case COLLECTION_LESSON:
        indexLesson(id);
        break;
      case COLLECTION_QUIZ:
        indexQuiz(id);
        break;
      case COLLECTION_QUESTION:
        indexQuestion(id);
        break;
      default:
        break;
    }
  }

  private void indexLevel(String id)
      throws IOException {

    Optional<Level> levelOptional = levelRepository.findBy(id);

    if (levelOptional.isPresent()) {
      Level level = levelOptional.get();
      indexRepository.save(level);
    }
  }

  private void indexSubject(String id)
      throws IOException {

    Optional<Subject> subjectOptional = subjectRepository.findBy(id);

    if (subjectOptional.isPresent()) {
      Subject subject = subjectOptional.get();
      indexRepository.save(subject);
    }
  }

  private void indexGroup(String id)
      throws IOException {

    Optional<Group> groupOptional = groupRepository.findBy(id);

    if (groupOptional.isPresent()) {
      Group group = groupOptional.get();
      indexRepository.save(group);
    }
  }

  private void indexUser(String id)
      throws IOException {

    Optional<User> userOptional = userRepository.findBy(id);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      List<Subscription> subscriptions
          = subscriptionRepository.findAllBy(user.getId());
      indexRepository.save(subscriptions);
      indexRepository.save(user);
    }
  }

  private void indexCourse(String id)
      throws IOException {

    Optional<CourseResource> resourceOptional
        = courseRepository.findBy(id);

    if (resourceOptional.isPresent()) {
      CourseResource courseResource = resourceOptional.get();
      String resourceId = courseResource.getId();
      List<Component> components
          = componentRepository.findAllBy(resourceId);
      indexComponents(components, resourceId, resourceId);
      indexRepository.save(courseResource);
    }
  }

  private void indexLesson(String id)
      throws IOException {

    Optional<LessonResource> resourceOptional
        = lessonRepository.findBy(id);

    if (resourceOptional.isPresent()) {
      LessonResource lessonResource = resourceOptional.get();
      String resourceId = lessonResource.getId();
      List<Component> components
          = componentRepository.findAllBy(resourceId);
      indexComponents(components, resourceId, resourceId);
      indexRepository.save(lessonResource);
    }
  }

  private void indexQuiz(String id)
      throws IOException {

    Optional<QuizResource> resourceOptional
        = quizRepository.findBy(id);

    if (resourceOptional.isPresent()) {
      QuizResource quizResource = resourceOptional.get();
      String resourceId = quizResource.getId();
      List<Component> components
          = componentRepository.findAllBy(resourceId);
      indexComponents(components, resourceId, resourceId);
      indexRepository.save(quizResource);
    }
  }

  private void indexQuestion(String id)
      throws IOException {

    Optional<QuestionResource> resourceOptional
        = questionRepository.findBy(id);

    if (resourceOptional.isPresent()) {
      QuestionResource questionResource = resourceOptional.get();
      indexRepository.save(questionResource);
    }
  }
}