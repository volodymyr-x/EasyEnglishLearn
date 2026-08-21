package com.volodymyr_x.easyenglishlearn.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.volodymyr_x.easyenglishlearn.ui.category_edit.CategoryEditScreen
import com.volodymyr_x.easyenglishlearn.ui.category_edit.CategoryEditViewModel
import com.volodymyr_x.easyenglishlearn.ui.category_select.CategorySelectScreen
import com.volodymyr_x.easyenglishlearn.ui.exercises.constructor.ExerciseConstructorScreen
import com.volodymyr_x.easyenglishlearn.ui.exercises.quiz.ExerciseQuizScreen
import com.volodymyr_x.easyenglishlearn.ui.word_selection.ExerciseType
import com.volodymyr_x.easyenglishlearn.ui.word_selection.WordSelectionScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import java.util.Map.entry

@Composable
fun NavigationRoot(
    showMessageAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.CategoryList::class, Route.CategoryList.serializer())
                    subclass(Route.CategoryEdit::class, Route.CategoryEdit.serializer())
                    subclass(Route.CategoryAdd::class, Route.CategoryAdd.serializer())
                    subclass(Route.WordSelection::class, Route.WordSelection.serializer())
                    subclass(Route.ExerciseQuiz::class, Route.ExerciseQuiz.serializer())
                    subclass(
                        Route.ExerciseConstructor::class,
                        Route.ExerciseConstructor.serializer()
                    )
                }
            }
        },
        Route.CategoryList
    )
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { route ->
            when (route) {
                is Route.CategoryList -> {
                    NavEntry(
                        key = route,
                        content = {
                            CategorySelectScreen(
                                action = { route ->
                                    backStack.add(route)
                                }
                            )
                        }
                    )
                }
                is Route.CategoryEdit -> {
                    NavEntry(
                        key = route,
                        content = {
                            CategoryEditScreen(
                                oldCategoryName = route.categoryName,
                                closeFragmentAction = { backStack.removeAt(backStack.lastIndex) },
                                showMessageAction = showMessageAction
                            )
                        }
                    )
                }
                is Route.CategoryAdd -> {
                    NavEntry(
                        key = route,
                        content = {
                            CategoryEditScreen(
                                oldCategoryName = "",
                                closeFragmentAction = { backStack.removeAt(backStack.lastIndex) },
                                showMessageAction = showMessageAction
                            )
                        }
                    )
                }
                is Route.WordSelection -> {
                    NavEntry(
                        key = route,
                        content = {
                            WordSelectionScreen(
                                route.categoryName,
                                startExerciseAction = { wordSelectionResult ->
                                    when (wordSelectionResult.exerciseType) {
                                        ExerciseType.QUIZ -> backStack.add(
                                            Route.ExerciseQuiz(
                                                wordSelectionResult
                                            )
                                        )
                                        ExerciseType.CONSTRUCTOR -> backStack.add(
                                            Route.ExerciseConstructor(
                                                wordSelectionResult
                                            )
                                        )
                                    }
                                },
                                showMessageAction = showMessageAction
                            )
                        }
                    )
                }
                is Route.ExerciseQuiz -> {
                    NavEntry(
                        key = route,
                        content = {
                            ExerciseQuizScreen(
                                wordSelectionResult = route.result,
                                closeFragmentAction = { backStack.removeAt(backStack.lastIndex) }
                            )
                        }
                    )
                }
                is Route.ExerciseConstructor ->
                    NavEntry(
                        key = route,
                        content = {
                            ExerciseConstructorScreen(
                                wordSelectionResult = route.result,
                                closeFragmentAction = { backStack.removeAt(backStack.lastIndex) }
                            )
                        }
                    )
                else -> error("Unhandled route: $route")
            }
        },
        modifier = modifier

    )
}
