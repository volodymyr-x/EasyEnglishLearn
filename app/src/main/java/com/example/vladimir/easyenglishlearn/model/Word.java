package com.example.vladimir.easyenglishlearn.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Word implements  Comparable<Word>, Parcelable{
    private String lexeme;
    private String translation;

    public Word(String lexeme, String translation) {
        super();
        this.translation = translation;
        this.lexeme = lexeme;
    }

    private Word(Parcel in) {
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

    public String getLexeme() {
        return lexeme;
    }

    public String getTranslation() {
        return translation;
    }
}
