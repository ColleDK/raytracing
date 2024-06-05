package com.colledk.raytracer.shapes

import com.colledk.raytracer.Interval
import com.colledk.raytracer.ray.Ray
import com.colledk.raytracer.vector.Vector3

interface Hittable {
    fun hasHit(ray: Ray, tInterval: Interval): HitRecord?
}

data class HitRecord(
    val point: Vector3,
    val normal: Vector3,
    val t: Float
)