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
    var lexeme: String?

    @ColumnInfo(name = "translation")
    var translation: String?

    @ColumnInfo(name = "category")
    var category: String?

    @Ignore
    var isChecked = false

    @Ignore
    constructor(lexeme: String?, translation: String?) : this(
        lexeme,
        translation,
        Constants.EMPTY_STRING
    ) {
    }

    constructor(lexeme: String?, translation: String?, category: String?) {
        this.lexeme = lexeme
        this.translation = translation
        this.category = category
        isChecked = false
    }

    @Ignore
    private constructor(`in`: Parcel) {
        lexeme = `in`.readString()
        translation = `in`.readString()
        this.category = `in`.readString()
    }

    override fun compareTo(o: Word): Int {
        var result = this.category!!.compareTo(o.category!!)
        return if (result != 0) {
            result
        } else {
            result = lexeme!!.compareTo(o.lexeme!!)
            if (result != 0) {
                result
            } else translation!!.compareTo(o.translation!!)
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val word = o as Word
        if (if (lexeme != null) lexeme != word.lexeme else word.lexeme != null) return false
        return if (translation != null) translation == word.translation else word.translation == null
    }

    override fun hashCode(): Int {
        var result = if (lexeme != null) lexeme.hashCode() else 0
        result = 31 * result + if (translation != null) translation.hashCode() else 0
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