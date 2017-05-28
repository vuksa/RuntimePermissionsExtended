# KotlinoidRuntimePermission
Kotlin extension functions that make permission handling concise and little bit easier.
This extensions provide same implementation for permission handling in both *Activities* and *Fragments*.

##Usage

All permissions that you need to use in app should be annotated *AppPermission* class as in example bellow:

AppPermission class preview:
```kotlin
enum class AppPermission(val permissionName: String,
                         val requestCode: Int,
                         val deniedMsgId: Int,
                         val explanationMsgId: Int) {
    
    CAMERA_PERMISSION(permissionName = Manifest.permission.CAMERA, 
    requestCode = 1, 
    deniedMsgId = R.string.permission_camera_denied, 
    explanationMsgId = R.string.permission_camera_explanation);
    /** other permissions **/
}

```
>AppPermission class properties:
* permissionName - system permission that you want to request (string value)
* requestCode - *unique* requestCode for permission request (int value)
* deniedMsgId - string resource id of message that should be shown to user if permission is denied
* explanationMsgId - string resource id of message that should be shown to user if rationale 
permission explanation is needed

#### Step 0: Prepare AndroidManifest

Add the following line to AndroidManifest.xml:

```xml
<uses-permission android:name="android.permission.CAMERA" />
```

#### Step 1: Request permission using `handlePermission` function

In Activity or Fragment where you want to perform operation that needs system permission use 
`handlePermission` function as in code block example bellow:

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
                   onGranted: (AppPermission) -> Unit, 
                   onDenied: (AppPermission) -> Unit,
                   onExplanationNeeded: (AppPermission) -> Unit)
```

Note: In every permission request callback block you can access instance of requested AppPermission with using Kotlin keyword ''**it**'' (implicit name of a single parameter).

> handlePermission function parameters:
* AppPermission - permission for which we are creating request (AppPermission.CAMERA_PERMISSION)
* onGranted - lamda expression that will be executed if permission is granted
* onDenied - lamda expression that will be executed if permission is not granted
* onExplanationNeeded - lamda expression that will be executed if additional explanation for permission is needed


#### Step 2: Handle onPermissionRequestResult callback

In Activity or Fragment where you need CAMERA permission you should override `onRequestPermissionsResult` function and delegate its parameters
to `onRequestPermissionsResultReceived` extension functions as in example bellow:

```kotlin
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResultReceived(requestCode, permissions, grantResults,
                onPermissionGranted = {
                    /** call camera intent **/
                    startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), it.requestCode)
                },
                onPermissionDenied = {
                    /** show message that permission is denied**/
                    snackbarWithoutAction(it.deniedMsgId)
                }
        )
    }
```

>onRequestPermissionsResultReceived function signature:
```kotlin
fun onRequestPermissionsResultReceived(requestCode: Int, permissions: Array<out String>,
                                       grantResults: IntArray,
                                       onPermissionGranted: (AppPermission) -> Unit, 
                                       onPermissionDenied: (AppPermission) -> Unit)
```

>onRequestPermissionsResultReceived function parameters: 
* requestCode - the request code passed in requestPermission(Int) call.
* permissions - the requested permissions. Never null.
* grantResults - the grant results for the corresponding permissions.
* onPermissionGranted - lamda block that is executed if permission is granted
* onPermissionGranted - lamda block that is executed if permission is denied



>**Important Note:** Currently these permission extensions are only able to handle one permission request at the time. Requesting of two permission in one call is yet to be implemented.  