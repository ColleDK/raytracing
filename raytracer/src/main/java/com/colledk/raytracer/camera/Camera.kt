package com.colledk.raytracer.camera

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.colledk.raytracer.Interval
import com.colledk.raytracer.randomFloat
import com.colledk.raytracer.ray.Ray
import com.colledk.raytracer.shapes.Hittable
import com.colledk.raytracer.vector.Vector3
import com.colledk.raytracer.vector.randomUnitVector
import com.colledk.raytracer.vector.randomVectorOnHemisphere
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.math.roundToInt

data class Camera(
    private val aspectRatio: Float,
    private val imageWidth: Int,
    private val samplesPerPixel: Int,
    private val maxRayDepth: Int
) {
    private val imageHeight: Int
    private val focalLength: Float
    private val viewportHeight: Float
    private val viewportWidth: Float
    private val cameraCenter: Vector3

    private val viewportU: Vector3
    private val viewportV: Vector3

    private val pixelDeltaU: Vector3
    private val pixelDeltaV: Vector3

    private val viewportUpperLeft: Vector3
    private val pixelLocation: Vector3

    private val pixelSampleScale = 1f / samplesPerPixel

    init {
        imageHeight = (imageWidth / aspectRatio).roundToInt().coerceAtLeast(1)
        focalLength = 1f
        viewportHeight = 2f
        viewportWidth = viewportHeight * (imageWidth.toFloat() / imageHeight.toFloat())
        cameraCenter = Vector3(0f, 0f, 0f)
        viewportU = Vector3(viewportWidth, 0f, 0f)
        viewportV = Vector3(0f, -viewportHeight, 0f)
        pixelDeltaU = viewportU / imageWidth.toFloat()
        pixelDeltaV = viewportV / imageHeight.toFloat()
        viewportUpperLeft = cameraCenter - Vector3(0f, 0f, focalLength) - (viewportU / 2f) - (viewportV / 2f)
        pixelLocation = viewportUpperLeft + (pixelDeltaU + pixelDeltaV) * 0.5f
    }

    suspend fun render(world: Hittable) : Bitmap {
        val jobs = mutableListOf<Deferred<*>>()
        val bitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888)
        coroutineScope {
            for (height in 0 until imageHeight) {
                Log.d("TEST", "Creating height $height out of $(imageHeight - 1)")
                for (width in 0 until imageWidth) {
                    jobs.add(
                        async {
                            val pixelColor = (0 until samplesPerPixel).fold(
                                Vector3()
                            ) { color, index ->
                                val ray = getRay(width, height)
                                if (index == samplesPerPixel - 1) {
                                    (color + rayColor(ray, world)) * pixelSampleScale
                                } else {
                                    color + rayColor(ray, world)
                                }
                            }

                            val intensity = Interval(0.000f, 0.999f)
                            bitmap.setPixel(
                                /* x = */ width,
                                /* y = */ height,
                                /* color = */ Color.argb(
                                    /* alpha = */ 1f,
                                    /* red = */ intensity.clamp(pixelColor[0]),
                                    /* green = */ intensity.clamp(pixelColor[1]),
                                    /* blue = */ intensity.clamp(pixelColor[2])
                                )
                            )
                            Log.d("TEST", "Finished height $height, width $width")
                        }
                    )
                }
            }
        }
        jobs.awaitAll()
        return bitmap
    }

    private fun getRay(width: Int, height: Int): Ray {
        val offset = sampleSquare()
        val pixelSample = pixelLocation + (pixelDeltaU * (width.toFloat() + offset.x)) + (pixelDeltaV * (height.toFloat() + offset.y))
        val rayOrigin = cameraCenter
        val rayDirection = pixelSample - rayOrigin

        return Ray(rayOrigin, rayDirection)
    }

    private fun sampleSquare(): Vector3 {
        return Vector3(randomFloat() -0.5f, randomFloat() - 0.5f, 0f)
    }

    private fun rayColor(ray: Ray, world: Hittable): Vector3 {
        val hit = world.hasHit(ray, Interval(0.001f, Float.MAX_VALUE))
        if (hit != null) {
            return (hit.normal + Vector3.WHITE) * 0.5f
        }
        val unitDirection = ray.direction.asUnitVector()
        val a = 0.5f * (unitDirection.y + 1f)
        return Vector3.WHITE * (1f - a) + Vector3.LIGHT_BLUE * a
    }
}
