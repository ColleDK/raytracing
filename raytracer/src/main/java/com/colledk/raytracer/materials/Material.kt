package com.colledk.raytracer.materials

import com.colledk.raytracer.Hit
import com.colledk.raytracer.vector.Ray
import com.colledk.raytracer.vector.Vector3

interface Material {
    fun scatter(ray: Ray, record: Hit): MaterialScatter?
}

data class MaterialScatter(
    val attenuation: Vector3,
    val scattered: Ray
)