package com.example.vladimir.easyenglishlearn;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vladimir.easyenglishlearn.model.Answer;
import com.example.vladimir.easyenglishlearn.model.Word;
import com.example.vladimir.easyenglishlearn.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.example.vladimir.easyenglishlearn.fragments.ExerciseChoiceFragment.SELECTED_WORDS;
import static com.example.vladimir.easyenglishlearn.fragments.ExerciseChoiceFragment.TRANSLATION_DIRECTION;

public class WordConstructorActivity extends AppCompatActivity implements OnClickListener {

    private GridLayout gridContainer;
    private Button btnAnswer;
    private Button btnClear;
    private TextView tvQuestion, tvAnswer;
    private ArrayList<Word> resultList;
    private StringBuilder answerBuilder;
    private int iteration;
    private int errorsCount;
    private char letters[];
    private LayoutParams layoutParams;
    private boolean translationDirection;
    private float fontSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_constructor);

        fontSize = MainActivity.fontSize;

        tvAnswer = findViewById(R.id.wca_tv_answer);
        tvQuestion = findViewById(R.id.wca_tv_question);
        btnAnswer = findViewById(R.id.wca_btn_answer);
        btnClear = findViewById(R.id.btnClear);

        layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        gridContainer = findViewById(R.id.gridContainer);
        answerBuilder = new StringBuilder("");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            resultList = bundle.getParcelableArrayList(SELECTED_WORDS);
            translationDirection = bundle.getBoolean(TRANSLATION_DIRECTION, false);
            Collections.shuffle(resultList);
            tvQuestion.setText(translationDirection ? resultList.get(iteration).getLexeme()
                    : resultList.get(iteration).getTranslation());
            letters = translationDirection ? resultList.get(iteration).getTranslation().toCharArray()
                    : resultList.get(iteration).getLexeme().toCharArray();
            tvAnswer.setText("");
            shuffleArray(letters);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvAnswer.setTextSize(fontSize);
        tvQuestion.setTextSize(fontSize);
        btnAnswer.setTextSize(fontSize);
        btnClear.setTextSize(fontSize);

        createButtons();
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btnClear:
                if (answerBuilder.length() > 0) {
                    int lastCharIndex = answerBuilder.length() - 1;
                    char letterFromButton = answerBuilder.charAt(lastCharIndex);
                    answerBuilder.deleteCharAt(lastCharIndex);
                    tvAnswer.setText(answerBuilder);
                    createButton(letterFromButton);
                }
                break;

            case R.id.wca_btn_answer:
                ToastUtil toastUtil = new ToastUtil(this);
                Answer answerCheck = new Answer(resultList.get(iteration), answerBuilder, translationDirection);
                if (answerCheck.isAnswerCorrect()) {
                    iteration++;
                    if (iteration < resultList.size()) {
                        tvQuestion.setText(translationDirection ? resultList.get(iteration).getLexeme()
                                : resultList.get(iteration).getTranslation());
                        letters = translationDirection ? resultList.get(iteration).getTranslation().toCharArray()
                                : resultList.get(iteration).getLexeme().toCharArray();
                        shuffleArray(letters);
                        for (char letter: letters) {
                            createButton(letter);
                        }
                    } else {
                        toastUtil.showMessage(R.string.errors_count, errorsCount);
                        finish();
                    }
                } else {
                    toastUtil.showMessage(R.string.wrong_answer);
                    errorsCount++;
                    gridContainer.removeAllViews();
                    createButtons();
                }
                tvAnswer.setText("");
                answerBuilder.delete(0, answerBuilder.length());
                break;
        }
    }
    @Override
    public void onClick(View v) {
        answerBuilder.append(((Button) v).getText().toString());
        tvAnswer.setText(answerBuilder);
        gridContainer.removeView(v);
    }

    static void shuffleArray(char[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }

    private void createButton(char letter) {
        Button button = new Button(this);
            button.setText(String.valueOf(letter));
            button.setOnClickListener(this);
            button.setTextSize(fontSize);
            gridContainer.addView(button, layoutParams);
    }

    private void createButtons() {
        for (char letter: letters) {
            createButton(letter);
        }
    }
}

