package com.techwolf.android.sample.camera

import android.content.Context
import android.hardware.Camera
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView


class CameraPreview(context: Context,
                    private var camera: Camera,
                    private var cameraInfo: Camera.CameraInfo,
                    private var displayOrientation: Int
) : SurfaceView(context), SurfaceHolder.Callback {
    private var surfaceHolder: SurfaceHolder

    companion object {
        private val TAG: String = CameraPreview::class.java.simpleName
    }

    init {
        surfaceHolder = getSurfaceHolder().apply {
            addCallback(this@CameraPreview)
        }
    }

    fun getSurfaceHolder(): SurfaceHolder = holder

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        holder?.let {
            camera.run {
                tryTo { stopPreview() }

                val orientation = calculatePreviewOrientation(cameraInfo, displayOrientation)
                setDisplayOrientation(orientation)

                tryTo {
                    setPreviewDisplay(it)
                    startPreview()
                }
            }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {}

    override fun surfaceCreated(holder: SurfaceHolder?) = camera.tryTo {
        setPreviewDisplay(surfaceHolder)
        startPreview()
    }


    fun calculatePreviewOrientation(info: Camera.CameraInfo, displayOrientation: Int): Int {
        var degrees = displayOrientation.degrees

        var result: Int = if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            (360 - ((info.orientation + degrees) % 360)) % 360  // compensate the mirror
        } else {  // back-facing
            (info.orientation - degrees + 360) % 360
        }

        return result
    }

    fun setCamera(newCamera: Camera, newCameraInfo: Camera.CameraInfo, orientation: Int) = takeIf { camera != null || cameraInfo != null }?.apply {
        camera = newCamera
        cameraInfo = newCameraInfo
        displayOrientation = orientation
    }

    private val Int.degrees: Int get() = when (this) {
        Surface.ROTATION_0 -> 0
        Surface.ROTATION_90 -> 90
        Surface.ROTATION_180 -> 180
        Surface.ROTATION_270 -> 270
        else -> 0
    }
}