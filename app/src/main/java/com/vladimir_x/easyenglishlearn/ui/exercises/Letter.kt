package com.vladimir_x.easyenglishlearn.ui.exercises

import kotlin.random.Random

data class Letter(
    val id: Int = Random.nextInt(),
    val value: String
    )
