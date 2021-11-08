package com.laurus.p.tools.camera.model

data class Resolution(
    val width: Int,
    val height: Int
) {
    /**
     * Creates new [Resolution] with swapped [width] and [height].
     * @return new [Resolution] with swapped [width] and [height].
     */
    fun rotate() = Resolution(
        height,
        width
    )
}