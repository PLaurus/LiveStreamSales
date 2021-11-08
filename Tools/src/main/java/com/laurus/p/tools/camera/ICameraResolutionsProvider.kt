package com.laurus.p.tools.camera

import android.hardware.Camera
import com.laurus.p.tools.camera.model.AspectRatio
import com.laurus.p.tools.camera.model.Resolution

interface ICameraResolutionsProvider {
    /**
     * @return Available camera resolutions corresponding to back camera.
     */
    fun getBackCameraResolutions(): List<Resolution>

    /**
     * @param aspectRatio preferred aspect ratio.
     * @return Available back camera sizes corresponding to [aspectRatio].
     */
    fun getBackCameraResolutions(aspectRatio: AspectRatio): List<Resolution>

    /**
     * @param orientation preferred aspect ratio.
     * @return Available back camera sizes with [orientation].
     */
    fun getBackCameraResolutions(orientation: AspectRatio.Orientation): List<Resolution>

    /**
     * @return Available camera resolutions corresponding to front camera.
     */
    fun getFrontCameraResolutions(): List<Resolution>

    /**
     * @param aspectRatio preferred aspect ratio.
     * @return Available front camera sizes corresponding to [aspectRatio].
     */
    fun getFrontCameraResolutions(aspectRatio: AspectRatio): List<Resolution>

    /**
     * @param orientation preferred aspect ratio.
     * @return Available front camera sizes with [orientation].
     */
    fun getFrontCameraResolutions(orientation: AspectRatio.Orientation): List<Resolution>

    /**
     * @param facing Camera facing. May be either [Camera.CameraInfo.CAMERA_FACING_BACK] or
     * [Camera.CameraInfo.CAMERA_FACING_FRONT].
     * @return Available camera resolutions corresponding to [facing].
     */
    fun getCameraResolutionsByFacing(facing: Int): List<Resolution>

    /**
     * @param facing Camera facing. May be either [Camera.CameraInfo.CAMERA_FACING_BACK] or
     * [Camera.CameraInfo.CAMERA_FACING_FRONT].
     * @param aspectRatio preferred aspect ratio.
     * @param useOrientation Set this parameter to true to use [AspectRatio.orientation] when there
     * are no any resolution corresponding to [aspectRatio]. In this situation function will get all
     * resolutions corresponding to [aspectRatio]'s orientation.
     * @return Available resolutions which meet the [aspectRatio] of [facing] camera. If there
     * is no any resolution with [aspectRatio] and [useOrientation] is set to true returns
     * available resolutions which meet the [aspectRatio]'s orientation.
     */
    fun getCameraResolutionsByFacing(
        facing: Int,
        aspectRatio: AspectRatio,
        useOrientation: Boolean = false
    ): List<Resolution>

    /**
     * @param facing Camera facing. May be either [Camera.CameraInfo.CAMERA_FACING_BACK] or
     * [Camera.CameraInfo.CAMERA_FACING_FRONT].
     * @param orientation preferred resolution orientation. May be VERTICAL, HORIZONTAL or SQUARE
     * @return Available resolutions which meet the [orientation] of [facing] camera.
     */
    fun getCameraResolutionsByFacing(
        facing: Int,
        orientation: AspectRatio.Orientation
    ): List<Resolution>

    /**
     * @param facing Camera facing. May be either [Camera.CameraInfo.CAMERA_FACING_BACK] or
     * [Camera.CameraInfo.CAMERA_FACING_FRONT].
     * @param aspectRatio preferred aspect ratio.
     * @param useOrientation Set this parameter to true to use [AspectRatio.orientation] when there
     * are no any resolution corresponding to [aspectRatio]. In this situation function will get
     * the highest resolution corresponding to [AspectRatio.orientation].
     * This parameter is set to false by default.
     * @return The highest resolution corresponding to [aspectRatio] of [facing] camera. If there
     * is no any resolution with [aspectRatio] and [useOrientation] is set to true returns the
     * lowest resolution with the [aspectRatio]'s orientation. Returns null if there is no any
     * resolution that meet the conditions.
     */
    fun getTheHighestResolution(
        facing: Int,
        aspectRatio: AspectRatio,
        useOrientation: Boolean = false
    ): Resolution?

    /**
     * @param aspectRatio preferred aspect ratio.
     * @param useOrientation Set this parameter to true to use [AspectRatio.orientation] when there
     * are no any resolution corresponding to [aspectRatio]. In this situation function will get
     * the highest resolution corresponding to [AspectRatio.orientation].
     * This parameter is set to false by default.
     * @return The highest resolution corresponding to [aspectRatio] of front camera. If there is
     * no any resolution with [aspectRatio] and [useOrientation] is set to true returns the highest
     * resolution with the [aspectRatio]'s orientation. Returns null if there is no any resolution
     * that meet the conditions.
     */
    fun getTheHighestResolutionOfFrontCamera(
        aspectRatio: AspectRatio,
        useOrientation: Boolean = false
    ): Resolution?

