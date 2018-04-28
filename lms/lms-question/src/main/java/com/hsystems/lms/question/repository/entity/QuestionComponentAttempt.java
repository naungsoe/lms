package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.entity.component.GradableComponentAttempt;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by naungsoe on 6/1/17.
 */
public final class QuestionComponentAttempt<T extends QuestionAttempt>
		implements GradableComponentAttempt<QuestionGradingStrategy>, Serializable {

	private static final long serialVersionUID = 668565871430745293L;

	private String id;

	private T attempt;

	private long score;

	private LocalDateTime attemptedDateTime;

	QuestionComponentAttempt() {

	}

	public QuestionComponentAttempt(
			String id,
			T attempt,
			long score,
			LocalDateTime attemptedDateTime) {

		this.id = id;
		this.attempt = attempt;
		this.score = score;
		this.attemptedDateTime = attemptedDateTime;
	}

	public String getId() {
		return id;
	}

	public T getAttempt() {
		return attempt;
	}

	public long getScore() {
		return score;
	}

	@Override
	public LocalDateTime getAttemptedDateTime() {
		return attemptedDateTime;
	}

	@Override
	public void gradeAttempt(QuestionGradingStrategy gradingStrategy) {
		gradingStrategy.gradeAttempt(this);
		score = gradingStrategy.calculateScore(this);
	}
}