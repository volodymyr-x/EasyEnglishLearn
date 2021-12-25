package com.vladimir_x.easyenglishlearn.model

import android.os.Parcelable
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import android.os.Parcel
import androidx.room.Entity
import androidx.room.Ignore
import com.vladimir_x.easyenglishlearn.Constants

@Entity
class Word : Comparable<Word>, Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "lexeme")
    var lexeme: String

    @ColumnInfo(name = "translation")
    var translation: String

    @ColumnInfo(name = "category")
    var category: String

    @Ignore
    var isChecked = false

    @Ignore
    constructor(lexeme: String, translation: String) : this(
        lexeme,
        translation,
        Constants.EMPTY_STRING
    )

    constructor(lexeme: String, translation: String, category: String) {
        this.lexeme = lexeme
        this.translation = translation
        this.category = category
        isChecked = false
    }

    @Ignore
    private constructor(`in`: Parcel) {
        lexeme = `in`.readString() ?: Constants.EMPTY_STRING
        translation = `in`.readString() ?: Constants.EMPTY_STRING
        this.category = `in`.readString() ?: Constants.EMPTY_STRING
    }

    override fun compareTo(other: Word): Int {
        var result = this.category.compareTo(other.category)
        return if (result != 0) {
            result
        } else {
            result = lexeme.compareTo(other.lexeme)
            if (result != 0) {
                result
            } else translation.compareTo(other.translation)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val word = other as Word
        if (lexeme != word.lexeme) return false
        return translation == word.translation
    }

    override fun hashCode(): Int {
        var result = lexeme.hashCode()
        result = 31 * result + translation.hashCode()
        return result
    }

    override fun toString(): String {
        return "$lexeme ($translation) "
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(lexeme)
        dest.writeString(translation)
        dest.writeString(this.category)
    }

    companion object CREATOR : Parcelable.Creator<Word> {
        override fun createFromParcel(parcel: Parcel): Word {
            return Word(parcel)
        }

        override fun newArray(size: Int): Array<Word?> {
            return arrayOfNulls(size)
        }
    }
}