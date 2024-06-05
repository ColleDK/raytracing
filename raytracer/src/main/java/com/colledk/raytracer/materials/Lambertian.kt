package com.colledk.raytracer.materials

import com.colledk.raytracer.Hit
import com.colledk.raytracer.vector.Ray
import com.colledk.raytracer.vector.Vector3
import com.colledk.raytracer.vector.randomUnitVector

data class Lambertian(
    private val albedo: Vector3
) : Material {
    override fun scatter(ray: Ray, record: Hit): MaterialScatter? {
        var scatterDirection = record.normal + randomUnitVector()
        if (scatterDirection.nearZero()) {
            scatterDirection = record.normal
        }
        return MaterialScatter(
            attenuation = albedo,
            scattered = Ray(record.point, scatterDirection)
        )
    }

}
