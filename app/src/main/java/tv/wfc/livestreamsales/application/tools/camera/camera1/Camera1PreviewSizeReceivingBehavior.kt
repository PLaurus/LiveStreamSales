package tv.wfc.livestreamsales.application.tools.camera.camera1

import android.hardware.Camera
import android.media.CamcorderProfile
import tv.wfc.livestreamsales.application.tools.camera.ICameraPreviewSizeReceivingBehavior
import tv.wfc.livestreamsales.application.tools.camera.model.CameraSize
import tv.wfc.livestreamsales.application.tools.camera.model.converter.toCameraSize
import javax.inject.Inject

class Camera1PreviewSizeReceivingBehavior @Inject constructor(

): ICameraPreviewSizeReceivingBehavior {
    override fun getBackCameraPreviewSizes(): List<CameraSize> {
        return getCameraPreviewSizesByFacing(Camera.CameraInfo.CAMERA_FACING_BACK)
    }

    override fun getFrontCameraPreviewSizes(): List<CameraSize> {
        return getCameraPreviewSizesByFacing(Camera.CameraInfo.CAMERA_FACING_FRONT)
    }

    override fun getCameraPreviewSizesByFacing(facing: Int): List<CameraSize> {
        val maxEncoderSizeSupported = getMaxEncoderSizeSupported()
        val cameraId = getCameraIdByFacing(facing)
        var camera: Camera? = Camera.open(cameraId)
        val supportedCameraPreviewSizes = (camera?.parameters?.supportedPreviewSizes ?: emptyList())
            .map { it.toCameraSize() }
            .toMutableList()

        camera?.release()

        val previewsIterator = supportedCameraPreviewSizes.iterator()
        while(previewsIterator.hasNext()){
            val cameraSize = previewsIterator.next()
            if(cameraSize.width > maxEncoderSizeSupported.width ||
                    cameraSize.height > maxEncoderSizeSupported.height){
                previewsIterator.remove()
            }
        }

        return supportedCameraPreviewSizes
    }

    private fun getCameraIdByFacing(facing: Int): Int {
        val numberOfCameras = Camera.getNumberOfCameras()
        for(cameraId in 0..numberOfCameras){
            val cameraInfo = Camera.CameraInfo()
            Camera.getCameraInfo(cameraId, cameraInfo)
            if(cameraInfo.facing == facing) return cameraId
        }
        return 0
    }

    private fun getMaxEncoderSizeSupported(): CameraSize {
        return when{
            CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_2160P) -> CameraSize(3840, 2160)
            CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_1080P) -> CameraSize(1920, 1080)
            CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P) -> CameraSize(1280, 720)
            else -> CameraSize(640, 480)
        }
    }
}