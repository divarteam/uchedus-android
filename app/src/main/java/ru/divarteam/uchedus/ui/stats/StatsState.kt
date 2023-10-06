package ru.divarteam.uchedus.ui.stats

sealed class StatsState {
    object Initializing : StatsState()
    class Error(val e: Throwable): StatsState()
    object Loading: StatsState()
    object Idle: StatsState()
    object Unauthorized: StatsState()
}