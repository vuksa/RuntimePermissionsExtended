package com.techwolf.android.sample

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.techwolf.android.permissionhandler.AppPermission
import com.techwolf.android.permissionhandler.handlePermission
import com.techwolf.android.permissionhandler.onRequestPermissionsResultReceived
import com.techwolf.android.permissionhandler.requestPermission
import com.techwolf.android.sample.camera.CameraFragment
import com.techwolf.android.sample.camera.gone
import com.techwolf.android.sample.camera.isVisible
import com.techwolf.android.sample.camera.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.content_view as contentView
import kotlinx.android.synthetic.main.activity_main.image_view as imageView
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            if (imageView.isVisible) {
                showCameraPreviewScreen()
            } else {
                closeCameraPreviewScreen()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResultReceived(requestCode, permissions, grantResults,
                onPermissionGranted = {
                    openCameraPreviewScreen()
                },
                onPermissionDenied = {
                    snackbarWithAction(it.deniedMessageId, R.string.action_settings) {
                        openPermissionSettingsScreen()
                    }
                }
        )
    }

    fun showCameraPreviewScreen() {
        handlePermission(AppPermission.CAMERA,
                onGranted = {
                    openCameraPreviewScreen()
                }, onDenied = {
                    requestPermission(it)
                }, onExplanationNeeded = {
                    snackbarWithAction(it.explanationMessageId) { requestPermission(it) }
                })
    }

    fun snackbarWithAction(messageId: Int, actionText: Int = R.string.request_permission, action: () -> Unit) {
        Snackbar.make(contentView, messageId, Snackbar.LENGTH_LONG)
                .setAction(actionText) { action() }
                .show()
    }

    private fun openPermissionSettingsScreen() {
        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intent.data = Uri.fromParts(getString(R.string.package_key_word), packageName, null)
        startActivity(intent)
    }

    private fun openCameraPreviewScreen() {
        imageView.gone()
        container.visible()
        button.text = getString(R.string.back)
        supportFragmentManager.beginTransaction()
                .add(R.id.container, CameraFragment.instance())
                .addToBackStack(CameraFragment.TAG)
                .commit()
    }

    private fun closeCameraPreviewScreen() {
        button.text = getString(R.string.open_preview_screen)
        container.gone()
        imageView.visible()
        supportFragmentManager.popBackStack()
    }

}
