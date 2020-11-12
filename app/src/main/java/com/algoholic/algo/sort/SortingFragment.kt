package com.algoholic.algo.sort

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.algoholic.R
import kotlinx.android.synthetic.main.fragment_sort.*
import kotlinx.android.synthetic.main.player_view.*
import kotlinx.coroutines.launch

class SortingFragment : Fragment() {

    private lateinit var viewModel: SortingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sort, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVM()

        initClickListeners()

        viewModel.viewModelScope.launch {

            observeColumnsChanges()

            observeStartStopButton()
        }

        waitForViewSize()
    }

    private fun initClickListeners() {
        ivForward.setOnClickListener {
            viewModel.forwardButtonPressed()
        }

        ivRewind.setOnClickListener {
            viewModel.rewindButtonPressed()
        }

        ivStartStop.setOnClickListener {
            viewModel.startStopButtonPressed()
        }
    }

    private fun observeColumnsChanges() {
        viewModel.columns.observe(this, {
            svSortingView.columns = it
        })

        viewModel.columnIndexesToAnimate.observe(this, {
            svSortingView.invalidateByIndex(it)
        })
    }

    private fun observeStartStopButton() {
        viewModel.playStopButtonState.observe(this, {
            ivStartStop.setImageDrawable(getDrawable(requireContext(), it.resIdRes))
        })
    }

    private fun waitForViewSize() {
        svSortingView.doOnLayout {
            val width = svSortingView.width
            Log.d(TAG, "width - $width")

            viewModel.generateCollection(width)
        }
    }

    private fun initVM() {
        viewModel = ViewModelProvider(this).get(SortingViewModel::class.java)
        viewLifecycleOwner.lifecycle.addObserver(viewModel)
    }
}

private const val TAG = "SortingFragment"