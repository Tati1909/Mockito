package com.example.tests.tests_details

import android.util.Log
import com.example.tests.view.ViewContract

internal class DetailsPresenter internal constructor(
    private val viewContract: ViewDetailsContract,
    private var count: Int = 0
) : PresenterDetailsContract {

    override fun onAttach(view: ViewContract) {}

    override fun setCounter(count: Int) {
        this.count = count
    }

    override fun onIncrement() {
        count++
        viewContract.setCount(count)
    }

    override fun onDecrement() {
        count--
        viewContract.setCount(count)
    }

    override fun onDetach() {
        Log.d("DetailActivity", "onDetach")

        super.onDetach()
    }
}
