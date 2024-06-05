package com.colledk.raytracing

import android.util.Log
import android.view.WindowMetrics
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.window.layout.WindowMetricsCalculator
import com.colledk.raytracer.renderer.Camera
import com.colledk.raytracer.vector.Vector3
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun RayTracingComponent(

) {
    val availableWidth = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(LocalContext.current).bounds.width()

    val density = LocalDensity.current

    val (imageWidth, imageHeight) = remember {
        with(density) {
            Pair(availableWidth.toDp(), availableWidth.toDp())
        }
    }

    val scope = rememberCoroutineScope()
    
    Canvas(modifier = Modifier.size(imageWidth, imageHeight), onDraw = {
        drawRect(Color.White)
        for (height in 0 until imageHeight.roundToPx()) {
            Log.d("TEST", "Building row $height $availableWidth")
            for (width in 0 until imageWidth.roundToPx()) {
                val r = width.toFloat() / (imageWidth.roundToPx() - 1)
                val g = height.toFloat() / (imageHeight.roundToPx() - 1)
                val b = 0f

                drawRect(
                    color = Color(r, g, b),
                    topLeft = Offset(height.toFloat(), width.toFloat()),
                    size = Size(1f, 1f)
                )
            }
        }
    })
}