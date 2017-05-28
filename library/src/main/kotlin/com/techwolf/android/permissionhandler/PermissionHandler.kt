@file:JvmName("PermissionHandler")

package com.techwolf.android.permissionhandler

typealias CodeBlock = (AppPermission) -> Unit

fun isLollipopOrBellow(): Boolean = android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP

/***********************************
 * HANDLE PERMISSIONS IN FRAGMENTS *
 **********************************/

fun android.support.v4.app.Fragment.isPermissionGranted(permission: AppPermission) = android.support.v4.content.ContextCompat.checkSelfPermission(activity, permission.permissionName) == android.content.pm.PackageManager.PERMISSION_GRANTED

fun android.support.v4.app.Fragment.isRationaleExplanationNeeded(permission: AppPermission) = android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.permissionName)

fun android.support.v4.app.Fragment.requestPermission(permission: AppPermission) = requestPermissions(arrayOf(permission.permissionName), permission.requestCode)

inline fun android.support.v4.app.Fragment.handlePermission(permission: AppPermission,
                                                            onGranted: com.techwolf.android.permissionhandler.CodeBlock,
                                                            onDenied: com.techwolf.android.permissionhandler.CodeBlock,
                                                            onExplanationNeeded: com.techwolf.android.permissionhandler.CodeBlock) {
    when {
        com.techwolf.android.permissionhandler.isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleExplanationNeeded(permission) -> onExplanationNeeded(permission)
        else -> onDenied(permission)
    }
}

inline fun android.support.v4.app.Fragment.handlePermission(permission: AppPermission,
                                                            onGranted: com.techwolf.android.permissionhandler.CodeBlock,
                                                            onExplanationNeeded: com.techwolf.android.permissionhandler.CodeBlock) {
    when {
        com.techwolf.android.permissionhandler.isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleExplanationNeeded(permission) -> onExplanationNeeded(permission)
        else -> requestPermission(permission)
    }
}

/************************************
 * HANDLE PERMISSIONS IN ACTIVITIES *
 ***********************************/

fun android.app.Activity.isPermissionGranted(permission: AppPermission) = android.support.v4.content.ContextCompat.checkSelfPermission(this, permission.permissionName) == android.content.pm.PackageManager.PERMISSION_GRANTED

fun android.app.Activity.isRationaleExplanationNeeded(permission: AppPermission) = android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale(this, permission.permissionName)

fun android.app.Activity.requestPermission(permission: AppPermission) = android.support.v4.app.ActivityCompat.requestPermissions(this, arrayOf(permission.permissionName), permission.requestCode)

inline fun android.app.Activity.handlePermission(permission: AppPermission,
                                                 onGranted: com.techwolf.android.permissionhandler.CodeBlock,
                                                 onDenied: com.techwolf.android.permissionhandler.CodeBlock,
                                                 onExplanationNeeded: com.techwolf.android.permissionhandler.CodeBlock) {
    when {
        com.techwolf.android.permissionhandler.isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleExplanationNeeded(permission) -> onExplanationNeeded(permission)
        else -> onDenied(permission)
    }
}

inline fun android.app.Activity.handlePermission(permission: AppPermission,
                                                 onGranted: com.techwolf.android.permissionhandler.CodeBlock,
                                                 onExplanationNeeded: com.techwolf.android.permissionhandler.CodeBlock) {
    when {
        com.techwolf.android.permissionhandler.isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleExplanationNeeded(permission) -> onExplanationNeeded(permission)
        else -> requestPermission(permission)
    }
}

/*********************************************
 * HANDLE onRequestPermissionResult CALLBACK *
 ********************************************/

fun onRequestPermissionsResultReceived(requestCode: Int, permissions: Array<out String>,
                                       grantResults: IntArray,
                                       onPermissionGranted: com.techwolf.android.permissionhandler.CodeBlock, onPermissionDenied: com.techwolf.android.permissionhandler.CodeBlock) {
    AppPermission.permissions.find { it.requestCode == requestCode }?.apply {
        analisePermissionResult(grantResults[0], onPermissionGranted, onPermissionDenied)
    }
}

private fun AppPermission.analisePermissionResult(grantResults: Int, onPermissionGranted: com.techwolf.android.permissionhandler.CodeBlock, onPermissionDenied: com.techwolf.android.permissionhandler.CodeBlock) {
    if (grantResults == android.content.pm.PackageManager.PERMISSION_GRANTED) {
        onPermissionGranted(this)
    } else {
        onPermissionDenied(this)
    }
}