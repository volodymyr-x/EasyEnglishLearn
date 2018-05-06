package com.example.vladimir.easyenglishlearn;


import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vladimir.easyenglishlearn.model.Answer;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.example.vladimir.easyenglishlearn.fragments.ExerciseChoiceFragment.SELECTED_WORDS;
import static com.example.vladimir.easyenglishlearn.fragments.ExerciseChoiceFragment.TRANSLATION_DIRECTION;

public class WordQuizActivity extends AppCompatActivity
        implements RadioGroup.OnCheckedChangeListener, OnClickListener{

    private RadioGroup rgTranslation;
    private RadioButton radioBtn0, radioBtn1, radioBtn2;
    private TextView tvQuestion;
    private Button btnAnswer;
    private ArrayList<Word> resultWordsList;
    private int iteration;
    private int errorsCount;
    private Word currentWord;
    private boolean translationDirection;
    private StringBuilder answerBuilder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_quiz);

        tvQuestion = findViewById(R.id.wqa_tv_question);
        this.rgTranslation = findViewById(R.id.wqa_rg_translation);
        this.rgTranslation.setOnCheckedChangeListener(this);
        radioBtn0 = findViewById(R.id.wqa_radio_btn_0);
        radioBtn1 = findViewById(R.id.wqa_radio_btn_1);
        radioBtn2 = findViewById(R.id.wqa_radio_btn_2);
        btnAnswer = findViewById(R.id.wqa_btn_answer);
        btnAnswer.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            resultWordsList = bundle.getParcelableArrayList(SELECTED_WORDS);
            translationDirection = bundle.getBoolean(TRANSLATION_DIRECTION, false);
            Collections.shuffle(resultWordsList);
            currentWord = resultWordsList.get(iteration);
            tvQuestion.setText(translationDirection ? currentWord.getLexeme()
                    : currentWord.getTranslation());
            fillRadioGroup(currentWord);
        }
        answerBuilder = new StringBuilder("");
    }
    @Override
    protected void onResume() {
        super.onResume();
        float fontSize = MainActivity.fontSize;
        tvQuestion.setTextSize(fontSize);
        btnAnswer.setTextSize(fontSize);
        radioBtn0.setTextSize(fontSize);
        radioBtn1.setTextSize(fontSize);
        radioBtn2.setTextSize(fontSize);
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public void onClick(View v) {
        RadioButton checkedRB = findViewById(rgTranslation.getCheckedRadioButtonId());
        answerBuilder.append(checkedRB.getText());
        Answer answerCheck = new Answer(currentWord, answerBuilder, translationDirection);
        if (answerCheck.isAnswerCorrect()) {
            iteration++;
            if (iteration < resultWordsList.size()) {
                currentWord = resultWordsList.get(iteration);
                tvQuestion.setText(translationDirection ? currentWord.getLexeme() : currentWord.getTranslation());
                fillRadioGroup(currentWord);
            }else {
                showMessage(R.string.errors_count, errorsCount);
                finish();
            }
        }else {
            showMessage(R.string.wrong_answer);
            errorsCount++;
        }
        answerBuilder.delete(0, answerBuilder.length());
    }

    private void fillRadioGroup(Word word) {
        ArrayList<Word> tempWordsList = new ArrayList<>(resultWordsList);
        Collections.shuffle(tempWordsList);
        int j = new Random().nextInt(3);
        for(int i = 0; i < 3; i++){
            if(i == j){
                ((RadioButton) rgTranslation.getChildAt(i))
                        .setText(translationDirection ? word.getTranslation() : word.getLexeme());
            }else 	 {
                if(!tempWordsList.get(i).getTranslation().equals(word.getTranslation())){
                    ((RadioButton) rgTranslation.getChildAt(i))
                            .setText(translationDirection ? tempWordsList.get(i).getTranslation()
                                    : tempWordsList.get(i).getLexeme());
                }else{
                    ((RadioButton) rgTranslation.getChildAt(i))
                            .setText(translationDirection ? tempWordsList.get(tempWordsList.size()-1).getTranslation()
                                    : tempWordsList.get(tempWordsList.size() - 1).getLexeme());
                }
            }
        }
    }

    private void showMessage(@StringRes int id, int errorsCount) {
        String message = getString(id);
        if (errorsCount >= 0) {
            message += " " + errorsCount;
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    private void showMessage(@StringRes int id) {
        showMessage(id, -1);
    }
}
