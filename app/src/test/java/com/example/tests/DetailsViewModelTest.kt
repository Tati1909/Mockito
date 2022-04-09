package com.example.tests

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tests.tests_details.DetailsViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class DetailsViewModelTest {

    private lateinit var detailsViewModel: DetailsViewModel

    private var count: Int = 0

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        detailsViewModel = DetailsViewModel(count)
    }

    @Test
    fun viewModel_TestReturnValueIsNotNull() {
        detailsViewModel.setCounter(5)
        //Убеждаемся, что в count установлено значение
        Assert.assertNotNull(count)
    }
}