package com.vladimir_x.easyenglishlearn.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Word(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "lexeme")
    val lexeme: String,
    @ColumnInfo(name = "translation")
    val translation: String,
    @ColumnInfo(name = "category")
    val category: String
) : Parcelable