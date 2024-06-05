package com.colledk.raytracer

import kotlin.random.Random

fun randomFloat(): Float {
    val generator = Random(System.currentTimeMillis())
    return generator.nextFloat()
}

fun randomFloat(min: Float, max: Float): Float {
    val generator = Random(System.currentTimeMillis())
    return generator.nextDouble(min.toDouble(), max.toDouble()).toFloat()
}