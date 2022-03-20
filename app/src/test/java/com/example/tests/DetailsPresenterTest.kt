package com.example.tests

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tests.tests_details.DetailsPresenter
import com.example.tests.tests_details.ViewDetailsContract
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

/**
 * эта аннотация используется для  Robolectric, чтобы TestRunner запускал работу в виде тестов
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class DetailsPresenterTest {

    private lateinit var presenter: DetailsPresenter

    @Mock
    private lateinit var viewContract: ViewDetailsContract

    private var count: Int = 0

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = DetailsPresenter(viewContract, count)
    }
}