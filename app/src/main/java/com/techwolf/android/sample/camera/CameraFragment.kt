package com.techwolf.android.sample.camera

import android.hardware.Camera
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.techwolf.android.sample.R
import java.lang.Exception

class CameraFragment : Fragment() {
    private var camera: Camera? = null
    private var cameraInfo: Camera.CameraInfo? = null
    private var cameraPreview: CameraPreview? = null

    companion object {
        fun instance() = CameraFragment()

        val TAG: String = CameraFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.preview_fragment_layout, null)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeCamera()
    }

    private fun initializeCamera() {
        camera = createCamera()

        camera?.let {
            cameraInfo = Camera.CameraInfo()
            Camera.getCameraInfo(it.frontCameraId, cameraInfo)

            // Get the rotation of the screen to adjust the preview image accordingly.
            val displayRotation = this.orientation

            view?.let {
                val preview = it.findViewById(R.id.camera_container) as FrameLayout
                preview.removeAllViews()

                cameraPreview = if (cameraPreview == null) {
                    CameraPreview(activity, camera!!, cameraInfo!!, displayRotation)
                } else {
                    cameraPreview?.apply { setCamera(camera!!, cameraInfo!!, displayRotation) }
                }

                preview.addView(cameraPreview)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initializeCamera()
    }

    override fun onPause() {
        super.onPause()
        if (camera != null) {
            cameraPreview?.holder?.surface?.release()
            camera!!.release()
            camera = null
        }
    }


    /**
     * @param cameraId camera id of front camera
     */

    fun createCamera(cameraId: Int = 0): Camera? = try {
        Camera.open(cameraId)
    } catch(exception: Exception) {
        Log.d(Fragment::class.java.simpleName, "Error happened during CameraPreview setup ${exception.message}")
        null
    }
}