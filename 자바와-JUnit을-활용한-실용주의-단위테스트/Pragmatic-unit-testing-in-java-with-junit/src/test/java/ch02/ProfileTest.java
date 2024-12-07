package ch02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    @Test
    void matchAnswerFalseWhenMustMatchCriteriaNotMet() {
        Profile profile = new Profile("Bull Hockey, Inc.");
        Question question = new BooleanQuestion(1, "Got bonuses?");

        Answer profileAnswer = new Answer(question, Bool.FALSE);
        profile.add(profileAnswer);

        Criteria criteria = new Criteria();
        Answer criteriaAnswer = new Answer(question, Bool.TRUE);
        Criterion criterion = new Criterion(criteriaAnswer, Weight.MustMatch);

        criteria.add(criterion);

        boolean matches = profile.matches(criteria);

        assertFalse(matches);
    }

    @Test
    void matchAnswersTrueForAnyDontCareCriteria() {
        Profile profile = new Profile("Bull Hockey, Inc.");
        Question question = new BooleanQuestion(1, "Got bonuses?");

        Answer profileAnswer = new Answer(question, Bool.FALSE);
        profile.add(profileAnswer);
        Criteria criteria = new Criteria();
        Answer criteriaAnswer = new Answer(question, Bool.TRUE);
        Criterion criterion = new Criterion(criteriaAnswer, Weight.DontCare);

        criteria.add(criterion);

        boolean matches = profile.matches(criteria);

        assertTrue(matches);

    }

}