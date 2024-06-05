package com.colledk.raytracer.vector

import kotlin.math.abs
import kotlin.math.sqrt

data class Vector3(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f
) {
    // Operator functions
    operator fun unaryMinus(): Vector3 {
        return Vector3(
            x = -this.x,
            y = -this.y,
            z = -this.z
        )
    }

    operator fun minus(other: Vector3): Vector3 {
        return Vector3(
            x = this.x - other.x,
            y = this.y - other.y,
            z = this.z - other.z
        )
    }

    operator fun plus(other: Vector3): Vector3 {
        return Vector3(
            x = this.x + other.x,
            y = this.y + other.y,
            z = this.z + other.z
        )
    }

    operator fun times(other: Vector3): Vector3 {
        return Vector3(
            x = this.x * other.x,
            y = this.y * other.y,
            z = this.z * other.z
        )
    }

    operator fun times(t: Float): Vector3 {
        return Vector3(
            x = x * t,
            y = y * t,
            z = z * t
        )
    }

    operator fun div(other: Vector3): Vector3 {
        return Vector3(
            x = this.x / other.x,
            y = this.y / other.y,
            z = this.z / other.z
        )
    }

    operator fun div(t: Float): Vector3 {
        return this * (1 / t)
    }

    // Vector functions
    fun length(): Float {
        return sqrt(lengthSquared())
    }

    fun lengthSquared(): Float {
        return x * x + y * y + z * z
    }

    fun nearZero(): Boolean {
        val smallNumber = 1e-8
        return abs(x) < smallNumber && abs(y) < smallNumber && abs(z) < smallNumber
    }

    fun asUnitVector(): Vector3 {
        return this / this.length()
    }

    operator fun get(i: Int): Float {
        return when(i) {
            0 -> x
            1 -> y
            else -> z
        }
    }
}
