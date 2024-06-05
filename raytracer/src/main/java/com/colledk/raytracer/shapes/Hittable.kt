package com.colledk.raytracer.shapes

import com.colledk.raytracer.Hit
import com.colledk.raytracer.Interval
import com.colledk.raytracer.vector.Ray

interface Hittable {
    fun hasHit(ray: Ray, tInterval: Interval): Hit?
}