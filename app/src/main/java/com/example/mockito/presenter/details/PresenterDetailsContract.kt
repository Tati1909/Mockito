package com.example.mockito.presenter.details

import com.example.mockito.presenter.PresenterContract

internal interface PresenterDetailsContract : PresenterContract {

    fun setCounter(count: Int)
    fun onIncrement()
    fun onDecrement()
}