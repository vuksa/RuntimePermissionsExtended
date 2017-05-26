package android.example.com.kotlinoidruntimepermissions.permissions

import android.Manifest
import android.example.com.kotlinoidruntimepermissions.R

enum class AppPermission(val permissionName: String,
                         val requestCode: Int,
                         val deniedMsgId: Int,
                         val explanationMsgId: Int) {
    CAMERA_PERMISSION(Manifest.permission.CAMERA, 1, R.string.permission_camera_denied, R.string.permission_camera_explanation);
}