    /**
     * @param aspectRatio preferred aspect ratio.
     * @param useOrientation Set this parameter to true to use [AspectRatio.orientation] when there
     * are no any resolution corresponding to [aspectRatio]. In this situation function will get
     * the highest resolution corresponding to [AspectRatio.orientation].
     * This parameter is set to false by default.
     * @return The highest resolution corresponding to [aspectRatio] of back camera. If there is
     * no any resolution with [aspectRatio] and [useOrientation] is set to true returns the highest
     * resolution with the [aspectRatio]'s orientation. Returns null if there is no any resolution
     * that meet the conditions.
     */
    fun getTheHighestResolutionOfBackCamera(
        aspectRatio: AspectRatio,
        useOrientation: Boolean = false
    ): Resolution?

    /**
     * @param facing Camera facing. May be either [Camera.CameraInfo.CAMERA_FACING_BACK] or
     * [Camera.CameraInfo.CAMERA_FACING_FRONT].
     * @param aspectRatio preferred aspect ratio.
     * @param useOrientation Set this parameter to true to use [AspectRatio.orientation] when there
     * are no any resolution corresponding to [aspectRatio]. In this situation function will get
     * the highest resolution corresponding to [AspectRatio.orientation].
     * This parameter is set to false by default.
     * @return The lowest resolution corresponding to [aspectRatio] of the camera with the requested
     * [facing]. If there is no any resolution with [aspectRatio] and [useOrientation] is set to
     * true returns the lowest resolution with the [aspectRatio]'s orientation. Returns null if
     * there is no any resolution that meet the conditions.
     */
    fun getTheLowestResolution(
        facing: Int,
        aspectRatio: AspectRatio,
        useOrientation: Boolean = false
    ): Resolution?

    /**
     * @param aspectRatio preferred aspect ratio.
     * @param useOrientation Set this parameter to true to use [AspectRatio.orientation] when there
     * are no any resolution corresponding to [aspectRatio]. In this situation function will get
     * the highest resolution corresponding to [AspectRatio.orientation].
     * This parameter is set to false by default.
     * @return The lowest resolution corresponding to [aspectRatio] of front camera. If there is
     * no any resolution with [aspectRatio] and [useOrientation] is set to true returns the lowest
     * resolution with the [aspectRatio]'s orientation. Returns null if there is no any resolution
     * that meet the conditions.
     */
    fun getTheLowestResolutionOfFrontCamera(
        aspectRatio: AspectRatio,
        useOrientation: Boolean = false
    ): Resolution?

    /**
     * @param aspectRatio preferred aspect ratio.
     * @param useOrientation Set this parameter to true to use [AspectRatio.orientation] when there
     * are no any resolution corresponding to [aspectRatio]. In this situation function will get
     * the lowest resolution corresponding to [AspectRatio.orientation].
     * This parameter is set to false by default.
     * @return The lowest resolution corresponding to [aspectRatio] of back camera. If there is
     * no any resolution with [aspectRatio] and [useOrientation] is set to true returns the lowest
     * resolution with the [aspectRatio]'s orientation. Returns null if there is no any resolution
     * that meet the conditions.
     */
    fun getTheLowestResolutionOfBackCamera(
        aspectRatio: AspectRatio,
        useOrientation: Boolean = false
    ): Resolution?

    /**
     * @param facing Camera facing. May be either [Camera.CameraInfo.CAMERA_FACING_BACK] or
     * [Camera.CameraInfo.CAMERA_FACING_FRONT].
     * @param aspectRatio preferred aspect ratio.
     * @param useOrientation Set this parameter to true to use [AspectRatio.orientation] when there
     * are no any resolution corresponding to [aspectRatio]. In this situation function will get
     * the lowest resolution corresponding to [AspectRatio.orientation].
     * This parameter is set to false by default.
     * @return Average resolution corresponding to [aspectRatio] of the camera with the requested
     * [facing]. If there is no any resolution with [aspectRatio] and [useOrientation] is set to
     * true returns average resolution with the [aspectRatio]'s orientation. Returns null if
     * there is no any resolution that meet the conditions.
     */
    fun getAverageResolution(
        facing: Int,
        aspectRatio: AspectRatio,
        useOrientation: Boolean = false
    ): Resolution?

    /**
     * @param aspectRatio preferred aspect ratio.
     * @param useOrientation Set this parameter to true to use [AspectRatio.orientation] when there
     * are no any resolution corresponding to [aspectRatio]. In this situation function will get
     * the lowest resolution corresponding to [AspectRatio.orientation].
     * This parameter is set to false by default.
     * @return Average resolution corresponding to [aspectRatio] of front camera. If there is
     * no any resolution with [aspectRatio] and [useOrientation] is set to true returns average
     * resolution with the [aspectRatio]'s orientation. Returns null if there is no any resolution
     * that meet the conditions.
     */
    fun getAverageResolutionOfFrontCamera(
        aspectRatio: AspectRatio,
        useOrientation: Boolean = false
    ): Resolution?

    /**
     * @param aspectRatio preferred aspect ratio.
     * @return Average resolution corresponding to [aspectRatio] of back camera. If there is
     * no any resolution with [aspectRatio] and [useOrientation] is set to true returns average
     * resolution with the [aspectRatio]'s orientation. Returns null if there is no any resolution
     * that meet the conditions.
     */
    fun getAverageResolutionOfBackCamera(
        aspectRatio: AspectRatio,
        useOrientation: Boolean = false
    ): Resolution?
}