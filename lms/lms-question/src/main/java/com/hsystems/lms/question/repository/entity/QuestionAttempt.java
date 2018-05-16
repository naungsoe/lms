package com.hsystems.lms.question.repository.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 6/1/17.
 */
public abstract class QuestionAttempt<T extends Question>
		implements Serializable {

	private static final long serialVersionUID = 783495004730708912L;

	protected T question;

	public T getQuestion() {
		return question;
	}
}