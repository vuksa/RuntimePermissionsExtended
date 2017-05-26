@file:JvmName("PermissionHandler")

package android.example.com.kotlinoidruntimepermissions.permissions

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat


inline fun Activity.isPermissionGranted(permission: String) = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

inline fun Fragment.isPermissionGranted(permission: String) = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

inline fun Activity.isRationaleExplanationNeeded(permission: String) = ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

inline fun Fragment.isRationaleExplanationNeeded(permission: String) = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

inline fun Activity.requestPermission(permission: AppPermission) = ActivityCompat.requestPermissions(this, arrayOf(permission.permissionName), permission.requestCode)

inline fun Fragment.requestPermission(permission: AppPermission) = requestPermissions(arrayOf(permission.permissionName), permission.requestCode)

inline fun Activity.handlePermission(permission: AppPermission,
                                     onGranted: (AppPermission) -> Unit,
                                     onDenied: (AppPermission) -> Unit,
                                     onExplanationNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission.permissionName) -> onGranted(permission)
        isRationaleExplanationNeeded(permission.permissionName) -> onExplanationNeeded(permission)
        else -> onDenied(permission)
    }
}

inline fun Activity.handlePermission(permission: AppPermission,
                                     onGranted: (AppPermission) -> Unit,
                                     onExplanationNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission.permissionName) -> onGranted(permission)
        isRationaleExplanationNeeded(permission.permissionName) -> onExplanationNeeded(permission)
        else -> requestPermission(permission)
    }
}

inline fun Fragment.handlePermission(permission: AppPermission,
                                     onGranted: (AppPermission) -> Unit,
                                     onDenied: (AppPermission) -> Unit,
                                     onExplanationNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission.permissionName) -> onGranted(permission)
        isRationaleExplanationNeeded(permission.permissionName) -> onExplanationNeeded(permission)
        else -> onDenied(permission)
    }
}

inline fun Fragment.handlePermission(permission: AppPermission,
                                     onGranted: (AppPermission) -> Unit,
                                     onExplanationNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission.permissionName) -> onGranted(permission)
        isRationaleExplanationNeeded(permission.permissionName) -> onExplanationNeeded(permission)
        else -> requestPermission(permission)
    }
}

fun isLollipopOrBellow(): Boolean = Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP