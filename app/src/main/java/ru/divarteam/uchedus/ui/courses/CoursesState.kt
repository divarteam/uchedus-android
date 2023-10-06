package ru.divarteam.uchedus.ui.courses

sealed class CoursesState {
    object Initializing : CoursesState()
    class Error(val e: Throwable): CoursesState()
    object Loading: CoursesState()
    object Idle: CoursesState()
    object Unauthorized: CoursesState()
    object RoleForbiddenTemporary: CoursesState()

}