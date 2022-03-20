package com.example.tests.automator

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 23)
class OpenOtherAppsTest {

    private val uiDevice: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    /** Открываем экран со списком установленных приложений. Обратите внимание, что на устройстве,
     * для которого писался этот тест (Samsung SM-A325), список приложений открывается
     * свайпом снизу вверх на главном экране. Но иконка "Настройки" находится на главном экране.
     * Именно на главном экране будем открывать приложение.
     */
    @Test
    fun test_OpenSettings() {

        //Для этого достаточно свернуть все приложения через uiDevice.pressHome() и найти Настройки на главном экране
        uiDevice.pressHome()
        val allAppsButton: UiObject = uiDevice.findObject(UiSelector().description("Настройки"))
        //Открываем
        allAppsButton.clickAndWaitForNewWindow()

        //Убеждаемся, что Настройки открыты
        val settingsValidation = uiDevice.findObject(UiSelector().packageName("com.android.settings"))
        Assert.assertTrue(settingsValidation.exists())
    }
}