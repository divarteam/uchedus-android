package ru.divarteam.uchedus.ui.profile

sealed class ProfileState {
    object Initializing : ProfileState()
    class Error(val e: Throwable): ProfileState()
    object Loading: ProfileState()
    object Idle: ProfileState()
    object Unauthorized: ProfileState()

}