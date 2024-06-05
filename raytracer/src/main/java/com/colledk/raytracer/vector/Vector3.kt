package com.colledk.raytracer.vector

import kotlin.math.pow
import kotlin.math.sqrt

data class Vector3(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f
) {
    companion object {
        val BLACK = Vector3(0f, 0f, 0f)
        val WHITE = Vector3(1f, 1f, 1f)
        val LIGHT_BLUE = Vector3(0.5f, 0.7f, 1f)
        val RED = Vector3(1f, 0f, 0f)
    }

    constructor(x: Double, y: Double, z: Double) : this(x.toFloat(), y.toFloat(), z.toFloat())

    fun length(): Float {
        return sqrt(lengthSquared())
    }

    fun lengthSquared(): Float {
        return x.pow(2) + y.pow(2) + z.pow(2)
    }

    fun asUnitVector(): Vector3 {
        return this / length()
    }

    operator fun unaryMinus(): Vector3 {
        return Vector3(
            x = -x,
            y = -y,
            z = -z
        )
    }

    operator fun get(index: Int): Float {
        return when(index) {
            0 -> x
            1 -> y
            else -> z
        }
    }

    operator fun plus(other: Vector3) : Vector3 {
        return Vector3(
            x = this.x + other.x,
            y = this.y + other.y,
            z = this.z + other.z
        )
    }

    operator fun minus(other: Vector3) : Vector3 {
        return Vector3(
            x = this.x - other.x,
            y = this.y - other.y,
            z = this.z - other.z
        )
    }

    operator fun times(other: Vector3) : Vector3 {
        return Vector3(
            x = this.x * other.x,
            y = this.y * other.y,
            z = this.z * other.z
        )
    }

    operator fun times(t: Float) : Vector3 {
        return Vector3(
            x = this.x * t,
            y = this.y * t,
            z = this.z * t
        )
    }

    operator fun div(other: Vector3) : Vector3 {
        return Vector3(
            x = this.x / other.x,
            y = this.y / other.y,
            z = this.z / other.z
        )
    }

    operator fun div(t: Float) : Vector3 {
        return Vector3(
            x = this.x / t,
            y = this.y / t,
            z = this.z / t
        )
    }
}
