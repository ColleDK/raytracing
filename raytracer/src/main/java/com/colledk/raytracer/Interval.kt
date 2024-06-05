package com.colledk.raytracer

data class Interval(
    val min: Float = Float.MAX_VALUE,
    val max: Float = Float.MIN_VALUE
) {
    fun size(): Float {
        return max - min
    }

    fun contains(value: Float): Boolean {
        return (value in min..max)
    }

    fun surrounds(value: Float): Boolean {
        return min < value && value < max
    }

    fun clamp(value: Float): Float {
        return when {
            value < min -> min
            value > max -> max
            else -> value
        }
    }
}
