package com.algoholic.algo.find

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.algoholic.R
import com.algoholic.algo.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FindElementFragment : Fragment() {
    private lateinit var viewModel: FindElementViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_find, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, ">> onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        initVM()

        viewModel.viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, ">> await Dispatchers.IO")

            withContext(Dispatchers.Main) {
                Log.d(TAG, ">> await Dispatchers.Main")
                createAnimator().await()
                Log.d(TAG, "<< await Dispatchers.Main")
            }

            Log.d(TAG, "<< await Dispatchers.IO")
        }

        Log.d(TAG, "<< onViewCreated")
    }

    private fun initVM() {
        viewModel = ViewModelProvider(this).get(FindElementViewModel::class.java)
    }

    var bottom = 1000
    private fun createAnimator(): ValueAnimator {
        val propertyHeight: PropertyValuesHolder = PropertyValuesHolder.ofInt("PROPERTY_HEIGHT", 0, bottom)
        val animator = ValueAnimator()
        animator.setValues(propertyHeight)
        animator.duration = 200
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener {
            Log.d(TAG, "value - ${it.getAnimatedValue("PROPERTY_HEIGHT") as Int} ")
        }
        animator.start()

        return animator
    }
}

private const val TAG = "FindElementFragment"