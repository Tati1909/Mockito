package com.example.tests.presenter

import com.example.tests.provider.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Это специальный класс для тестирования. Обычные scheduler’ы для Unit-тестов
не подходят — при попытке запустить тест он будет падать с NPE. Специально для тестирования в rx
есть метод Schedulers.trampoline(), который позволяет запускать код без крашей. Но вы должны
понимать, что ни о каком реальном запросе в сеть речи не идет — с реальными запросами в сеть мы
работали в Инструментальных тестах и там речь больше о нажатии на кнопку на экране и ожидании
результата, который можно сравнить с предполагаемым.
 */
class ScheduleProviderStub : SchedulerProvider {

    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }
}