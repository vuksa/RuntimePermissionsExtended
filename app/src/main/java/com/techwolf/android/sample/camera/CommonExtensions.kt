package com.techwolf.android.sample.camera

import android.hardware.Camera
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import java.io.IOException
import java.lang.Exception


val Fragment.orientation: Int get() = activity.windowManager.defaultDisplay.rotation

val Camera.frontCameraId: Int get() = 0

fun Camera.setCameraInfo(cameraId: Int, cameraInfo: Camera.CameraInfo) = Camera.getCameraInfo(cameraId, cameraInfo)

inline fun View.gone() {
    visibility = View.GONE
}

inline fun View.visible() {
    visibility = View.VISIBLE
}

inline fun View.invisible() {
    visibility = View.INVISIBLE
}

val View.isVisible: Boolean get() = visibility == View.VISIBLE


inline fun <reified T : Any?> T.tryTo(block: T.() -> Unit) {
    try {
        block()
    } catch (exception: IOException) {
        Log.d(T::class.java.simpleName, "Error happened during CameraPreview setup ${exception.message}")
    }
}