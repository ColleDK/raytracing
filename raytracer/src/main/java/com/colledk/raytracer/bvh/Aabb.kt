package com.colledk.raytracer.bvh

import com.colledk.raytracer.Interval
import com.colledk.raytracer.vector.Ray
import com.colledk.raytracer.vector.Vector3
import kotlin.math.max
import kotlin.math.min

/**
 * Class to represent an axis aligned bounding box
 */
data class Aabb(
    val xInterval: Interval,
    val yInterval: Interval,
    val zInterval: Interval
) {
    // Convenience constructor to build a bounding box based on 2 points in 3D space.
    constructor(vec1: Vector3, vec2: Vector3) : this(
        xInterval = Interval(min(vec1.x, vec2.x), max(vec1.x, vec2.x)),
        yInterval = Interval(min(vec1.y, vec2.y), max(vec1.y, vec2.y)),
        zInterval = Interval(min(vec1.z, vec2.z), max(vec1.z, vec2.z))
    )

    operator fun get(i: Int): Interval {
        return when(i) {
            0 -> xInterval
            1 -> yInterval
            else -> zInterval
        }
    }

    /**
     * Function to check if a bounding box has been hit by a ray.
     */
    fun hit(ray: Ray, tInterval: Interval): Boolean {
        val origin = ray.origin
        val dir = ray.direction

        var min = tInterval.min
        var max = tInterval.max

        for (i in 0 until  3) {
            val axis = this[i]
            val inverse = 1f / dir[i]
            val t0 = (axis.min - origin[i]) * inverse
            val t1 = (axis.max - origin[i]) * inverse

            if (t0 < t1) {
                if (t0 > min) min = t0
                if (t1 < max) max = t1
            } else {
                if (t1 > min) min = t1
                if (t0 < max) max = t0
            }
            if (max <= min) return false
        }

        return true
    }
}