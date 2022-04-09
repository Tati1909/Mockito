package com.example.tests.tests_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tests.R
import com.example.tests.databinding.FragmentDetailsBinding
import java.util.Locale

/**
 * На этом экране мы будем отображать количество найденных аккаунтов по запросу.
 */
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    var count: Int? = null

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    private fun setUI() {
        arguments?.let {
            val counter = it.getInt(TOTAL_COUNT_EXTRA, 0)
            viewModel.setCounter(counter)
            setCountText(counter)
        }
        binding.decrementButton.setOnClickListener {
            count = viewModel.onDecrement()
            setCountText(count!!)
        }
        binding.incrementButton.setOnClickListener {
            count = viewModel.onIncrement()
            setCountText(count!!)
        }
    }

    fun setCount(count: Int) {
        setCountText(count)
    }

    private fun setCountText(count: Int) {
        binding.totalCountTextViewDetails.text =
            String.format(
                Locale.getDefault(), getString(R.string.results_count),
                count
            )
    }

    companion object {

        private const val TOTAL_COUNT_EXTRA = "TOTAL_COUNT_EXTRA"

        @JvmStatic
        fun newInstance(counter: Int) =
            DetailsFragment().apply {
                arguments = bundleOf(
                    TOTAL_COUNT_EXTRA to
                        counter
                )
            }
    }
}