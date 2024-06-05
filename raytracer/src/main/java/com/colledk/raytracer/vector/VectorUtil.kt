package com.colledk.raytracer.vector

import com.colledk.raytracer.randomFloat

fun dotProduct(vec1: Vector3, vec2: Vector3): Float {
    return vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z
}

fun crossProduct(vec1: Vector3, vec2: Vector3): Vector3 {
    return Vector3(
        x = vec1.y * vec2.z - vec1.z * vec2.y,
        y = vec1.z * vec2.x - vec1.x * vec2.z,
        z = vec1.x * vec2.y - vec1.y * vec2.x
    )
}

fun randomVector(): Vector3 {
    return Vector3(randomFloat(), randomFloat(), randomFloat())
}

fun randomVector(min: Float, max: Float): Vector3 {
    return Vector3(randomFloat(min, max), randomFloat(min, max), randomFloat(min, max))
}

fun randomVectorOnHemisphere(normal: Vector3): Vector3 {
    val onUnitSphere = randomUnitVector()
    return if (dotProduct(onUnitSphere, normal) > 0f) {
        onUnitSphere
    } else {
        -onUnitSphere
    }
}

fun randomUnitVector(): Vector3 {
    return randomVectorInUnitSphere().asUnitVector()
}

fun randomVectorInUnitSphere(): Vector3 {
    while (true) {
        val point = randomVector(-1f, 1f)
        if (point.lengthSquared() < 1f) {
            return point
        }
    }
}
