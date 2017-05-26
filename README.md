# KotlinoidRuntimePermission
Kotlin extension functions that make permission handling concise and little bit easier.
>This extensions provide same implementation for permission handling in both *Activities* and *Fragments*.

#### Where to store all permissions required by app?

All permissions that you need to use in app should be stored in *AppPermission* class.

Example:
```kotlin
enum class AppPermission(val permissionName: String,
                         val requestCode: Int,
                         val deniedMsgId: Int,
                         val explanationMsgId: Int) {
    
    CAMERA_PERMISSION(permissionName = Manifest.permission.CAMERA, 
    requestCode = 1, 
    deniedMsgId = R.string.permission_camera_denied, 
    explanationMsgId = R.string.permission_camera_explanation);
    /** Other permissions **/
}

```
* permissionName - system permission that you want to request (string value)
* requestCode - *unique* requestCode for permission request (int value)
* deniedMsgId - string resource id of message that should be shown to user if permission is denied
* explanationMsgId - string resource id of message that should be shown to user if rationale 
permission explanation is needed


#### HOW TO REQUEST PERMISSION

In place where you need to perform operation that needs system permission execute next code block:
Example - requesting camera permission:
```kotlin
fun captureCameraImage(){
        handlePermission(AppPermission.CAMERA_PERMISSION,
            onGranted = {
                 /** Permission is granted and we can use camera* */
                startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), it.requestCode)
            },
            onDenied = {
                /** Permission is not granted - we should request permission **/      
                requestPermission(it)
            },
            onExplanationNeeded = {
                /** Additional explanation for permission usage needed **/
                snackbarWithAction(it.explanationMsgId) {
                    requestPermission(it)
                }
            })
}
```

> handlePermission function signature:
```kotlin
fun handlePermission(permission: AppPermission, 
                   onGranted: () -> Unit, 
                   onDenied: () -> Unit,
                   onExplanationNeeded: () -> Unit)
```

> handlePermission function parameters:
* AppPermission - permission for which we are creating request (AppPermission.CAMERA_PERMISSION)
* onGranted - lamda expression that will be executed if permission is granted
* onDenied - lamda expression that will be executed if permission is not granted
  * *we should request permission here*
* onExplanationNeeded - lamda expression that will be executed if additional explanation for permission is needed
  * *we should show to user dialog with explanation, if user agrees we should request permission*


#### HOW TO HANDLE onPermissionRequestResult callback

##TBD


> Note: Currently these extensions are only to handle one permission request at the time. 