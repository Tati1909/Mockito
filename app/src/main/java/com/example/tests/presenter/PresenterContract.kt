package com.example.tests.presenter

import com.example.tests.view.ViewContract

/**
 * Общий для всех экранов интерфейс с функциями onAttach() и onDetach()
 * Помним, что когда тело функции пустое, то можем эту функцию далее не переопределять
 */
internal interface PresenterContract {

    fun onAttach(view: ViewContract) {}

    fun onDetach() {}
}