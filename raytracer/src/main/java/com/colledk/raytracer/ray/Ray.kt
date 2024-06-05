package com.colledk.raytracer.ray

import com.colledk.raytracer.vector.Vector3

data class Ray(
    val origin: Vector3,
    val direction: Vector3
) {
    fun pointAt(t: Float) : Vector3 {
        return origin + direction * t
    }

}
