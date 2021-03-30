package com.flynnchow.zero.common.helper

import android.app.Activity
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.lang.ref.WeakReference

class ActivityHelper(
    context: Activity,
    lifecycle: Lifecycle
) : LifecycleObserver {
    companion object {
        @JvmStatic
        private val activityTask = ArrayList<WeakReference<Activity>>()

        @JvmStatic
        fun clearTask() {
            for (activity in activityTask) {
                activity.get()?.finish()
            }
            activityTask.clear()
        }
    }
    private var activityRef: WeakReference<Activity> = WeakReference(context)

    init {
        activityTask.add(activityRef)
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        logDebug("des")
        activityTask.remove(activityRef)
    }
}