package com.hsystems.lms.question.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.level.repository.entity.Level;
import com.hsystems.lms.level.service.mapper.LevelRefModelsMapper;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionResource;
import com.hsystems.lms.question.service.model.QuestionResourceModel;
import com.hsystems.lms.resource.Permission;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.service.mapper.AuditableModelMapper;
import com.hsystems.lms.school.service.mapper.SchoolRefModelMapper;
import com.hsystems.lms.subject.repository.entity.Subject;
import com.hsystems.lms.subject.service.mapper.SubjectRefModelsMapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public final class QuestionResourceModelMapper
    implements Mapper<Auditable<QuestionResource>, QuestionResourceModel> {

  private final QuestionModelMapperFactory questionMapperFactory;

  private final PermissionModelsMapper permissionsMapper;

  private final SchoolRefModelMapper schoolRefMapper;

  private final LevelRefModelsMapper levelRefsMapper;

  private final SubjectRefModelsMapper subjectRefsMapper;

  public QuestionResourceModelMapper() {
    this.questionMapperFactory = new QuestionModelMapperFactory();
    this.permissionsMapper = new PermissionModelsMapper();
    this.schoolRefMapper = new SchoolRefModelMapper();
    this.levelRefsMapper = new LevelRefModelsMapper();
    this.subjectRefsMapper = new SubjectRefModelsMapper();
  }

  @Override
  public QuestionResourceModel from(Auditable<QuestionResource> source) {
    QuestionResourceModel questionModel = new QuestionResourceModel();
    QuestionResource resource = source.getEntity();
    questionModel.setId(resource.getId());

    Question question = resource.getQuestion();
    QuestionModelMapper<Question> questionMapper
        = questionMapperFactory.create(question);
    questionModel.setQuestion(questionMapper.from(question));

    School school = resource.getSchool();
    questionModel.setSchool(schoolRefMapper.from(school));

    List<Level> levels = Collections.list(resource.getLevels());
    questionModel.setLevels(levelRefsMapper.from(levels));

    List<Subject> subjects = Collections.list(resource.getSubjects());
    questionModel.setLevels(subjectRefsMapper.from(subjects));

    Enumeration<String> keywords = resource.getKeywords();
    questionModel.setKeywords(Collections.list(keywords));

    List<Permission> permissions = Collections.list(resource.getPermissions());
    questionModel.setPermissions(permissionsMapper.from(permissions));

    AuditableModelMapper<QuestionResourceModel> auditableMapper
        = new AuditableModelMapper<>(questionModel);
    return auditableMapper.from(source);
  }
}