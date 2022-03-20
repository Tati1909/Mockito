package com.example.tests.tests_details

import com.example.tests.presenter.PresenterContract

internal interface PresenterDetailsContract : PresenterContract {

    fun setCounter(count: Int)
    fun onIncrement()
    fun onDecrement()
}