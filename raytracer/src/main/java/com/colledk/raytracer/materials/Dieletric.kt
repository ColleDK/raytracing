package com.colledk.raytracer.materials

import com.colledk.raytracer.Hit
import com.colledk.raytracer.randomFloat
import com.colledk.raytracer.vector.Ray
import com.colledk.raytracer.vector.Vector3
import com.colledk.raytracer.vector.dotProduct
import com.colledk.raytracer.vector.reflect
import com.colledk.raytracer.vector.refract
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

data class Dieletric(
    private val refractionIndex: Float
) : Material {
    override fun scatter(ray: Ray, record: Hit): MaterialScatter? {
        val ri = if (record.isFrontFace) (1f / refractionIndex) else refractionIndex
        val unitDirection = ray.direction.asUnitVector()

        val cosTheta = min(dotProduct(-unitDirection, record.normal), 1f)
        val sinTheta = sqrt(1f - cosTheta.pow(2))
        val cannotRefract = ri * sinTheta > 1f
        val refracted = if (cannotRefract || reflectance(cosTheta, ri) > randomFloat()) {
            reflect(vec = unitDirection, normal = record.normal)
        } else {
            refract(vec = unitDirection, normal = record.normal, eta = ri)
        }
        return MaterialScatter(
            attenuation = Vector3(1f, 1f, 1f),
            scattered = Ray(record.point, refracted)
        )
    }

    // Schlicks approximation for reflectance
    private fun reflectance(cos: Float, refractionIndex: Float) : Float {
        val r = (1 - refractionIndex) / (1 + refractionIndex).pow(2)
        return r + (1 - r) * (1 - cos).pow(5)
    }
}
