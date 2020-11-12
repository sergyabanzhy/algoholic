package com.algoholic.algo

import android.animation.Animator
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun Animator.await() = suspendCancellableCoroutine<Unit> { continuation ->
    continuation.invokeOnCancellation { cancel() }

    doOnEnd {
        continuation.resume(Unit)
    }

    doOnCancel { continuation.cancel() }
}