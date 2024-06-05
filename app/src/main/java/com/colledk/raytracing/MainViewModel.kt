package com.colledk.raytracing

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colledk.raytracer.camera.Camera
import com.colledk.raytracer.shapes.HittableList
import com.colledk.raytracer.shapes.Sphere
import com.colledk.raytracer.vector.Vector3
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

const val IMAGE_WIDTH = 400

class MainViewModel : ViewModel() {
    private val _bitmap: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val bitmap: StateFlow<Bitmap?> = _bitmap

    init {
        createBitmap()
    }

    private fun createBitmap() {
        viewModelScope.launch {
            val world = HittableList()
            world.addObject(Sphere(Vector3(0f, 0f, -1f), 0.5f))
            world.addObject(Sphere(Vector3(0f, -100.5f, -1f), 100f))

            val camera = Camera(
                aspectRatio = 16f / 9f,
                imageWidth = IMAGE_WIDTH,
                samplesPerPixel = 100,
                maxRayDepth = 10
            )
            _bitmap.value = camera.render(world)
        }
    }
}