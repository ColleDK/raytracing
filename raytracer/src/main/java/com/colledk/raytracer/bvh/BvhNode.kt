package com.colledk.raytracer.bvh

import com.colledk.raytracer.Interval
import com.colledk.raytracer.vector.Ray

data class BvhNode(
    val boundingBox: Aabb,
    val left: BvhNode?,
    val right: BvhNode?
) {
    fun hit(ray: Ray, tInterval: Interval): Boolean {
        if (!boundingBox.hit(ray = ray, tInterval = tInterval)) {
            return false
        }

        val hitLeft = left?.hit(ray, tInterval) ?: false
        val hitRight = right?.hit(ray, tInterval) ?: false

        return hitLeft || hitRight
    }
}