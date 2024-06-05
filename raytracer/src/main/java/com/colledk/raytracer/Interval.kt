package com.colledk.raytracer

// Default interval is an empty set
data class Interval(
    val min: Float = Float.MAX_VALUE,
    val max: Float = Float.MIN_VALUE
) {
    fun size(): Float {
        return max - min
    }

    fun contains(value: Float): Boolean {
        return value in min..max
    }

    fun surrounds(value: Float): Boolean {
        return min < value && value < max
    }

    fun clamp(value: Float): Float {
        return if (value < min) {
            min
        } else if (value > max) {
            max
        } else {
            value
        }
    }

    /**
     * Expand an interval by a certain amount. This is done equally on both sides (min/max) by half the amount.
     */
    fun expand(value: Float): Interval {
        val padding = value / 2
        return Interval(
            min = min - padding,
            max = max + padding
        )
    }
}
