package com.vladimir_x.easyenglishlearn.exercises

sealed class DataDto {
    class QuizDto(val question: String, val answers: List<String>) : DataDto()

    class ConstructorDto(val question: String, val answer: String, val letters: List<Char>) : DataDto()
}