@file:JvmName("PermissionHandler")

package com.techwolf.android.permissionhandler

import android.app.Activity
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.support.v4.content.ContextCompat.checkSelfPermission

fun isLollipopOrBellow(): Boolean = android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP

/***********************************
 * HANDLE PERMISSIONS IN FRAGMENTS *
 **********************************/

inline fun android.support.v4.app.Fragment.isPermissionGranted(permission: String) = checkSelfPermission(activity, permission) == android.content.pm.PackageManager.PERMISSION_GRANTED

inline fun android.support.v4.app.Fragment.isRationaleExplanationNeeded(permission: String) = shouldShowRequestPermissionRationale(activity, permission)

inline fun android.support.v4.app.Fragment.requestPermission(permission: AppPermission) = requestPermissions(arrayOf(permission.permissionName), permission.requestCode)

inline fun android.support.v4.app.Fragment.handlePermission(permission: AppPermission,
                                                            onGranted: (AppPermission) -> Unit,
                                                            onDenied: (AppPermission) -> Unit,
                                                            onExplanationNeeded: (AppPermission) -> Unit) {
    when {
        com.techwolf.android.permissionhandler.isLollipopOrBellow() || isPermissionGranted(permission.permissionName) -> onGranted(permission)
        isRationaleExplanationNeeded(permission.permissionName) -> onExplanationNeeded(permission)
        else -> onDenied(permission)
    }
}

inline fun android.support.v4.app.Fragment.handlePermission(permission: AppPermission,
                                                            onGranted: (AppPermission) -> Unit,
                                                            onExplanationNeeded: (AppPermission) -> Unit) {
    when {
        com.techwolf.android.permissionhandler.isLollipopOrBellow() || isPermissionGranted(permission.permissionName) -> onGranted(permission)
        isRationaleExplanationNeeded(permission.permissionName) -> onExplanationNeeded(permission)
        else -> requestPermission(permission)
    }
}

/************************************
 * HANDLE PERMISSIONS IN ACTIVITIES *
 ***********************************/

inline fun Activity.isPermissionGranted(permission: String) = checkSelfPermission(this, permission) == android.content.pm.PackageManager.PERMISSION_GRANTED

inline fun Activity.isRationaleExplanationNeeded(permission: String) = shouldShowRequestPermissionRationale(this, permission)

inline fun Activity.requestPermission(permission: AppPermission) = requestPermissions(this, arrayOf(permission.permissionName), permission.requestCode)

inline fun Activity.handlePermission(permission: AppPermission,
                                     onGranted: (AppPermission) -> Unit,
                                     onDenied: (AppPermission) -> Unit,
                                     onExplanationNeeded: (AppPermission) -> Unit) {
    when {
        com.techwolf.android.permissionhandler.isLollipopOrBellow() || isPermissionGranted(permission.permissionName) -> onGranted(permission)
        isRationaleExplanationNeeded(permission.permissionName) -> onExplanationNeeded(permission)
        else -> onDenied(permission)
    }
}

inline fun Activity.handlePermission(permission: AppPermission,
                                     onGranted: (AppPermission) -> Unit,
                                     onExplanationNeeded: (AppPermission) -> Unit) {
    when {
        com.techwolf.android.permissionhandler.isLollipopOrBellow() || isPermissionGranted(permission.permissionName) -> onGranted(permission)
        isRationaleExplanationNeeded(permission.permissionName) -> onExplanationNeeded(permission)
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
    if (grantResults == android.content.pm.PackageManager.PERMISSION_GRANTED) {
        onPermissionGranted(this)
    } else {
        onPermissionDenied(this)
    }
}