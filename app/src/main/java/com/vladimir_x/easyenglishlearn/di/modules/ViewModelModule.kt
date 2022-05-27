package com.vladimir_x.easyenglishlearn.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vladimir_x.easyenglishlearn.ui.category_edit.CategoryEditViewModel
import com.vladimir_x.easyenglishlearn.ui.category_select.CategoryViewModel
import com.vladimir_x.easyenglishlearn.ui.exercises.ConstructorViewModel
import com.vladimir_x.easyenglishlearn.ui.exercises.ExerciseViewModel
import com.vladimir_x.easyenglishlearn.ui.exercises.QuizViewModel
import com.vladimir_x.easyenglishlearn.ui.word_selection.WordSelectionViewModel
import com.vladimir_x.easyenglishlearn.util.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @[IntoMap ViewModelKey(WordSelectionViewModel::class)]
    internal abstract fun bindWordSelectionViewModel(viewModel: WordSelectionViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(CategoryViewModel::class)]
    internal abstract fun bindCategoryViewModel(viewModel: CategoryViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(CategoryEditViewModel::class)]
    internal abstract fun bindCategoryEditViewModel(viewModel: CategoryEditViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(ConstructorViewModel::class)]
    internal abstract fun bindConstructorViewModel(viewModel: ConstructorViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(QuizViewModel::class)]
    internal abstract fun bindQuizViewModel(viewModel: QuizViewModel): ViewModel
}