package com.rei.compose.playground.ui.compose

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt


enum class UITutorialType {
    SQUARE, CIRCLE
}

data class UITutorialPosition(
    val type: UITutorialType,
    val coordinates: LayoutCoordinates,
    val title: String, val subTitle: String,
    val titleColor: Color = Color.White,
    val subTitleColor: Color = Color.White,
)

@Composable
fun UITutorials(
    snapshotStateList: SnapshotStateMap<Int, UITutorialPosition>,
    color: Color = Color.Red,
    onCompleted: () -> Unit
) {
    if (snapshotStateList.isEmpty()) return
    val index = remember {
        mutableStateOf(0)
    }
    snapshotStateList[index.value]?.let {
        when (it.type) {
            UITutorialType.CIRCLE -> {
                UITutorialScreen(target = it, color) {
                    index.value = index.value + 1
                }
            }
            UITutorialType.SQUARE -> {
                UITutorialSquareScreen(target = it, color) {
                    index.value = index.value + 1
                }
            }
        }
    } ?: run {
        onCompleted()
    }

}

@Composable
fun UITutorialScreen(
    target: UITutorialPosition,
    color: Color = Color.Red,
    onClick: () -> Unit
) {
    val topArea = 88.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val yOffset = with(LocalDensity.current) {
        target.coordinates.positionInRoot().y.toDp()
    }

    val targetRect = target.coordinates.boundsInRoot()
    val targetRadius = targetRect.maxDimension / 2f + 40f
    // 40f extra traget spacing
    val animationSpec = infiniteRepeatable<Float>(
        animation = tween(2000, easing = FastOutLinearInEasing),
        repeatMode = RepeatMode.Restart,
    )
    val animatables = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) }
    )

    animatables.forEachIndexed { index, animatable ->
        LaunchedEffect(animatable) {
            delay(index * 1000L)
            animatable.animateTo(
                targetValue = 1f, animationSpec = animationSpec
            )
        }
    }
    val dys = animatables.map { it.value }
    var textCoordinate: MutableState<LayoutCoordinates?> = remember {
        mutableStateOf(null)
    }
    var outerOffset = remember {
        mutableStateOf(Offset(0f, 0f))
    }
    var outerRadius = remember {
        mutableStateOf(0f)
    }
    textCoordinate.value?.let { textCoords ->
        val textRect = textCoords.boundsInRoot()
        val textHeight = textCoords.size.height
        val isInGutter = topArea > yOffset || yOffset > screenHeight.dp.minus(topArea)
        outerOffset.value =
            getOuterCircleCenter(targetRect, textRect, targetRadius, textHeight, isInGutter)
        outerRadius.value = getOuterRadius(textRect, targetRect) + targetRadius
    }
    val outerAnimatable = remember { Animatable(0.6f) }

    LaunchedEffect(target) {
        outerAnimatable.snapTo(0.6f)

        outerAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing,
            ),
        )
    }
    Box {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.99f)
                .clickable {
                    onClick()
                }
        ) {
            drawCircle(
                color = color,
                center = outerOffset.value,
                radius = outerRadius.value * outerAnimatable.value,
                alpha = 0.9F
            )
            dys.forEach { dy ->
                drawCircle(
                    color = Color.White,
                    radius = targetRect.maxDimension * dy * 2f,
                    center = targetRect.center,
                    alpha = 1 - dy
                )
            }
            drawCircle(
                color = Color.White,
                radius = targetRadius,
                center = targetRect.center,
                blendMode = BlendMode.Clear
            )
        }
        ShowCaseText(
            currentTarget = target,
            targetRect,
            targetRadius
        ) {
            textCoordinate.value = it
        }
    }
}

@Composable
fun UITutorialSquareScreen(
    target: UITutorialPosition,
    color: Color = Color.Red,
    onClick: () -> Unit
) {
    val topArea = 88.dp
    val density = LocalDensity.current
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val screenWidthPx = with(density) { screenWidthDp.toPx() }
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val yOffset = with(LocalDensity.current) {
        target.coordinates.positionInRoot().y.toDp()
    }

    val targetRect = target.coordinates.boundsInRoot()
    val targetRadius = targetRect.maxDimension / 2f + 40f
    // 40f extra traget spacing
    val animationSpec = infiniteRepeatable<Float>(
        animation = tween(2000, easing = FastOutSlowInEasing),
        repeatMode = RepeatMode.Restart,
    )
    val animatables = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) }
    )

    animatables.forEachIndexed { index, animatable ->
        LaunchedEffect(animatable) {
            delay(index * 1000L)
            animatable.animateTo(
                targetValue = 1f, animationSpec = animationSpec
            )
        }
    }
    val dys = animatables.map { it.value }
    var textCoordinate: MutableState<LayoutCoordinates?> = remember {
        mutableStateOf(null)
    }
    var outerOffset = remember {
        mutableStateOf(Offset(0f, 0f))
    }
    var outerRadius = remember {
        mutableStateOf(0f)
    }
    textCoordinate.value?.let { textCoords ->
        val textRect = textCoords.boundsInRoot()
        val textHeight = textCoords.size.height
        val isInGutter = topArea > yOffset || yOffset > screenHeight.dp.minus(topArea)
        outerOffset.value =
            getOuterCircleCenter(targetRect, textRect, targetRadius, textHeight, isInGutter)
        outerRadius.value = getOuterRadius(textRect, targetRect) + targetRadius
    }
    val outerAnimatable = remember { Animatable(0.6f) }

    LaunchedEffect(target) {
        outerAnimatable.snapTo(0.3f)

        outerAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing,
            ),
        )
    }
    var offset = remember {
        mutableStateOf(0F)
    }
    var textHeight = remember {
        mutableStateOf(0)
    }
    var isTop = remember {
        mutableStateOf(false)
    }
    Box {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.99f)
                .clickable {
                    onClick()
                }
        ) {
            drawRect(
                color = color,
                topLeft = Offset(
                    x = 0F,
                    y = (targetRect.top - textHeight.value.times(outerAnimatable.value))
                ),
                size = targetRect.size.copy(
                    width = screenWidthPx,
                    height = (textHeight.value + targetRect.height.times(0.5)).toFloat() * outerAnimatable.value
                ),
                alpha = 0.8F
            )
            drawRect(
                color = color,
                topLeft = targetRect.centerLeft.copy(x = 0F),
                size = targetRect.size.copy(
                    width = screenWidthPx,
                    height = (textHeight.value + targetRect.height.times(0.5)).toFloat() * outerAnimatable.value
                ),
                alpha = 0.8F
            )
            dys.forEach { dy ->
                drawRect(
                    color = Color.White,
                    topLeft = Offset(
                        x = 0F,
                        y = (targetRect.top - textHeight.value.times(dy))
                    ),
                    size = targetRect.size.copy(
                        width = screenWidthPx,
                        height = (textHeight.value * 2 + targetRect.height) * dy
                    ),
                    alpha = 1 - dy
                )
            }
            drawRect(
                color = Color.White,
                topLeft = targetRect.topLeft.copy(
                    x = targetRect.topLeft.x - 25,
                    y = targetRect.topLeft.y - 25
                ),
                size = targetRect.size.copy(
                    width = screenWidthPx,
                    height = targetRect.height + 50
                ),
                blendMode = BlendMode.Clear
            )
        }
        ShowCaseSquareText(
            currentTarget = target,
            targetRect,
        ) { top, offsetText, height ->
            offset.value = offsetText
            textHeight.value = height
            isTop.value = top
        }
    }
}

