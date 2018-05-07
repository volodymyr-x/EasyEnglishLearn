package com.example.vladimir.easyenglishlearn.model;

public class Answer {
    private Word question;
    private String answer;
    private boolean translationDirection;

    public Answer(Word question, StringBuilder answerBuilder, boolean translationDirection) {
        super();
        this.question = question;
        this.answer = answerBuilder.toString();
        this.translationDirection =translationDirection;
    }

    public boolean isAnswerCorrect(){
        return  translationDirection ? question.getTranslation().equalsIgnoreCase(answer)
                : question.getLexeme().equalsIgnoreCase(answer);
    }

}
