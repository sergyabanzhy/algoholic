package com.algoholic.algo.graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.algoholic.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom.*
import kotlinx.android.synthetic.main.fragment_graph.*

class PathFindingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_graph, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBottom()

        initRadioGroup()

        initSeekBar()

        initButtons()
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private fun initButtons() {

        btnStart.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            boardView.start()
        }

        btnClear.setOnClickListener {
            boardView.clear()
        }
    }

    private fun initBottom() {

        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.isHideable = false
    }

    private fun initSeekBar() {
        seek.min = 0
        seek.max = 100

        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                boardView.changeSpeed(0 - progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

    private fun initRadioGroup() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.astar -> {
                    boardView.isAstarSelected = true
                }
                R.id.dijkstra -> {
                    boardView.isAstarSelected = false
                }
            }
        }
    }
}