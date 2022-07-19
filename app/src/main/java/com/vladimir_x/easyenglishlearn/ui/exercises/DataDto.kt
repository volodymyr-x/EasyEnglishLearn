package com.vladimir_x.easyenglishlearn.ui.exercises

sealed class DataDto {
    class QuizDto(val question: String, val answers: List<String>) : DataDto()

    class ConstructorDto(val question: String, val answer: String, val letters: List<Letter>) :
        DataDto()
}