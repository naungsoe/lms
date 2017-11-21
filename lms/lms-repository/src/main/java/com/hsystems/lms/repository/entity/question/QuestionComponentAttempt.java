package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.GradableComponentAttempt;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by naungsoe on 6/1/17.
 */
public class QuestionComponentAttempt<T extends QuestionAttempt>
		implements GradableComponentAttempt<QuestionGradingStrategy>, Serializable {

	private static final long serialVersionUID = 5517618524514244154L;

	@IndexField
	private String id;

	@IndexField
	private T attempt;

	@IndexField
	private long score;

	@IndexField
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

	@Override
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