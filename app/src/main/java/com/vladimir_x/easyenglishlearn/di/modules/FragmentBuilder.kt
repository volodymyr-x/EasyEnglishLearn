package com.vladimir_x.easyenglishlearn.di.modules

import com.vladimir_x.easyenglishlearn.ui.category_edit.CategoryEditFragment
import com.vladimir_x.easyenglishlearn.ui.category_select.CategoryFragment
import com.vladimir_x.easyenglishlearn.ui.category_select.CategoryRemoveFragment
import com.vladimir_x.easyenglishlearn.ui.exercises.ConstructorFragment
import com.vladimir_x.easyenglishlearn.ui.exercises.QuizFragment
import com.vladimir_x.easyenglishlearn.ui.word_selection.ExerciseChoiceFragment
import com.vladimir_x.easyenglishlearn.ui.word_selection.WordSelectionFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {
    @ContributesAndroidInjector()
    abstract fun contributeCategoryFragment(): CategoryFragment

    @ContributesAndroidInjector()
    abstract fun contributeCategoryEditFragment(): CategoryEditFragment

    @ContributesAndroidInjector()
    abstract fun contributeWordSelectionFragment(): WordSelectionFragment

    @ContributesAndroidInjector()
    abstract fun contributeExerciseChoiceFragment(): ExerciseChoiceFragment

    @ContributesAndroidInjector()
    abstract fun contributeConstructorFragment(): ConstructorFragment

    @ContributesAndroidInjector()
    abstract fun contributeQuizFragment(): QuizFragment

    @ContributesAndroidInjector()
    abstract fun contributeCategoryRemoveFragment(): CategoryRemoveFragment
}