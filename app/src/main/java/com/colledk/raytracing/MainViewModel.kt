package com.colledk.raytracing

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colledk.raytracer.materials.Dieletric
import com.colledk.raytracer.materials.Lambertian
import com.colledk.raytracer.materials.Metal
import com.colledk.raytracer.randomDouble
import com.colledk.raytracer.randomFloat
import com.colledk.raytracer.renderer.Camera
import com.colledk.raytracer.shapes.HittableList
import com.colledk.raytracer.shapes.Sphere
import com.colledk.raytracer.vector.Vector3
import com.colledk.raytracer.vector.randomVector3
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

const val IMAGE_WIDTH = 256

class MainViewModel : ViewModel() {
    private val _bitmap: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val bitmap: StateFlow<Bitmap?> = _bitmap

    init {
        createBitmap()
    }

    private fun createBitmap() {
        viewModelScope.launch {
            val camera = Camera(
                imageWidth = IMAGE_WIDTH.toFloat(),
                aspectRatio = 16f / 9f,
                pixelSampleSize = 20,
                maxRayDepth = 10,
                fov = 20f,
                position = Vector3(13f, 2f, 3f),
                lookAt = Vector3(0f, 0f, 0f),
                upVector = Vector3(0f, 1f, 0f),
                defocusAngle = 0.6f,
                focusDistance = 10f
            )

            val world = HittableList(
                emptyList()
            )
            val groundMaterial = Lambertian(Vector3(0.5f, 0.5f, 0.5f))
            world.addObject(
                obj = Sphere(
                    center = Vector3(x = 0f, y = -1000f, z = 0f),
                    radius = 1000f,
                    material = groundMaterial
                )
            )

            for (i in -11 until 11) {
                for (j in -11 until 11) {
                    val material = randomFloat()
                    val center = Vector3(i + 0.9f * randomFloat(), 0.2f, j + 0.9f * randomFloat())

                    if ((center - Vector3(4f, 0.2f, 0f)).length() > 0.9f) {
                        val sphereMaterial = when {
                            material < 0.8f -> {
                                val albedo = randomVector3() * randomVector3()
                                Lambertian(albedo = albedo)
                            }
                            material < 0.95f -> {
                                val albedo = randomVector3(0.5, 1.0)
                                val fuzz = randomDouble(0.0, 0.5)
                                Metal(albedo, fuzz.toFloat())
                            }
                            else -> {
                                Dieletric(1.5f)
                            }
                        }
                        world.addObject(Sphere(center, 0.2f, sphereMaterial))
                    }
                }
            }

            val sphere1Material = Dieletric(1.5f)
            world.addObject(Sphere(Vector3(0f, 1f, 0f), 1f, sphere1Material))

            val sphere2Material = Lambertian(Vector3(0.4f, 0.2f, 0.1f))
            world.addObject(Sphere(Vector3(-4f, 1f, 0f), 1f, sphere2Material))

            val sphere3Material = Metal(Vector3(0.7f, 0.6f, 0.5f), 0.0f)
            world.addObject(Sphere(Vector3(4f, 1f, 0f), 1f, sphere3Material))

            _bitmap.value = camera.render(world)
        }
    }
}