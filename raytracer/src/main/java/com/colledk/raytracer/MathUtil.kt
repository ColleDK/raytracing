package com.colledk.raytracer

import kotlin.random.Random

fun randomFloat(): Float {
    val generator = Random(System.currentTimeMillis())
    return generator.nextFloat()
}

fun randomDouble(min: Double, max: Double): Double {
    val generator = Random(System.currentTimeMillis())
    return generator.nextDouble(min, max)
}