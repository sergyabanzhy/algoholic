package com.algoholic

import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.algoholic.graph.BoardView
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {

    private lateinit var boardView: BoardView
    private lateinit var btnStart: Button
    private lateinit var btnClear: Button
    private lateinit var seekSpeed: SeekBar
    private lateinit var llBottomSheet: ConstraintLayout
    private lateinit var radioGroup: RadioGroup
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        boardView = findViewById(R.id.boardView1)

        initBottom()

        initRadioGroup()

        initSeekBar()

        initButtons()

    }

    private fun initButtons() {
        btnStart = findViewById(R.id.btnStart)
        btnClear = findViewById(R.id.btnClear)

        btnStart.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            boardView.start()
        }

        btnClear.setOnClickListener {
            boardView.clear()
        }
    }

    private fun initBottom() {
        llBottomSheet = findViewById(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.isHideable = false
    }

    private fun initSeekBar() {
        seekSpeed = findViewById(R.id.seek)
        seekSpeed.min = 0
        seekSpeed.max = 100

        seekSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

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
        radioGroup = findViewById(R.id.radioGroup)
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