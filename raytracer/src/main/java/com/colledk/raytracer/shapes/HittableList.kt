package com.colledk.raytracer.shapes

import com.colledk.raytracer.Hit
import com.colledk.raytracer.Interval
import com.colledk.raytracer.vector.Ray

class HittableList(private val objects: List<Hittable>): Hittable {
    private val currentObjects: MutableList<Hittable> = mutableListOf(*objects.toTypedArray())

    fun addObject(obj: Hittable) {
        currentObjects.add(obj)
    }

    override fun hasHit(ray: Ray, tInterval: Interval): Hit? {
        return currentObjects.fold(
            initial = null
        ) { current: Hit?, obj: Hittable ->
            obj.hasHit(
                ray = ray,
                tInterval = if (current?.t != null) tInterval.copy(max = current.t) else tInterval
            ) ?: current
        }
    }
}