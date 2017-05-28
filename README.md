# RuntimePermissionsExtended 

This project provides Kotlin extension functions that make permission handling easier and more concise.
These extensions provide same implementation for permission handling in both *Activities* and *Fragments*.

## Usage

All system permissions are annotated in *[AppPermission](https://github.com/Vuksa/KotlinoidRuntimePermission/blob/master/library/src/main/kotlin/com/techwolf/android/permissionhandler/AppPermission.kt)* class.

AppPermission class preview:
```kotlin
sealed class AppPermission(val permissionName: String,
                         val requestCode: Int,
                         val deniedMessageId: Int,
                         val explanationMessageId: Int) {
    
    object CAMERA:  AppPermission(permissionName = Manifest.permission.CAMERA, 
    requestCode = 1, 
    deniedMessageId = R.string.permission_camera_denied, 
    explanationMessageId = R.string.permission_camera_explanation)
    /** 
    * other permissions
    **/
}

```

**`AppPermission`** class properties:
* _`permissionName`_ - system permission that you want to request (string value)
* _`requestCode`_ - *unique* requestCode for permission request (int value)
* _`deniedMessageId`_ - string resource id of message that should be shown to user if permission is denied
* _`explanationMessageId`_ - string resource id of message that should be shown to user if rationale 
permission explanation is needed

>Note: If you want to use certain permission, you can do that by annotating **`AppPermision.{PERMISSION NAME}`**

### Step 0: Prepare AndroidManifest

Add the following line to AndroidManifest.xml:

```xml
<uses-permission android:name="android.permission.CAMERA" />
```

### Step 1: Request permission by calling *[handlePermission](https://github.com/Vuksa/KotlinoidRuntimePermission/blob/master/library/src/main/kotlin/com/techwolf/android/permissionhandler/PermissionHandler.kt#L61)* function

In Activity or Fragment where you want to perform operation that needs system permission use 
`handlePermission` function as in example bellow:

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
                snackbarWithAction(it.explanationMessageId) {
                    requestPermission(it)
                }
            })
}
```

`handlePermission` function signature:
```kotlin
fun handlePermission(permission: AppPermission, 
                   onGranted: (AppPermission) -> Unit, 
                   onDenied: (AppPermission) -> Unit,
                   onExplanationNeeded: (AppPermission) -> Unit)
```

**`handlePermission`** function parameters:
* *`AppPermission`* - permission for which we are creating request (e.g AppPermission.CAMERA_PERMISSION)
* *`onGranted`* - lambda block that will be executed if permission is granted.
* *`onDenied`* - lambda block that will be executed if permission is not granted.
* *`onExplanationNeeded`* - lambda block that will be executed if additional explanation for permission is needed.


>Note: In every permission request callback block you can access instance of requested AppPermission via Kotlin keyword ''**it**'' (implicit name of a single parameter).

### Step 2: Handle *`onRequestPermissionResult`* callback

In Activity or Fragment where you need CAMERA permission you should override `onRequestPermissionsResult` function and delegate its parameters
to [onRequestPermissionsResultReceived](https://github.com/Vuksa/KotlinoidRuntimePermission/blob/master/library/src/main/kotlin/com/techwolf/android/permissionhandler/PermissionHandler.kt#L75) extension functions as in example bellow:

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
                    snackbarWithoutAction(it.deniedMessageId)
                }
        )
    }
```

**`onRequestPermissionsResultReceived`** function signature:
```kotlin
fun onRequestPermissionsResultReceived(requestCode: Int, permissions: Array<out String>,
                                       grantResults: IntArray,
                                       onPermissionGranted: (AppPermission) -> Unit, 
                                       onPermissionDenied: (AppPermission) -> Unit)
```

**`onRequestPermissionsResultReceived`** function parameters: 
* *`requestCode`* - the request code passed in requestPermission(Int) call.
* *`permissions`* - the requested permissions. Never null.
* *`grantResults`* - the grant results for the corresponding permissions.
* *`onPermissionGranted`* - lambda block that will be executed if permission is granted.
* *`onPermissionDenied`* - lambda block that will be executed if permission is denied.


>**Important Note:** Currently, these permission extensions are only able to handle one permission request at the time. Requesting two or more permissions in single permission request is yet to be implemented.  

## Licence
```
MIT License

Copyright (c) 2017 Nebojsa Vuksic

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```