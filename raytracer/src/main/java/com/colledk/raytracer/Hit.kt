package com.colledk.raytracer

import com.colledk.raytracer.materials.Material
import com.colledk.raytracer.vector.Ray
import com.colledk.raytracer.vector.Vector3
import com.colledk.raytracer.vector.dotProduct

data class Hit(
    val point: Vector3,
    val normal: Vector3,
    val material: Material,
    val t: Float,
    val isFrontFace: Boolean
) {
//    fun setNormal(ray: Ray, outwardNormal: Vector3) {
//        isFrontFace = dotProduct(ray.direction, outwardNormal) < 0
//        normal = if (isFrontFace) outwardNormal else -outwardNormal
//    }
}
