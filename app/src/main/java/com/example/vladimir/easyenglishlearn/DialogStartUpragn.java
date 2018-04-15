package com.example.vladimir.easyenglishlearn;

/**
 * Created by BOBAH on 26.03.2015.
 */

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class DialogStartUpragn extends DialogFragment implements OnClickListener, OnCheckedChangeListener{
    RadioGroup rgNP;
    RadioButton rRuEn, rEnRu;
    Button btnUpr1, btnUpr2, btnCancel1;
    TextView tvTitle;
    boolean translationDirection;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_start_upragn, container);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        rgNP = (RadioGroup) view.findViewById(R.id.rgNP);
        rgNP.setOnCheckedChangeListener(this);
        rEnRu = (RadioButton) view.findViewById(R.id.rEnRu);
        rRuEn = (RadioButton) view.findViewById(R.id.rRuEn);
        rRuEn.setChecked(true);
        btnUpr1 = (Button) view.findViewById(R.id.btnUpr1);
        btnUpr1.setOnClickListener(this);
        btnUpr2 = (Button) view.findViewById(R.id.btnUpr2);
        btnUpr2.setOnClickListener(this);
        btnCancel1 = (Button) view.findViewById(R.id.btnCancel1);
        btnCancel1.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        float fSize = MainActivity.fSize;
        rRuEn.setTextSize(fSize);
        rEnRu.setTextSize(fSize);
        btnUpr1.setTextSize(fSize);
        btnUpr2.setTextSize(fSize);
        btnCancel1.setTextSize(fSize);
        tvTitle.setTextSize(fSize);

        int color = MainActivity.color;
        btnUpr1.setTextColor(color);
        btnUpr2.setTextColor(color);
        btnCancel1.setTextColor(color);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnUpr2:
                intent = new Intent(getActivity(), WordConstructor.class);
                startActivity(intentLoad(intent));
                break;
            case R.id.btnUpr1:
                intent = new Intent(getActivity(), WordQuiz.class);
                startActivity(intentLoad(intent));
                break;
            case R.id.btnCancel1:
                dismiss();
                break;

            default:
                break;
        }
        dismiss();
    }

    private Intent intentLoad (Intent intent) {
        Bundle b = new Bundle();
        b.putParcelableArrayList("selectedItems", ((WordSelection) this.getActivity()).selectedItems);
        b.putBoolean("flag", translationDirection);
        intent.putExtras(b);
        return intent;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        translationDirection = true;
        switch (checkedId) {
            case R.id.rEnRu:
                translationDirection = true;
                break;
            case R.id.rRuEn:
                translationDirection = false;
                break;
        }

    }
}

