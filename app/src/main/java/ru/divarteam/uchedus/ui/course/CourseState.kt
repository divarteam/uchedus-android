package ru.divarteam.uchedus.ui.course

sealed class CourseState {
    object Initializing : CourseState()
    class Error(e: Throwable): CourseState()
    object Loading: CourseState()
    object Idle: CourseState()

}