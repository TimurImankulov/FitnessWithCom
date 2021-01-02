package com.example.fitness.ui

interface LiveCycle<V> {
    fun bind(view: V)
    fun unbind()
}