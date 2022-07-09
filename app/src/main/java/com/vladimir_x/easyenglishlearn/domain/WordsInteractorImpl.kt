package com.vladimir_x.easyenglishlearn.domain

import com.vladimir_x.easyenglishlearn.domain.repository.WordsRepository
import com.vladimir_x.easyenglishlearn.model.Word
import com.vladimir_x.easyenglishlearn.ui.model.WordUI
import kotlinx.coroutines.flow.Flow

class WordsInteractorImpl(private val wordsRepository: WordsRepository) : WordsInteractor {
    override suspend fun getWordsByCategory(categoryName: String): List<WordUI> =
        wordsRepository.getWordsByCategory(categoryName).map { word ->
            WordUI(id = word.id, lexeme = word.lexeme, translation = word.translation)
        }

    override fun getAllCategories(): Flow<List<String>> =
        wordsRepository.getAllCategories()

    override suspend fun updateCategory(
        oldCategoryName: String,
        newCategoryName: String,
        wordUIList: List<WordUI>
    ) {
        val words = wordUIList.map { wordUI ->
            Word(
                lexeme = wordUI.lexeme,
                translation = wordUI.translation,
                category = newCategoryName
            )
        }
        wordsRepository.updateCategory(oldCategoryName, words)
    }

    override suspend fun removeCategory(categoryName: String) {
        wordsRepository.removeCategory(categoryName)
    }

    override suspend fun addNewCategory(wordUIList: List<WordUI>, newCategoryName: String) {
        val words = wordUIList.map { wordUI ->
            Word(
                lexeme = wordUI.lexeme,
                translation = wordUI.translation,
                category = newCategoryName
            )
        }
        wordsRepository.addNewCategory(words)
    }
}