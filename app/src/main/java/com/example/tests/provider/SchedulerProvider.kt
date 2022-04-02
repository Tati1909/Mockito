package com.example.tests.provider

import io.reactivex.Scheduler

interface SchedulerProvider {

    fun ui(): Scheduler
    fun io(): Scheduler
}