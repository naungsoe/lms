package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.repository.ComponentRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.LessonRepository;
import com.hsystems.lms.repository.LevelRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.QuizRepository;
import com.hsystems.lms.repository.SubjectRepository;
import com.hsystems.lms.repository.UserEnrollmentRepository;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.UserEnrollment;
import com.hsystems.lms.repository.entity.question.QuestionResource;
import com.hsystems.lms.repository.entity.quiz.Quiz;
import com.hsystems.lms.repository.entity.quiz.QuizResource;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class IndexService extends AbstractService {

  private static final String COLLECTION_LEVEL = "levels";
  private static final String COLLECTION_SUBJECT = "subjects";
  private static final String COLLECTION_USER = "users";
  private static final String COLLECTION_ENROLLMENT = "enrollments";
  private static final String COLLECTION_LESSON = "lessons";
  private static final String COLLECTION_QUIZ = "quizzes";
  private static final String COLLECTION_QUESTION = "questions";

  private static final int INDEX_LIMIT = 50;

  private final IndexRepository indexRepository;

  private final LevelRepository levelRepository;

  private final SubjectRepository subjectRepository;

  private final UserRepository userRepository;

  private final UserEnrollmentRepository enrollmentRepository;

  private final LessonRepository lessonRepository;

  private final QuizRepository quizRepository;

  private final ComponentRepository componentRepository;

  private final QuestionRepository questionRepository;

  @Inject
  IndexService(
      IndexRepository indexRepository,
      LevelRepository levelRepository,
      SubjectRepository subjectRepository,
      UserRepository userRepository,
      UserEnrollmentRepository enrollmentRepository,
      LessonRepository lessonRepository,
      QuizRepository quizRepository,
      ComponentRepository componentRepository,
      QuestionRepository questionRepository) {

    this.indexRepository = indexRepository;
    this.levelRepository = levelRepository;
    this.subjectRepository = subjectRepository;
    this.userRepository = userRepository;
    this.enrollmentRepository = enrollmentRepository;
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
      case COLLECTION_USER:
        indexAllUsers(schoolId);
        break;
      case COLLECTION_ENROLLMENT:
        indexAllEnrollments(schoolId);
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

  private void indexAllUsers(String schoolId)
      throws IOException {

    String lastId = schoolId;
    int numFound;

    do {
      List<User> users = userRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);
      indexRepository.save(users);

      numFound = users.size();
      lastId = users.get(numFound - 1).getId();

    } while (!isLastPage(numFound));
  }

  private boolean isLastPage(int numFound) {
    return (numFound == 0) || (numFound < INDEX_LIMIT);
  }

  private void indexAllEnrollments(String schoolId)
      throws IOException {

    String lastId = schoolId;
    int numFound;

    do {
      List<UserEnrollment> enrollments
          = enrollmentRepository.findAllBy(
              schoolId, lastId, INDEX_LIMIT);
      indexRepository.save(enrollments);

      numFound = enrollments.size();
      lastId = enrollments.get(numFound - 1).getId();

    } while (!isLastPage(numFound));
  }

  private void indexAllQuizzes(String schoolId)
      throws IOException {

    String lastId = schoolId;
    int numFound;

    do {
      List<QuizResource> quizResources = quizRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);

      for (QuizResource quizResource : quizResources) {
        List<Component> components = componentRepository.findAllBy(
            schoolId, quizResource.getId());
        quizResource.getQuiz().addComponent(
            components.toArray(new Component[0]));
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
      case COLLECTION_USER:
        indexUser(id);
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

  private void indexUser(String id)
      throws IOException {

    Optional<User> userOptional = userRepository.findBy(id);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      indexRepository.save(user);
    }
  }

  private void indexLesson(String id)
      throws IOException {

    Optional<QuizResource> resourceOptional
        = quizRepository.findBy(id);

    if (resourceOptional.isPresent()) {
      QuizResource quizResource = resourceOptional.get();
      School school = quizResource.getSchool();
      Quiz quiz = quizResource.getQuiz();
      List<Component> components = componentRepository.findAllBy(
          school.getId(), quizResource.getId());
      quiz.addComponent(components.toArray(new Component[0]));
      indexRepository.save(quizResource);
    }
  }

  private void indexQuiz(String id)
      throws IOException {

    Optional<QuizResource> resourceOptional
        = quizRepository.findBy(id);

    if (resourceOptional.isPresent()) {
      QuizResource quizResource = resourceOptional.get();
      School school = quizResource.getSchool();
      Quiz quiz = quizResource.getQuiz();
      List<Component> components = componentRepository.findAllBy(
          school.getId(), quizResource.getId());
      quiz.addComponent(components.toArray(new Component[0]));
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