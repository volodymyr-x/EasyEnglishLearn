package com.example.vladimir.easyenglishlearn;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.vladimir.easyenglishlearn.model.Answer;
import com.example.vladimir.easyenglishlearn.model.Word;
import com.example.vladimir.easyenglishlearn.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.example.vladimir.easyenglishlearn.WordSelectionActivity.*;

public class WordQuizActivity extends AppCompatActivity {

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
    private ToastUtil toastUtil;

    private OnCheckedChangeListener rgTranslationListener = (group, checkedId) -> {};

    private OnClickListener btnAnswerListener = v -> {
        RadioButton checkedRB = findViewById(rgTranslation.getCheckedRadioButtonId());
        answerBuilder.append(checkedRB.getText());
        Answer answerCheck = new Answer(currentWord, answerBuilder, translationDirection);
        if (answerCheck.isCorrect()) {
            iteration++;
            if (iteration < resultWordsList.size()) {
                currentWord = resultWordsList.get(iteration);
                tvQuestion.setText(translationDirection ? currentWord.getLexeme() : currentWord.getTranslation());
                fillRadioGroup(currentWord);
            }else {
                toastUtil.showMessage(R.string.errors_count, errorsCount);
                finish();
            }
        }else {
            toastUtil.showMessage(R.string.wrong_answer);
            errorsCount++;
        }
        answerBuilder.delete(0, answerBuilder.length());
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_quiz);

        toastUtil = new ToastUtil(this);

        tvQuestion = findViewById(R.id.wqa_tv_question);
        rgTranslation = findViewById(R.id.wqa_rg_translation);
        rgTranslation.setOnCheckedChangeListener(rgTranslationListener);
        radioBtn0 = findViewById(R.id.wqa_radio_btn_0);
        radioBtn1 = findViewById(R.id.wqa_radio_btn_1);
        radioBtn2 = findViewById(R.id.wqa_radio_btn_2);
        btnAnswer = findViewById(R.id.wqa_btn_answer);
        btnAnswer.setOnClickListener(btnAnswerListener);


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
        answerBuilder = new StringBuilder();
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

    private void fillRadioGroup(Word word) {
        ArrayList<Word> tempWordsList = new ArrayList<>(resultWordsList);
        rgTranslation.clearCheck();
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
}
