package ru.divarteam.uchedus.ui.product

sealed class ProductState {
    object Initializing : ProductState()
    class Error(val e: Throwable): ProductState()
    object Loading: ProductState()
    object Idle: ProductState()
    object Unauthorized: ProductState()
}