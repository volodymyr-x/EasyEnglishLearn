package com.example.vladimir.easyenglishlearn;

/**
 * Created by BOBAH on 26.03.2015.
 */
public class Answer {
    Word question;
    String answer;
    boolean translationDirection;

    public Answer(Word question, StringBuilder answerBuilder, boolean translationDirection) {
        super();
        this.question = question;
        this.answer = answerBuilder.toString();
        this.translationDirection =translationDirection;
    }

    boolean isAnswerCorrect(){
        return  translationDirection ? question.translation.equalsIgnoreCase(answer): question.lexeme.equalsIgnoreCase(answer);
    }

}
