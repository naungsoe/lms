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
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.Quiz;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class IndexService extends BaseService {

  private static final String COLLECTION_LEVEL = "levels";
  private static final String COLLECTION_SUBJECT = "subjects";
  private static final String COLLECTION_USER = "users";
  private static final String COLLECTION_QUESTION = "questions";
  private static final String COLLECTION_QUIZ = "quizzes";
  private static final String COLLECTION_LESSON = "lessons";

  private static final int INDEX_LIMIT = 50;

  private final IndexRepository indexRepository;

  private final LevelRepository levelRepository;

  private final SubjectRepository subjectRepository;

  private final UserRepository userRepository;

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
      LessonRepository lessonRepository,
      QuizRepository quizRepository,
      ComponentRepository componentRepository,
      QuestionRepository questionRepository) {

    this.indexRepository = indexRepository;
    this.levelRepository = levelRepository;
    this.subjectRepository = subjectRepository;
    this.userRepository = userRepository;
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
    List<User> users;
    int numFound;

    do {
      users = userRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);
      indexRepository.save(users);

      numFound = users.size();
      lastId = users.get(numFound - 1).getId();

    } while (!isLastPage(numFound));
  }

  private boolean isLastPage(int numFound) {
    return (numFound == 0) || (numFound < INDEX_LIMIT);
  }

  private void indexAllQuestions(String schoolId)
      throws IOException {

    String lastId = schoolId;
    List<Question> questions;
    int numFound;

    do {
      questions = questionRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);
      indexRepository.save(questions);

      numFound = questions.size();
      lastId = questions.get(numFound - 1).getId();

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

    Optional<Quiz> quizOptional = quizRepository.findBy(id);

    if (quizOptional.isPresent()) {
      Quiz quiz = quizOptional.get();
      List<Component> components = componentRepository.findAllBy(
          quiz.getSchool().getId(), quiz.getId());
      quiz.addComponent(components.toArray(new Component[0]));
      indexRepository.save(quiz);
    }
  }

  private void indexQuiz(String id)
      throws IOException {

    Optional<Quiz> quizOptional = quizRepository.findBy(id);

    if (quizOptional.isPresent()) {
      Quiz quiz = quizOptional.get();
      List<Component> components = componentRepository.findAllBy(
          quiz.getSchool().getId(), quiz.getId());
      quiz.addComponent(components.toArray(new Component[0]));
      indexRepository.save(quiz);
    }
  }

  private void indexQuestion(String id)
      throws IOException {

    Optional<Question> questionOptional = questionRepository.findBy(id);

    if (questionOptional.isPresent()) {
      Question question = questionOptional.get();
      indexRepository.save(question);
    }
  }
}