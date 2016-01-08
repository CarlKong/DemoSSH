package com.augmentum.ot.dao.impl;

import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.AssessmentScoreDao;
import com.augmentum.ot.model.AssessmentScore;

@Component("assessmentScoreDao")
public class AssessmentScoreDaoImpl extends BaseDaoImpl<AssessmentScore> implements
		AssessmentScoreDao {

	@Override
	public Class<AssessmentScore> getEntityClass() {
		return AssessmentScore.class;
	}

}
