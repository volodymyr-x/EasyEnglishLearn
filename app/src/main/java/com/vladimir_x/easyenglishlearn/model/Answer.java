package com.vladimir_x.easyenglishlearn.model;

public class Answer {
    private Word mQuestion;
    private String mAnswer;
    private boolean mTranslationDirection;

    public Answer(Word question, StringBuilder answerBuilder, boolean translationDirection) {
        mQuestion = question;
        mAnswer = answerBuilder.toString();
        mTranslationDirection = translationDirection;
    }

    public boolean isCorrect() {
        return mTranslationDirection
                ? mQuestion.getTranslation().equalsIgnoreCase(mAnswer)
                : mQuestion.getLexeme().equalsIgnoreCase(mAnswer);
    }

}
