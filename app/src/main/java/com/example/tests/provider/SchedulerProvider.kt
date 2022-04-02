package com.example.tests.provider

import io.reactivex.Scheduler

internal interface SchedulerProvider {

    fun ui(): Scheduler
    fun io(): Scheduler
}