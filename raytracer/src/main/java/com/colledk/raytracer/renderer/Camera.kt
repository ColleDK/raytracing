package com.colledk.raytracer.renderer

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.colledk.raytracer.Interval
import com.colledk.raytracer.shapes.Hittable
import com.colledk.raytracer.vector.Ray
import com.colledk.raytracer.vector.Vector3
import com.colledk.raytracer.vector.crossProduct
import com.colledk.raytracer.vector.randomInUnitDisk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.math.roundToInt
import kotlin.math.tan
import kotlin.random.Random

data class Camera(
    // Rendering values
    val imageWidth: Float,
    val aspectRatio: Float,
    val pixelSampleSize: Int,
    val maxRayDepth: Int,
    // Camera values
    val fov: Float,
    val position: Vector3,
    val lookAt: Vector3,
    val upVector: Vector3,
    // Defocus values
    val defocusAngle: Float,
    val focusDistance: Float
) {
    val imageHeight: Float = imageWidth / aspectRatio
    private val cameraCenter: Vector3 = position
    private val pixelSampleScale: Float = 1f / pixelSampleSize

    private val u: Vector3
    private val v: Vector3
    private val w: Vector3

    private val pixelU: Vector3
    private val pixelV: Vector3
    private val pixelLocation: Vector3

    private val defocusU: Vector3
    private val defocusV: Vector3

    init {
        val theta = degreeToRadian(fov)
        val h = tan(theta / 2)
        val viewportHeight = 2 * h * focusDistance
        val viewportWidth = viewportHeight * (imageWidth / imageHeight)

        w = (position-lookAt).asUnitVector()
        u = (crossProduct(upVector, w)).asUnitVector()
        v = crossProduct(w, u)

        val viewportU = u * viewportWidth
        val viewportV = -v * viewportHeight

        pixelU = viewportU / imageWidth
        pixelV = viewportV / imageHeight

        val viewportUpperLeft = cameraCenter - (w * focusDistance) - viewportU / 2f - viewportV / 2f
        pixelLocation = viewportUpperLeft + (pixelU + pixelV) * 0.5f

        val defocusRadius = focusDistance * tan(degreeToRadian(defocusAngle / 2))
        defocusU = u * defocusRadius
        defocusV = v * defocusRadius
    }

    suspend fun render(world: Hittable): Bitmap {
        val width = imageWidth.roundToInt()
        val height = imageHeight.roundToInt()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val jobs = mutableListOf<Deferred<*>>()
        coroutineScope {
            for (h in 0 until height) {
                jobs.add(
                    async {
                        setPixels(
                            start = h * (width - 1),
                            end = h * (width - 1) + (width - 1),
                            world = world,
                            bitmap = bitmap,
                            maxHeight = (height - 1),
                            maxWidth = (width - 1)
                        )
                    }
                )
            }
        }
        jobs.awaitAll()
        Log.d("TEST", "Finished all jobs")
        return bitmap
    }

    private suspend fun setPixels(start: Int, end: Int, world: Hittable, bitmap: Bitmap, maxHeight: Int, maxWidth: Int) {
        coroutineScope {
            (start until end).map {
                async {
                    val height = it / maxWidth
                    val width = it % maxWidth
                    Log.d("TEST", "Creating pixel $start, $end. Height: $height, Width: $width, index: $it, $maxWidth, $maxHeight")

                    var pixelColor: Vector3 = Vector3()
                    for (sample in 0 until pixelSampleSize) {
                        val ray = getRay(width = width, height = height)
                        pixelColor += rayColor(
                            ray = ray,
                            depth = maxRayDepth,
                            world = world
                        )
                    }
                    pixelColor *= pixelSampleScale
                    bitmap.setPixel(
                        width,
                        height,
                        Color.argb(1f, pixelColor[0], pixelColor[1], pixelColor[2])
                    )
                }
            }.awaitAll()
            Log.d("TEST", "Finished job $start, $end")
        }
    }

    private fun rayColor(ray: Ray, depth: Int, world: Hittable) : Vector3 {
        val blue = Vector3(0.5f, 0.7f, 1.0f)
        val white = Vector3(1f, 1f, 1f)
        val black = Vector3(0f, 0f, 0f)
        if (depth <= 0) {
            return black
        }

        val record = world.hasHit(ray, Interval(0.001f, Float.MAX_VALUE))
        return if (record != null) {
            val scatter = record.material.scatter(ray, record)
            if (scatter != null) {
                scatter.attenuation * rayColor(
                    ray = scatter.scattered,
                    depth = depth - 1,
                    world = world
                )
            } else {
                black
            }
        } else {
            val unitDirection = ray.direction.asUnitVector()
            val a = 0.5f * (unitDirection.y + 1f)
            white * (1f - a) + blue * a
        }
    }

    private fun getRay(width: Int, height: Int): Ray {
        val offset = sampleSquare()
        val pixelSample = pixelLocation + (pixelU * (width * offset.x)) + (pixelV * (height * offset.y))

        val rayOrigin = if (defocusAngle <= 0) cameraCenter else defocusDiskSample()
        val rayDirection = pixelSample - rayOrigin

        return Ray(rayOrigin, rayDirection)
    }

    private fun sampleSquare(): Vector3 {
        val generator = Random(System.currentTimeMillis())
        return Vector3(
            x = generator.nextFloat() - 0.5f,
            y = generator.nextFloat() - 0.5f,
            z = 0f
        )
    }

    private fun defocusDiskSample(): Vector3 {
        val p = randomInUnitDisk()
        return cameraCenter + (defocusU * p.x) + (defocusV * p.y)
    }

    private fun degreeToRadian(degrees: Float): Float {
        return (degrees * Math.PI / 180).toFloat()
    }
}