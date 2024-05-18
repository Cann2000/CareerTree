package com.example.careertree.model.game

import com.example.careertree.model.salary.Salary

data class Game(

    val languages: List<GameEngine>?,

    val salary:List<Salary>?
) {
}