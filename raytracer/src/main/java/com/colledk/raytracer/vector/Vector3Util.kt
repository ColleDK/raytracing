package com.colledk.raytracer.vector

import com.colledk.raytracer.randomDouble
import com.colledk.raytracer.randomFloat
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

fun randomVector3(): Vector3 {
    return Vector3(
        x = randomFloat(),
        y = randomFloat(),
        z = randomFloat()
    )
}

fun randomVector3(min: Double, max: Double): Vector3 {
    return Vector3(
        x = randomDouble(min, max).toFloat(),
        y = randomDouble(min, max).toFloat(),
        z = randomDouble(min, max).toFloat()
    )
}

fun randomInUnitSphere(): Vector3 {
    while (true) {
        val point = randomVector3(-1.0, 1.0)
        if (point.lengthSquared() < 1) {
            return point
        }
    }
}

fun randomUnitVector(): Vector3 {
    return randomInUnitSphere().asUnitVector()
}


fun randomInUnitDisk(): Vector3 {
    val generator = Random(System.currentTimeMillis())
    while (true) {
        val p = Vector3(
            x = generator.nextDouble(-1.0, 1.0).toFloat(),
            y = generator.nextDouble(-1.0, 1.0).toFloat(),
            z = 0f
        )
        if (p.lengthSquared() < 1) {
            return p
        }
    }
}

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

/**
 * Reflect a vector with a normal vector.
 */
fun reflect(vec: Vector3, normal: Vector3): Vector3 {
    return vec - normal * dotProduct(vec, normal) * 2f
}

/**
 * Refract a vector with a normal vector given the angle eta
 */
fun refract(vec: Vector3, normal: Vector3, eta: Float): Vector3 {
    val cosTheta = min(dotProduct(-vec, normal), 1f)
    val rPerp = (vec * cosTheta * normal) * eta
    val rParallel = normal * -sqrt(abs(1f - rPerp.lengthSquared()))
    return rPerp + rParallel
}