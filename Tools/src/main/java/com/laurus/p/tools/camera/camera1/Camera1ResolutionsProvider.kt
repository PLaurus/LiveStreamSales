package com.laurus.p.tools.camera.camera1

import android.hardware.Camera
import android.media.CamcorderProfile
import com.laurus.p.tools.camera.ICameraResolutionsProvider
import com.laurus.p.tools.camera.model.AspectRatio
import com.laurus.p.tools.camera.model.Resolution
import tv.wfc.core.entity.IEntityMapper
import javax.inject.Inject

class Camera1ResolutionsProvider @Inject constructor(
    private val camera1SizeToResolutionMapper: IEntityMapper<Camera.Size, Resolution>
) : ICameraResolutionsProvider {
    override fun getBackCameraResolutions(): List<Resolution> {
        return getCameraResolutionsByFacing(Camera.CameraInfo.CAMERA_FACING_BACK)
    }

    override fun getBackCameraResolutions(aspectRatio: AspectRatio): List<Resolution> {
        return getCameraResolutionsByFacing(Camera.CameraInfo.CAMERA_FACING_BACK, aspectRatio)
    }

    override fun getBackCameraResolutions(orientation: AspectRatio.Orientation): List<Resolution> {
        return getCameraResolutionsByFacing(Camera.CameraInfo.CAMERA_FACING_BACK, orientation)
    }

    override fun getFrontCameraResolutions(): List<Resolution> {
        return getCameraResolutionsByFacing(Camera.CameraInfo.CAMERA_FACING_FRONT)
    }

    override fun getFrontCameraResolutions(aspectRatio: AspectRatio): List<Resolution> {
        return getCameraResolutionsByFacing(Camera.CameraInfo.CAMERA_FACING_BACK, aspectRatio)
    }

    override fun getFrontCameraResolutions(orientation: AspectRatio.Orientation): List<Resolution> {
        return getCameraResolutionsByFacing(Camera.CameraInfo.CAMERA_FACING_FRONT, orientation)
    }

    override fun getCameraResolutionsByFacing(facing: Int): List<Resolution> {
        val maxEncoderSizeSupported = getMaxEncoderSizeSupported()
        val cameraId = getCameraIdByFacing(facing)
        val camera: Camera? = Camera.open(cameraId)
        val supportedCameraPreviewSizes = (camera?.parameters?.supportedPreviewSizes ?: emptyList())
            .mapNotNull(camera1SizeToResolutionMapper::map)
            .toMutableList()

        camera?.release()

        val previewsIterator = supportedCameraPreviewSizes.iterator()
        while (previewsIterator.hasNext()) {
            val cameraSize = previewsIterator.next()
            if (cameraSize.width > maxEncoderSizeSupported.width ||
                cameraSize.height > maxEncoderSizeSupported.height
            ) {
                previewsIterator.remove()
            }
        }

        return supportedCameraPreviewSizes
    }

    override fun getCameraResolutionsByFacing(
        facing: Int,
        aspectRatio: AspectRatio,
        useOrientation: Boolean
    ): List<Resolution> {
        val allResolutions = getCameraResolutionsByFacing(facing)

        val resolutionsMetAspectRatio = allResolutions
            .filter { it.width * aspectRatio.height == it.height * aspectRatio.width }

        return if (resolutionsMetAspectRatio.isNotEmpty() || !useOrientation) {
            resolutionsMetAspectRatio
        } else {
            val resolutionsMetOrientation =
                getCameraResolutionsByFacing(facing, aspectRatio.orientation)
            resolutionsMetOrientation
        }
    }

    override fun getCameraResolutionsByFacing(
        facing: Int,
        orientation: AspectRatio.Orientation
    ): List<Resolution> {
        val allResolutions = getCameraResolutionsByFacing(facing)

        return allResolutions
            .filter { AspectRatio(it.width, it.height).orientation == orientation }
    }

    override fun getTheHighestResolution(
        facing: Int,
        aspectRatio: AspectRatio,
        useOrientation: Boolean
    ): Resolution? {
        val resolutions = getCameraResolutionsByFacing(facing, aspectRatio, useOrientation)

        return resolutions
            .maxByOrNull { it.width * it.height }
    }

    override fun getTheHighestResolutionOfFrontCamera(
        aspectRatio: AspectRatio,
        useOrientation: Boolean
    ): Resolution? = getTheHighestResolution(
        facing = Camera.CameraInfo.CAMERA_FACING_FRONT,
        aspectRatio,
        useOrientation
    )

    override fun getTheHighestResolutionOfBackCamera(
        aspectRatio: AspectRatio,
        useOrientation: Boolean
    ): Resolution? = getTheHighestResolution(
        facing = Camera.CameraInfo.CAMERA_FACING_BACK,
        aspectRatio,
        useOrientation
    )

    override fun getTheLowestResolution(
        facing: Int,
        aspectRatio: AspectRatio,
        useOrientation: Boolean
    ): Resolution? {
        val resolutions = getCameraResolutionsByFacing(facing, aspectRatio, useOrientation)

        return resolutions
            .minByOrNull { it.width * it.height }
    }

    override fun getTheLowestResolutionOfFrontCamera(
        aspectRatio: AspectRatio,
        useOrientation: Boolean
    ): Resolution? = getTheLowestResolution(
        facing = Camera.CameraInfo.CAMERA_FACING_FRONT,
        aspectRatio,
        useOrientation
    )

    override fun getTheLowestResolutionOfBackCamera(
        aspectRatio: AspectRatio,
        useOrientation: Boolean
    ): Resolution? = getTheLowestResolution(
        facing = Camera.CameraInfo.CAMERA_FACING_BACK,
        aspectRatio,
        useOrientation
    )

    override fun getAverageResolution(
        facing: Int,
        aspectRatio: AspectRatio,
        useOrientation: Boolean
    ): Resolution? {
        val resolutions = getCameraResolutionsByFacing(facing, aspectRatio, useOrientation)

        val sortedResolutions = resolutions
            .sortedBy { it.width * it.height }

        val resolutionsCount = sortedResolutions.size

        val averageResolutionPosition: Int? = when {
            resolutionsCount == 0 -> null
            resolutionsCount % 2 == 0 -> sortedResolutions.size / 2
            else -> (sortedResolutions.size / 2) + 1
        }

        return averageResolutionPosition?.let(sortedResolutions::getOrNull)
    }

    override fun getAverageResolutionOfFrontCamera(
        aspectRatio: AspectRatio,
        useOrientation: Boolean
    ): Resolution? = getAverageResolution(
        facing = Camera.CameraInfo.CAMERA_FACING_FRONT,
        aspectRatio,
        useOrientation
    )

    override fun getAverageResolutionOfBackCamera(
        aspectRatio: AspectRatio,
        useOrientation: Boolean
    ): Resolution? = getAverageResolution(
        facing = Camera.CameraInfo.CAMERA_FACING_BACK,
        aspectRatio,
        useOrientation
    )

    private fun getCameraIdByFacing(facing: Int): Int {
        val numberOfCameras = Camera.getNumberOfCameras()

        for (cameraId in 0..numberOfCameras) {
            val cameraInfo = Camera.CameraInfo()
            Camera.getCameraInfo(cameraId, cameraInfo)
            if (cameraInfo.facing == facing) return cameraId
        }

        return 0
    }

    private fun getMaxEncoderSizeSupported(): Resolution {
        return when {
            CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_2160P) -> Resolution(3840, 2160)
            CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_1080P) -> Resolution(1920, 1080)
            CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P) -> Resolution(1280, 720)
            else -> Resolution(640, 480)
        }
    }
}