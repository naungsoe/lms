package com.hsystems.lms.service.model.question;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by naungsoe on 3/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CompositeQuestionResourceModel
    extends QuestionResourceModel<CompositeQuestionModel> {

}