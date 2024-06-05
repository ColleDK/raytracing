package com.colledk.raytracer.shapes

import com.colledk.raytracer.Interval
import com.colledk.raytracer.ray.Ray

data class HittableList(
    private val objects: List<Hittable> = emptyList()
) : Hittable {
    private val _objects: MutableList<Hittable> = mutableListOf(*objects.toTypedArray())

    fun clear() {
        _objects.clear()
    }

    fun addObject(obj: Hittable) {
        _objects.add(obj)
    }

    override fun hasHit(ray: Ray, tInterval: Interval): HitRecord? {
        return _objects.fold(
            null
        ) { current: HitRecord?, obj: Hittable ->
            obj.hasHit(
                ray = ray,
                tInterval = if (current != null) tInterval.copy(max = current.t) else tInterval
            ) ?: current
        }
    }


}
