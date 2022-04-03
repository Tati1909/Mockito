package com.example.tests.tests_details

import androidx.lifecycle.ViewModel

class DetailsViewModel(
    private var count: Int = 0
) : ViewModel() {

    fun setCounter(count: Int) {
        this.count = count
    }

    fun onIncrement(): Int {
        count++
        return count
    }

    fun onDecrement(): Int {
        count--
        return count
    }
}