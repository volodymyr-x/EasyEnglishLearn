package com.example.vladimir.easyenglishlearn.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Word implements Comparable<Word>, Parcelable {

    private String mLexeme;
    private String mTranslation;
    private String mCategory;


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

    public Word(String lexeme, String translation) {
        this(lexeme, translation, "");
    }

    public Word(String lexeme, String translation, String category) {
        mLexeme = lexeme;
        mTranslation = translation;
        mCategory = category;
    }

    private Word(Parcel in) {
        mLexeme = in.readString();
        mTranslation = in.readString();
        mCategory = in.readString();
    }

    public int compareTo(@NonNull Word o) {
        int result = this.mCategory.compareTo(o.mCategory);
        if (result != 0) {
            return result;
        } else {
            result = this.mLexeme.compareTo(o.mLexeme);
            if (result != 0) {
                return result;
            } else
                return this.mTranslation.compareTo(o.mTranslation);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word = (Word) o;

        if (mLexeme != null ? !mLexeme.equals(word.mLexeme) : word.mLexeme != null) return false;
        return mTranslation != null ? mTranslation.equals(word.mTranslation) : word.mTranslation == null;
    }

    @Override
    public int hashCode() {
        int result = mLexeme != null ? mLexeme.hashCode() : 0;
        result = 31 * result + (mTranslation != null ? mTranslation.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return mLexeme + " (" + mTranslation + ") ";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLexeme);
        dest.writeString(mTranslation);
        dest.writeString(mCategory);
    }

    public String getLexeme() {
        return mLexeme;
    }

    public String getTranslation() {
        return mTranslation;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }
}
