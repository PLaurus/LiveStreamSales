package com.laurus.p.tools.camera.model.mapper

import android.hardware.Camera
import com.laurus.p.tools.camera.model.Resolution
import tv.wfc.core.entity.IEntityMapper
import javax.inject.Inject

class Camera1SizeToResolutionMapper @Inject constructor(

) : IEntityMapper<Camera.Size, Resolution> {
    override fun map(from: Camera.Size): Resolution = Resolution(from.width, from.height)
}