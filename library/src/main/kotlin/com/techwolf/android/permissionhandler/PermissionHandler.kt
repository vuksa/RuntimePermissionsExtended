@file:JvmName("PermissionHandler")

package com.techwolf.android.permissionhandler

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.*
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.support.v4.content.ContextCompat.checkSelfPermission

fun isLollipopOrBellow(): Boolean = android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP

/***********************************
 * HANDLE PERMISSIONS IN FRAGMENTS *
 **********************************/

inline fun Fragment.isPermissionGranted(permission: AppPermission) = checkSelfPermission(activity, permission.permissionName) == PackageManager.PERMISSION_GRANTED

inline fun Fragment.isRationaleExplanationNeeded(permission: AppPermission) = shouldShowRequestPermissionRationale(activity, permission.permissionName)

inline fun Fragment.requestPermission(permission: AppPermission) = requestPermissions(arrayOf(permission.permissionName), permission.requestCode)

inline fun Fragment.handlePermission(permission: AppPermission,
                                     onGranted: (AppPermission) -> Unit,
                                     onDenied: (AppPermission) -> Unit,
                                     onExplanationNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleExplanationNeeded(permission) -> onExplanationNeeded(permission)
        else -> onDenied(permission)
    }
}

inline fun Fragment.handlePermission(permission: AppPermission,
                                     onGranted: (AppPermission) -> Unit,
                                     onExplanationNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleExplanationNeeded(permission) -> onExplanationNeeded(permission)
        else -> requestPermission(permission)
    }
}

/************************************
 * HANDLE PERMISSIONS IN ACTIVITIES *
 ***********************************/

inline fun Activity.isPermissionGranted(permission: AppPermission) = checkSelfPermission(this, permission.permissionName) == PackageManager.PERMISSION_GRANTED

inline fun Activity.isRationaleExplanationNeeded(permission: AppPermission) = shouldShowRequestPermissionRationale(this, permission.permissionName)

inline fun Activity.requestPermission(permission: AppPermission) = requestPermissions(this, arrayOf(permission.permissionName), permission.requestCode)

inline fun Activity.handlePermission(permission: AppPermission,
                                     onGranted: (AppPermission) -> Unit,
                                     onDenied: (AppPermission) -> Unit,
                                     onExplanationNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleExplanationNeeded(permission) -> onExplanationNeeded(permission)
        else -> onDenied(permission)
    }
}

inline fun Activity.handlePermission(permission: AppPermission,
                                     onGranted: (AppPermission) -> Unit,
                                     onExplanationNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleExplanationNeeded(permission) -> onExplanationNeeded(permission)
        else -> requestPermission(permission)
    }
}

/*********************************************
 * HANDLE onRequestPermissionResult CALLBACK *
 ********************************************/

fun onRequestPermissionsResultReceived(requestCode: Int, permissions: Array<out String>,
                                       grantResults: IntArray,
                                       onPermissionGranted: (AppPermission) -> Unit,
                                       onPermissionDenied: (AppPermission) -> Unit) {
    AppPermission.permissions.find { it.requestCode == requestCode }?.apply {
        analisePermissionResult(grantResults[0], onPermissionGranted, onPermissionDenied)
    }
}

private fun AppPermission.analisePermissionResult(grantResults: Int, onPermissionGranted: (AppPermission) -> Unit, onPermissionDenied: (AppPermission) -> Unit) {
    if (grantResults == PackageManager.PERMISSION_GRANTED) {
        onPermissionGranted(this)
    } else {
        onPermissionDenied(this)
    }
}