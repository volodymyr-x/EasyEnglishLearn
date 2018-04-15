package com.example.vladimir.easyenglishlearn;

/**
 * Created by BOBAH on 26.03.2015.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WordConstructor extends Activity implements OnClickListener {
    GridLayout gridContainer;
    Button btnAnswer, btnClear, btnNew;
    TextView tvQuestion, tvAnswer;
    ArrayList<Word>  resultArr;
    StringBuilder answerBuilder;
    int iteration = 0;
    int errorsCount = 0;
    char letters[];
    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
    LinearLayout.LayoutParams lParams;
    boolean translationDirection;
    private LayoutTransition mTransitioner;
    float fSize;
    int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_constructor);

        tvAnswer = (TextView) findViewById(R.id.tvOtvet);
        tvQuestion = (TextView) findViewById(R.id.tvVopros);
        btnAnswer = (Button) findViewById(R.id.btnOtvet2);
        btnClear = (Button) findViewById(R.id.btnClear);
        lParams = new LinearLayout.LayoutParams( wrapContent, wrapContent);
        lParams.weight = 1;
        gridContainer = (GridLayout) findViewById(R.id.gridContainer);
        answerBuilder = new StringBuilder("");
        Bundle b = getIntent().getExtras();
        resultArr = b.getParcelableArrayList("selectedItems");
        translationDirection = b.getBoolean("flag", false);
        Collections.shuffle(resultArr);
        tvQuestion.setText(translationDirection ? resultArr.get(iteration).lexeme : resultArr.get(iteration).translation);
        letters = translationDirection ? resultArr.get(iteration).translation.toCharArray(): resultArr.get(iteration).lexeme.toCharArray();
        tvAnswer.setText("");
        shuffleArray(letters);
        resetTransition();
        mTransitioner.setStagger(LayoutTransition.CHANGE_APPEARING, 30);
        mTransitioner.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30);
        setupCustomAnimations();
        mTransitioner.setDuration(500);

    }
    @Override
    protected void onResume() {
        super.onResume();
        fSize = MainActivity.fSize;
        tvAnswer.setTextSize(fSize);
        tvQuestion.setTextSize(fSize);
        btnAnswer.setTextSize(fSize);
        btnClear.setTextSize(fSize);

        color = MainActivity.color;
        tvAnswer.setTextColor(color);
        tvQuestion.setTextColor(color);
        btnAnswer.setTextColor(color);
        btnClear.setTextColor(color);

        for (char letter: letters) {
            newButtonCreate(letter);
        }
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btnClear:
                if (answerBuilder.length() != 0) {
                    int lastCharIndex = answerBuilder.length() - 1;
                    char letterFromButton = answerBuilder.charAt(lastCharIndex);
                    answerBuilder.deleteCharAt(lastCharIndex);
                    tvAnswer.setText(answerBuilder);
                    newButtonCreate(letterFromButton);
                }
                break;

            case R.id.btnOtvet2:
                Answer answerCheck = new Answer(resultArr.get(iteration), answerBuilder, translationDirection);
                if (answerCheck.isAnswerCorrect()) {
                    iteration++;
                    if (iteration < resultArr.size()) {
                        tvQuestion.setText(translationDirection ? resultArr.get(iteration).lexeme : resultArr.get(iteration).translation);
                        letters = translationDirection ?resultArr.get(iteration).translation.toCharArray(): resultArr.get(iteration).lexeme.toCharArray();
                        shuffleArray(letters);
                        for (char letter: letters) {
                            newButtonCreate(letter);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Количество ошибок: " + errorsCount, Toast.LENGTH_LONG).show();
                        this.finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "не правильно", Toast.LENGTH_SHORT).show();
                    errorsCount++;
                    gridContainer.removeAllViews();
                    for (char letter: letters) {
                        newButtonCreate(letter);
                    }
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
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            char a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }

    private void newButtonCreate(char letter) {
            btnNew = new Button(this);
            btnNew.setText(String.valueOf(letter));
            btnNew.setOnClickListener(this);
            btnNew.setTextSize(fSize);
            btnNew.setTextColor(color);
            gridContainer.addView(btnNew, lParams);
    }

    private void resetTransition() {
        mTransitioner = new LayoutTransition();
        gridContainer.setLayoutTransition(mTransitioner);
    }

    private void setupCustomAnimations() {
        // Changing while Adding
        PropertyValuesHolder pvhLeft =
                PropertyValuesHolder.ofInt("left", 0, 1);
        PropertyValuesHolder pvhTop =
                PropertyValuesHolder.ofInt("top", 0, 1);
        PropertyValuesHolder pvhRight =
                PropertyValuesHolder.ofInt("right", 0, 1);
        PropertyValuesHolder pvhBottom =
                PropertyValuesHolder.ofInt("bottom", 0, 1);
        PropertyValuesHolder pvhScaleX =
                PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f);
        PropertyValuesHolder pvhScaleY =
                PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f);
        final ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(
                this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX, pvhScaleY).
                setDuration(mTransitioner.getDuration(LayoutTransition.CHANGE_APPEARING));
        mTransitioner.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);
        changeIn.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setScaleX(1f);
                view.setScaleY(1f);
            }
        });

        // Changing while Removing
        Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
        Keyframe kf1 = Keyframe.ofFloat(.9999f, 360f);
        Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
        PropertyValuesHolder pvhRotation =
                PropertyValuesHolder.ofKeyframe("rotation", kf0, kf1, kf2);
        final ObjectAnimator changeOut = ObjectAnimator.ofPropertyValuesHolder(
                this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhRotation).
                setDuration(mTransitioner.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
        mTransitioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeOut);
        changeOut.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotation(0f);
            }
        });

        // Adding
        ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 90f, 0f).
                setDuration(mTransitioner.getDuration(LayoutTransition.APPEARING));
        mTransitioner.setAnimator(LayoutTransition.APPEARING, animIn);
        animIn.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotationY(0f);
            }
        });

        // Removing
        ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f).
                setDuration(mTransitioner.getDuration(LayoutTransition.DISAPPEARING));
        mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
        animOut.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotationX(0f);
            }
        });

    }
}

