package android.example.com.kotlinoidruntimepermissions

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.techwolf.android.sample.permissions.AppPermission
import com.techwolf.android.sample.permissions.handlePermission
import com.techwolf.android.sample.permissions.onRequestPermissionsResultReceived
import com.techwolf.android.sample.permissions.requestPermission
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.content_view as contentView
import kotlinx.android.synthetic.main.content_main.image_view as imageView


class MainActivity : AppCompatActivity() {

    companion object {
        const private val DATA_KEY = "data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            captureCameraImage()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === AppPermission.CAMERA_PERMISSION.requestCode && resultCode === Activity.RESULT_OK) {
            val photo = data?.extras?.get(DATA_KEY) as? Bitmap
            imageView.setImageBitmap(photo)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResultReceived(requestCode, permissions, grantResults,
                onPermissionGranted = {
                    startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), it.requestCode)
                },
                onPermissionDenied = {
                    snackbarWithoutAction(it.deniedMsgId)
                }
        )
    }

    fun captureCameraImage() = handlePermission(AppPermission.CAMERA_PERMISSION,
            onGranted = {
                startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), it.requestCode)
            },
            onDenied = {
                requestPermission(it)
            },
            onExplanationNeeded = {
                snackbarWithAction(it.explanationMsgId) {
                    requestPermission(it)
                }
            })

    fun snackbarWithAction(messageId: Int, action: () -> Unit) {
        Snackbar.make(contentView, messageId, Snackbar.LENGTH_LONG)
                .setAction(R.string.request_permission) { action() }
                .show()
    }

    fun snackbarWithoutAction(messageId: Int) =
            Snackbar.make(contentView, messageId, Snackbar.LENGTH_SHORT).show()
}
