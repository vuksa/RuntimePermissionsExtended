@file:JvmName("PermissionHandler")

package com.techwolf.android.permissionhandler

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v13.app.FragmentCompat
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.support.v4.content.PermissionChecker

fun isLollipopOrBellow(): Boolean = (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP)

/**************************************
 * HANDLE PERMISSIONS IN FRAGMENTS *
 *************************************/

inline fun android.app.Fragment.isPermissionGranted(permission: AppPermission) = (PermissionChecker.checkSelfPermission(activity, permission.permissionName) == PackageManager.PERMISSION_GRANTED)

inline fun android.app.Fragment.isRationaleNeeded(permission: AppPermission) = FragmentCompat.shouldShowRequestPermissionRationale(this, permission.permissionName)

inline fun android.app.Fragment.requestPermission(permission: AppPermission) = FragmentCompat.requestPermissions(this, arrayOf(permission.permissionName), permission.requestCode)

inline fun android.app.Fragment.handlePermission(permission: AppPermission,
                                                 onGranted: (AppPermission) -> Unit,
                                                 onDenied: (AppPermission) -> Unit,
                                                 onRationaleNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleNeeded(permission) -> onRationaleNeeded(permission)
        else -> onDenied(permission)
    }
}

inline fun android.app.Fragment.handlePermission(permission: AppPermission,
                                                 onGranted: (AppPermission) -> Unit,
                                                 onRationaleNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleNeeded(permission) -> onRationaleNeeded(permission)
        else -> requestPermission(permission)
    }
}


/**************************************
 * HANDLE PERMISSIONS IN v4 FRAGMENTS *
 *************************************/

inline fun android.support.v4.app.Fragment.isPermissionGranted(permission: AppPermission) = (PermissionChecker.checkSelfPermission(activity, permission.permissionName) == PackageManager.PERMISSION_GRANTED)

inline fun android.support.v4.app.Fragment.isRationaleNeeded(permission: AppPermission) = shouldShowRequestPermissionRationale(activity, permission.permissionName)

inline fun android.support.v4.app.Fragment.requestPermission(permission: AppPermission) = requestPermissions(arrayOf(permission.permissionName), permission.requestCode)

inline fun android.support.v4.app.Fragment.handlePermission(permission: AppPermission,
                                                            onGranted: (AppPermission) -> Unit,
                                                            onDenied: (AppPermission) -> Unit,
                                                            onRationaleNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleNeeded(permission) -> onRationaleNeeded(permission)
        else -> onDenied(permission)
    }
}

inline fun android.support.v4.app.Fragment.handlePermission(permission: AppPermission,
                                                            onGranted: (AppPermission) -> Unit,
                                                            onRationaleNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleNeeded(permission) -> onRationaleNeeded(permission)
        else -> requestPermission(permission)
    }
}

/************************************
 * HANDLE PERMISSIONS IN ACTIVITIES *
 ***********************************/

inline fun Activity.isPermissionGranted(permission: AppPermission) = (PermissionChecker.checkSelfPermission(this, permission.permissionName) == PackageManager.PERMISSION_GRANTED)

inline fun Activity.isRationaleNeeded(permission: AppPermission) = ActivityCompat.shouldShowRequestPermissionRationale(this, permission.permissionName)

inline fun Activity.requestPermission(permission: AppPermission) = requestPermissions(this, arrayOf(permission.permissionName), permission.requestCode)

inline fun Activity.handlePermission(permission: AppPermission,
                                     onGranted: (AppPermission) -> Unit,
                                     onDenied: (AppPermission) -> Unit,
                                     onRationaleNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleNeeded(permission) -> onRationaleNeeded(permission)
        else -> onDenied(permission)
    }
}

inline fun Activity.handlePermission(permission: AppPermission,
                                     onGranted: (AppPermission) -> Unit,
                                     onRationaleNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleNeeded(permission) -> onRationaleNeeded(permission)
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
    AppPermission.permissions.find {
        it.requestCode == requestCode
    }?.let {
        val permissionGrantResult = mapPermissionsAndResults(permissions, grantResults)[it.permissionName]
        if (PackageManager.PERMISSION_GRANTED == permissionGrantResult) {
            onPermissionGranted(it)
        } else {
            onPermissionDenied(it)
        }
    }
}

private fun mapPermissionsAndResults(permissions: Array<out String>, grantResults: IntArray): Map<String, Int>
        = permissions.mapIndexedTo(mutableListOf<Pair<String, Int>>()) { index, permission -> permission to grantResults[index] }.toMap()