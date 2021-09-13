package tv.wfc.livestreamsales.application.tools.camera

import android.hardware.Camera
import tv.wfc.livestreamsales.application.tools.camera.model.CameraSize

interface ICameraPreviewSizeReceivingBehavior {
    /**
     * @return Camera size for back camera.
     */
    fun getBackCameraPreviewSizes(): List<CameraSize>

    /**
     * @return Camera size for front camera.
     */
    fun getFrontCameraPreviewSizes(): List<CameraSize>

    /**
     * Camera facing can be either [Camera.CameraInfo.CAMERA_FACING_BACK] or
     * [Camera.CameraInfo.CAMERA_FACING_FRONT].
     * @return Camera size for corresponding [facing].
     */
    fun getCameraPreviewSizesByFacing(facing: Int): List<CameraSize>
}