package com.example.mockito.tests_details

import com.example.mockito.presenter.PresenterContract

internal interface PresenterDetailsContract : PresenterContract {

    fun setCounter(count: Int)
    fun onIncrement()
    fun onDecrement()
}