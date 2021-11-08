package com.laurus.p.tools.camera.model

/**
 * Ratio of an image's width to its height expressed with two numbers separated by a colon,
 * such as 16:9, sixteen-to-nine. For the [width]:[height] aspect ratio, the image is [width] units
 * wide and [height] units high
 */
data class AspectRatio(
    /**
     * Example: if aspect ratio is 16:9 [width] will be 16 units.
     */
    val width: Int,
    /**
     * Example: if aspect ratio is 16:9 [height] will be 9 units.
     */
    val height: Int
) {
    /**
     * Float value of aspect ratio. For 16:9 it will be 1.7(7)
     */
    val value: Float
        get() = width.toFloat() / height

    val orientation
        get() = when {
            value > 1f -> Orientation.HORIZONTAL
            value == 1f -> Orientation.SQUARE
            else -> Orientation.VERTICAL
        }

    enum class Orientation {
        VERTICAL,
        HORIZONTAL,
        SQUARE
    }
}