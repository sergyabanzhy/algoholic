package com.algoholic.algo.sort

import android.util.Log
import androidx.annotation.CallSuper
import androidx.lifecycle.*
import com.algoholic.R
import com.algoholic.algo.sort.sortingplayer.SortingProcessPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlin.random.Random

class SortingViewModel(private val sortingProcessPlayer: SortingProcessPlayer = SortingProcessPlayer(BubbleSort())): ViewModel(), LifecycleObserver {

    private var isStarted: Boolean = false

    val columns : MutableLiveData<List<IPaintable>> = MutableLiveData()

    val playStopButtonState : MutableLiveData<PlayStopButtonState> = MutableLiveData(if (isStarted) PlayStopButtonState.Started() else PlayStopButtonState.Stopped())

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        Log.d(TAG, "onStart")
    }

    private fun initializeColumns(width: Int) {
        val columns = mutableListOf<Column>()

        for (y in 0 until width step 8) {
            Log.d(TAG, "x = $y")
            if (y == 0) {
                columns.add(Column(left = 0, top = y, right = y + 6, bottom = Random.nextInt(300)))
            } else {
                columns.add(Column(left = y, top = 0, right = y + 6, bottom = Random.nextInt(300)))
            }
        }

        Log.d(TAG, "columns size - ${columns.size}")

        sortingProcessPlayer.collectionToVisualize(columns)

        this.columns.value = columns
    }

    fun forwardButtonPressed() {
        Log.d(TAG, "forwardButtonPressed")

        sortingProcessPlayer.stepForward()
    }

    fun rewindButtonPressed() {
        Log.d(TAG, "rewindButtonPressed")

        sortingProcessPlayer.stepBackward()
    }

    fun startStopButtonPressed() {
        Log.d(TAG, "startStopButtonPressed")

        if (isStarted) {
            viewModelScope.coroutineContext.cancelChildren()
            sortingProcessPlayer.stopSorting {
                playStopButtonState.value = PlayStopButtonState.Stopped()
                isStarted = false
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                sortingProcessPlayer.startSorting(started = {
                    playStopButtonState.postValue(PlayStopButtonState.Started())
                    isStarted = true
                }, sorted = {
                    Log.d(TAG, "sorted, columns size - ${columns.value?.size}")
                    playStopButtonState.postValue(PlayStopButtonState.Stopped())
                    isStarted = false
                    Log.d(TAG, "sorted")
                }, invalidate = {
                    columns.postValue(columns.value)
                })
            }
        }
    }

    fun generateCollection(width: Int) {
        initializeColumns(width)
    }
}

sealed class PlayStopButtonState {
    abstract val resIdRes: Int
    data class Stopped(override val resIdRes: Int = R.drawable.ic_play) : PlayStopButtonState()
    data class Started(override val resIdRes: Int = R.drawable.ic_stop) : PlayStopButtonState()
}


private const val TAG = "SortingViewModel"