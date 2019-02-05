package com.example.vladimir.easyenglishlearn.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import static com.example.vladimir.easyenglishlearn.Constants.EMPTY_STRING;

@Entity
public class Word implements Comparable<Word>, Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;
    @ColumnInfo(name = "lexeme")
    private String mLexeme;
    @ColumnInfo(name = "translation")
    private String mTranslation;
    @ColumnInfo(name = "category")
    private String mCategory;
    @Ignore
    private boolean isChecked;


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

    @Ignore
    public Word(String lexeme, String translation) {
        this(lexeme, translation, EMPTY_STRING);
    }

    public Word(String lexeme, String translation, String category) {
        mLexeme = lexeme;
        mTranslation = translation;
        mCategory = category;
        isChecked = false;
    }

    @Ignore
    private Word(@NonNull Parcel in) {
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

    @NonNull
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

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getLexeme() {
        return mLexeme;
    }

    public void setLexeme(String lexeme) {
        mLexeme = lexeme;
    }

    public void setTranslation(String translation) {
        mTranslation = translation;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
