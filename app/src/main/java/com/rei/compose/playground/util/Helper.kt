package com.rei.compose.playground.util


fun <E> MutableList<E>.set(list: List<E>) {
    this.clear()
    this.addAll(list)
}