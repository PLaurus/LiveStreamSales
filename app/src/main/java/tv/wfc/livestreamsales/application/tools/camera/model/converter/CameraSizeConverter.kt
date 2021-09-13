package tv.wfc.livestreamsales.application.tools.camera.model.converter

import android.hardware.Camera
import tv.wfc.livestreamsales.application.tools.camera.model.CameraSize

fun Camera.Size.toCameraSize(): CameraSize {
    return CameraSize(width, height)
}