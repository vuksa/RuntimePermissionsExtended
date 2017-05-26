@file:JvmName("PermissionHandler")

package com.techwolf.android.sample.permissions

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat

typealias CodeBlock = (AppPermission) -> Unit

fun isLollipopOrBellow(): Boolean = Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP

/***********************************
 * HANDLE PERMISSIONS IN FRAGMENTS *
 **********************************/

inline fun Fragment.isPermissionGranted(permission: String) = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

inline fun Fragment.isRationaleExplanationNeeded(permission: String) = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

inline fun Fragment.requestPermission(permission: AppPermission) = requestPermissions(arrayOf(permission.permissionName), permission.requestCode)

inline fun Fragment.handlePermission(permission: AppPermission,
                                     onGranted: CodeBlock,
                                     onDenied: CodeBlock,
                                     onExplanationNeeded: CodeBlock) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission.permissionName) -> onGranted(permission)
        isRationaleExplanationNeeded(permission.permissionName) -> onExplanationNeeded(permission)
        else -> onDenied(permission)
    }
}

inline fun Fragment.handlePermission(permission: AppPermission,
                                     onGranted: CodeBlock,
                                     onExplanationNeeded: CodeBlock) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission.permissionName) -> onGranted(permission)
        isRationaleExplanationNeeded(permission.permissionName) -> onExplanationNeeded(permission)
        else -> requestPermission(permission)
    }
}

/************************************
 * HANDLE PERMISSIONS IN ACTIVITIES *
 ***********************************/

inline fun Activity.isPermissionGranted(permission: String) = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

inline fun Activity.isRationaleExplanationNeeded(permission: String) = ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

inline fun Activity.requestPermission(permission: AppPermission) = ActivityCompat.requestPermissions(this, arrayOf(permission.permissionName), permission.requestCode)

inline fun Activity.handlePermission(permission: AppPermission,
                                     onGranted: CodeBlock,
                                     onDenied: CodeBlock,
                                     onExplanationNeeded: CodeBlock) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission.permissionName) -> onGranted(permission)
        isRationaleExplanationNeeded(permission.permissionName) -> onExplanationNeeded(permission)
        else -> onDenied(permission)
    }
}

inline fun Activity.handlePermission(permission: AppPermission,
                                     onGranted: CodeBlock,
                                     onExplanationNeeded: CodeBlock) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission.permissionName) -> onGranted(permission)
        isRationaleExplanationNeeded(permission.permissionName) -> onExplanationNeeded(permission)
        else -> requestPermission(permission)
    }
}

/*********************************************
 * HANDLE onRequestPermissionResult CALLBACK *
 ********************************************/

fun onRequestPermissionsResultReceived(requestCode: Int, permissions: Array<out String>,
                                       grantResults: IntArray,
                                       onPermissionGranted: CodeBlock, onPermissionDenied: CodeBlock) {
    AppPermission.values().filter {
        it.requestCode == requestCode
    }.first().apply {
        analisePermissionResult(grantResults[0], onPermissionGranted, onPermissionDenied)
    }
}

private fun AppPermission.analisePermissionResult(grantResults: Int, onPermissionGranted: CodeBlock, onPermissionDenied: CodeBlock) {
    if (grantResults == PackageManager.PERMISSION_GRANTED) {
        onPermissionGranted(this)
    } else {
        onPermissionDenied(this)
    }
}