@Composable
private fun ShowCaseSquareText(
    currentTarget: UITutorialPosition,
    targetRect: Rect,
    location: (Boolean, Float, Int) -> Unit
) {
    var txtOffsetY = remember {
        mutableStateOf(0f)
    }
    Column(
        modifier = Modifier
            .offset(y = with(LocalDensity.current) {
                txtOffsetY.value.toDp()
            })
            .onGloballyPositioned {
                val textHeight = it.size.height
                val possibleTop =
                    targetRect.topCenter.y - textHeight

                txtOffsetY.value = if (possibleTop > 0) {
                    location(true, targetRect.topCenter.y, textHeight)
                    possibleTop
                } else {
                    location(
                        false,
                        targetRect.bottomCenter.y,
                        textHeight
                    )
                    targetRect.bottomCenter.y
                }
            }
            .padding(16.dp)
    )
    {
        Text(
            text = currentTarget.title,
            fontSize = 24.sp,
            color = currentTarget.titleColor,
            fontWeight = FontWeight.Bold
        )
        Text(text = currentTarget.subTitle, fontSize = 16.sp, color = currentTarget.subTitleColor)
    }
}

@Composable
private fun ShowCaseText(
    currentTarget: UITutorialPosition,
    targetRect: Rect,
    targetRadius: Float,
    location: (LayoutCoordinates) -> Unit
) {
    var txtOffsetY = remember {
        mutableStateOf(0f)
    }
    Column(
        modifier = Modifier
            .offset(y = with(LocalDensity.current) {
                txtOffsetY.value.toDp()
            })
            .onGloballyPositioned {
                location(it)
                val textHeight = it.size.height
                val possibleTop =
                    targetRect.center.y - targetRadius - textHeight

                txtOffsetY.value = if (possibleTop > 0) {
                    possibleTop
                } else {
                    targetRect.center.y + targetRadius
                }
            }
            .padding(16.dp)
    )
    {
        Text(
            text = currentTarget.title,
            fontSize = 24.sp,
            color = currentTarget.titleColor,
            fontWeight = FontWeight.Bold
        )
        Text(text = currentTarget.subTitle, fontSize = 16.sp, color = currentTarget.subTitleColor)
    }
}

fun getOuterRadius(textRect: Rect, targetRect: Rect): Float {

    val topLeftX = min(textRect.topLeft.x, targetRect.topLeft.x)
    val topLeftY = min(textRect.topLeft.y, targetRect.topLeft.y)
    val bottomRightX = max(textRect.bottomRight.x, targetRect.bottomRight.x)
    val bottomRightY = max(textRect.bottomRight.y, targetRect.bottomRight.y)

    val expandedBounds = Rect(topLeftX, topLeftY, bottomRightX, bottomRightY)

    val d = sqrt(
        expandedBounds.height.toDouble().pow(2.0)
                + expandedBounds.width.toDouble().pow(2.0)
    ).toFloat()

    return (d / 2f)
}

fun getOuterCircleCenter(
    targetBound: Rect,
    textBound: Rect,
    targetRadius: Float,
    textHeight: Int,
    isInGutter: Boolean,
): Offset {
    var outerCenterX: Float
    var outerCenterY: Float

    val onTop =
        targetBound.center.y - targetRadius - textHeight > 0

    val left = min(
        textBound.left,
        targetBound.left - targetRadius
    )
    val right = max(
        textBound.right,
        targetBound.right + targetRadius
    )

    val centerY =
        if (onTop) targetBound.center.y - targetRadius - textHeight
        else targetBound.center.y + targetRadius + textHeight

    outerCenterY = centerY
    outerCenterX = (left + right) / 2

    if (isInGutter) {
        outerCenterY = targetBound.center.y
    }

    return Offset(outerCenterX, outerCenterY)
}