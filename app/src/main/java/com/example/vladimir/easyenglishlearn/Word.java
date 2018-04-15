package com.example.vladimir.easyenglishlearn;

/**
 * Created by BOBAH on 26.03.2015.
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Word implements  Comparable<Word>, Parcelable{
    String lexeme;
    String translation;

    public Word(String lexeme, String translation) {
        super();
        this.translation = translation;
        this.lexeme = lexeme;
    }

    protected Word(Parcel in) {
        lexeme = in.readString();
        translation = in.readString();
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    public int compareTo(Word o) {
        return this.lexeme.compareTo(o.lexeme);
    }


    @Override
    public String toString() {
        return  lexeme + " (" + translation + ") ";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lexeme);
        dest.writeString(translation);
    }
}
