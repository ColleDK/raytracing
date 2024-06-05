package com.colledk.raytracer.shapes

import com.colledk.raytracer.Interval
import com.colledk.raytracer.ray.Ray
import com.colledk.raytracer.vector.Vector3
import com.colledk.raytracer.vector.dotProduct
import kotlin.math.pow
import kotlin.math.sqrt

data class Sphere(
    private val center: Vector3,
    private val radius: Float
) : Hittable {
    override fun hasHit(ray: Ray, tInterval: Interval): HitRecord? {
        val oc = center - ray.origin
        val a = ray.direction.lengthSquared()
        val h = dotProduct(ray.direction, oc)
        val c = oc.lengthSquared() - radius.pow(2)
        val discriminant = h.pow(2) - a * c
        if (discriminant < 0) {
            return null
        }

        val sqrtd = sqrt(discriminant)

        var root = (h - sqrtd) / a
        if (!tInterval.surrounds(root)) {
            root = (h + sqrtd) / a
            if (!tInterval.surrounds(root)) {
                return null
            }
        }

        val point = ray.pointAt(root)
        val normal = (point - center) / radius
        val isFrontFace = dotProduct(ray.direction, normal) < 0
        return HitRecord(
            point = point,
            normal = if (isFrontFace) normal else -normal,
            t = root
        )
    }
}