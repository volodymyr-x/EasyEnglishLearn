package com.example.vladimir.easyenglishlearn;

/**
 * Created by BOBAH on 26.03.2015.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class WordQuiz extends Activity implements RadioGroup.OnCheckedChangeListener, OnClickListener{
    RadioGroup rgTranslation;
    RadioButton radio0, radio1, radio2;
    TextView question;
    Button btnAnswer;
    ArrayList<Word> resultArr;
    int iteration=0;
    int errors = 0;
    final Random random = new Random();
    Word currentWord;
    boolean translationDirection;
    StringBuilder answerBuilder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_quiz);

        question = (TextView) findViewById(R.id.textView1);
        this.rgTranslation = (RadioGroup) findViewById(R.id.rgPerevod);
        this.rgTranslation.setOnCheckedChangeListener(this);
        radio0 = (RadioButton) findViewById(R.id.radio0);
        radio1 = (RadioButton) findViewById(R.id.radio1);
        radio2 = (RadioButton) findViewById(R.id.radio2);
        btnAnswer = (Button) findViewById(R.id.btnOtvet);
        btnAnswer.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        resultArr = b.getParcelableArrayList("selectedItems");
        translationDirection = b.getBoolean("flag", false);
        Collections.shuffle(resultArr);
        currentWord =resultArr.get(iteration);
        question.setText(translationDirection ? currentWord.lexeme : currentWord.translation);
        changeAnswersCatalog(currentWord);
        answerBuilder = new StringBuilder("");
    }
    @Override
    protected void onResume() {
        super.onResume();
        float fSize = MainActivity.fSize;
        question.setTextSize(fSize);
        btnAnswer.setTextSize(fSize);
        radio0.setTextSize(fSize);
        radio1.setTextSize(fSize);
        radio2.setTextSize(fSize);

        int color = MainActivity.color;
        question.setTextColor(color);
        btnAnswer.setTextColor(color);
        radio0.setTextColor(color);
        radio1.setTextColor(color);
        radio2.setTextColor(color);
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public void onClick(View v) {
        RadioButton myRb = (RadioButton) findViewById(rgTranslation.getCheckedRadioButtonId());
        answerBuilder.append(myRb.getText());
        Answer answerCheck = new Answer(currentWord, answerBuilder, translationDirection);
        if (answerCheck.isAnswerCorrect()) {
            iteration++;
            if (iteration < resultArr.size()) {
                currentWord = resultArr.get(iteration);
                question.setText(translationDirection ? currentWord.lexeme : currentWord.translation);
                changeAnswersCatalog(currentWord);
            }else {
                Toast.makeText(getApplicationContext(),
                        "Количество ошибок: " + errors, Toast.LENGTH_LONG).show();
                this.finish();
            }
        }else {
            Toast.makeText(getApplicationContext(), "не правильно", Toast.LENGTH_SHORT).show();
            errors++;
        }
        answerBuilder.delete(0, answerBuilder.length());
    }

    private void changeAnswersCatalog(Word word) {
        ArrayList<Word> temp = new ArrayList<>();
        temp.addAll(resultArr);
        Collections.shuffle(temp);
        int j = random.nextInt(3);
        for(int i = 0; i < 3; i++){
            if(i == j ){
                ((RadioButton) rgTranslation.getChildAt(i)).setText(translationDirection ?word.translation :word.lexeme);
            }else 	 {
                if(!temp.get(i).translation.equals(word.translation)){
                    ((RadioButton) rgTranslation.getChildAt(i)).setText(translationDirection ?temp.get(i).translation :temp.get(i).lexeme);
                }else{
                    ((RadioButton) rgTranslation.getChildAt(i)).setText(translationDirection ?temp.get(temp.size()-1).translation :temp.get(temp.size()-1).lexeme);
                }
            }
        }
    }
}
