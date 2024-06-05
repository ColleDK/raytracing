package com.colledk.raytracer.materials

import com.colledk.raytracer.Hit
import com.colledk.raytracer.vector.Ray
import com.colledk.raytracer.vector.Vector3
import com.colledk.raytracer.vector.dotProduct
import com.colledk.raytracer.vector.randomUnitVector
import com.colledk.raytracer.vector.reflect

data class Metal(
    private val albedo: Vector3,
    private val fuzz: Float
) : Material {
    override fun scatter(ray: Ray, record: Hit): MaterialScatter? {
        var reflected = reflect(ray.direction, record.normal)
        reflected = reflected.asUnitVector() + (randomUnitVector() * fuzz)
        val scattered = Ray(record.point, reflected)
        return if (dotProduct(scattered.direction, record.normal) > 0) {
            MaterialScatter(
                attenuation = albedo,
                scattered = scattered
            )
        } else {
            null
        }
    }
}
