package com.colledk.raytracer.vector

/**
 * Data representation of a ray in 3D space.
 */
data class Ray(
    val origin: Vector3,
    val direction: Vector3
) {
    /**
     * Function to get a point of a ray after t steps.
     */
    fun pointAt(t: Float): Vector3 {
        return origin + direction * t
    }
}