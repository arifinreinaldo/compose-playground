@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalUnitApi::class)

package com.rei.compose.playground.ui.compose

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.rei.compose.playground.util.set
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.roundToInt


@Composable
fun UIInput(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    value: String,
    label: String,
    valueFont: Float = 20F,
    readOnly: Boolean = false,
    maxLine: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    click: (() -> Unit)? = null,
    maxLength: Int = -1,
    error: String? = null,
) {
    Column(
        modifier
            .padding(vertical = 7.dp),
        Arrangement.SpaceEvenly
    ) {
        OutlinedTextField(
            readOnly = readOnly,
            value = value, onValueChange = {
                if ((maxLength > -1 && it.length <= maxLength) || maxLength == -1) {
                    if (keyboardType != KeyboardType.Phone) {
                        onValueChange(it)
                    } else if (keyboardType == KeyboardType.Phone && !it.contains(Regex("\\D"))) {
                        onValueChange(it)
                    }
                }
            },
            textStyle = TextStyle.Default.copy(fontSize = valueFont.sp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    click?.invoke()
                },
            singleLine = maxLine == 1,
            maxLines = maxLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon,
            visualTransformation = visualTransformation,
            enabled = click == null, //If textfied enabled false clickable can be fired
            label = { Text(label) },
            isError = error != null
        )
        error?.apply {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = this,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun UIPassword(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    error: String? = null,
    onValueChange: (String) -> Unit,
) {
    var passwordVisible: Boolean by remember { mutableStateOf(false) }
    UIInput(
        modifier = modifier,
        onValueChange = { data ->
            onValueChange(data)
        },
        value = value,
        label = label,
        keyboardType = KeyboardType.Password,
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Default.Visibility
            else Icons.Default.VisibilityOff

            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        error = error
    )
}

@Composable
fun UIDescriptiveLink(
    modifier: Modifier,
    description: String,
    url: String,
    action: () -> Unit
) {
    Row(modifier = modifier) {
        Text(description)
        Spacer(modifier = Modifier.width(10.dp))
        Text(url, modifier = Modifier.clickable {
            action()
        }, color = MaterialTheme.colorScheme.primary)
    }
}

data class UIOTPData(
    var otp: MutableState<String> = mutableStateOf(""),
    val focus: MutableState<FocusRequester> = mutableStateOf(FocusRequester())
)

@Composable
fun UIOTP(
    modifier: Modifier,
    value: String,
    otp: (String) -> Unit,
    submit: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Number,
    count: Int = 4,
) {
    val otpData = remember { mutableListOf<UIOTPData>() }
    if (otpData.isEmpty()) {
        for (i in 1..count) {
            UIOTPData().apply {
                if (value.length >= i) {
                    this.otp.value = value[i - 1].toString()
                }
                otpData.add(this)
            }
        }
    }
    BoxWithConstraints(modifier = modifier) {
        val width = maxWidth / (count + 1)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            otpData.forEachIndexed { i, data ->
                UIOTPField(
                    modifier = Modifier
                        .width(width)
                        .aspectRatio(1F, true)
                        .focusRequester(data.focus.value),
                    action = { text ->
                        data.otp.value = text
                        if (text.isEmpty()) {
                            val prev = i - 1
                            if (prev >= 0) {
                                otpData[prev].focus.value.requestFocus()
                            }
                        } else {
                            val next = i + 1
                            if (next >= count) {
                                submit(otpData.map { it.otp.value }.joinToString(""))
                            } else {
                                otpData[next].focus.value.requestFocus()
                            }
                        }
                    },
                    value = data.otp.value,
                    keyboardType = keyboardType
                )
            }
            otp(otpData.map { it.otp.value }.joinToString(""))
        }
    }
}

@Composable
fun UIOTPField(
    modifier: Modifier = Modifier,
    action: (String) -> Unit,
    value: String,
    keyboardType: KeyboardType
) {

    OutlinedTextField(
        modifier = modifier,
        value = value,
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = TextUnit(25F, TextUnitType.Sp)
        ),
        onValueChange = {
            if (it.length <= 1) {
                action(it)
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

data class ComboBoxData(
    val Code: String,
    val Text: String
)

@Composable
fun UIComboBox(
    modifier: Modifier,
    label: String,
    options: List<ComboBoxData>,
    onValueChange: (String) -> Unit,
    value: String,
) {
    val isKeyboardOpen by keyboardAsState()

    val searchAble = options.size > 10
    var filterOptions = remember { mutableListOf<ComboBoxData>() }
    var filter = remember { mutableStateOf("") }

    fun filter() {
        if (filter.value.isEmpty()) {
            filterOptions.set(options)
        } else {
            filterOptions.set(options.filter {
                it.Text.contains(
                    filter.value,
                    ignoreCase = true
                )
            })
        }
    }
    filter()
    val density = LocalDensity.current
    val view = LocalView.current
    val verticalMarginInPx = with(density) { 48.dp.roundToPx() }
    var expanded by remember { mutableStateOf(false) }
    var width by remember {
        mutableStateOf(0)
    }
    var height by remember {
        mutableStateOf(0)
    }
    var parentWidth by remember {
        mutableStateOf(0)
    }
    var parentHeight by remember {
        mutableStateOf(0)
    }
    var offset by remember {
        mutableStateOf(IntOffset(0, 0))
    }
    val coordinates = remember { Ref<LayoutCoordinates>() }
    var menuHeight by remember { mutableStateOf(0) }
    isKeyboardOpen.apply {
        updateHeight(
            view.rootView, coordinates.value, verticalMarginInPx
        ) {
            menuHeight = it
        }
    }
    UIInput(
        modifier = modifier
            .onGloballyPositioned {
                coordinates.value = it
                width = it.size.width
                height = it.size.height
                parentWidth = it.parentCoordinates?.size?.width ?: 0
                parentHeight = it.parentCoordinates?.size?.height ?: 0
                offset = IntOffset(
                    it.positionInParent().x.roundToInt(),
                    it.positionInParent().y.roundToInt()
                )
                updateHeight(
                    view.rootView, coordinates.value, verticalMarginInPx
                ) {
                    menuHeight = it
                }
            },
        onValueChange = {},
        value = options.find { it.Code == value }?.Text ?: "",
        label = label,
        trailingIcon = {
            Icon(
                Icons.Filled.ArrowDropDown,
                "Trailing icon for exposed dropdown menu",
                Modifier.rotate(
                    if (expanded)
                        180f
                    else
                        360f
                )
            )
        },
        click = {
            expanded = !expanded
        }
    )
    if (expanded) {
        var popUpHeight = remember { mutableStateOf(0) }
        var currentOffset = IntOffset(offset.x, offset.y)
        if (offset.y + height + popUpHeight.value > parentHeight) {
            Log.d("TAG", "UIComboBox: $height $menuHeight")
            currentOffset = currentOffset.copy(y = (offset.y - popUpHeight.value))
        } else {
            currentOffset = currentOffset.copy(y = (offset.y + height))
        }
        Popup(
            offset = currentOffset,
            onDismissRequest = { expanded = false },
            properties = PopupProperties(
                focusable = true
            ),
            content = {
                Column(
                    modifier = Modifier
                        .onSizeChanged {
                            popUpHeight.value = it.height
                        }
                        .width(with(density) { width.toDp() })
                        .heightIn(max = with(density) { menuHeight.toDp() })
                        .background(MaterialTheme.colorScheme.background)
                        .border(1.dp, Color.Gray.copy(alpha = 0.8F))
                        .clip(RoundedCornerShape(10F))
                ) {
                    if (searchAble) {
                        UIInput(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            value = filter.value,
                            onValueChange = { query ->
                                filter.value = query
                            },
                            label = "",
                            maxLine = 1,
                        )
                    }
                    filter()
                    if (filterOptions.isNotEmpty()) {
                        LazyColumn {
                            items(items = filterOptions) { item ->
                                Text(
                                    text = item.Text, modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 15.dp)
                                        .clickable {
                                            expanded = false
                                            onValueChange(item.Code)
                                        }
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(bottom = 16.dp), contentAlignment = Alignment.Center
                        ) {
                            Text(text = "--No Data--")
                        }
                    }
                }
            })
    }

}

fun updateHeight(
    view: View,
    coordinates: LayoutCoordinates?,
    verticalMarginInPx: Int,
    onHeightUpdate: (Int) -> Unit
) {
    coordinates ?: return
    val visibleWindowBounds = Rect().let {
        view.getWindowVisibleDisplayFrame(it)
        it
    }
    val heightAbove = coordinates.boundsInWindow().top - visibleWindowBounds.top
    val heightBelow =
        visibleWindowBounds.bottom - visibleWindowBounds.top - coordinates.boundsInWindow().bottom
    onHeightUpdate(max(heightAbove, heightBelow).toInt())
}

enum class Keyboard {
    Opened, Closed
}

@Composable
fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                Keyboard.Opened
            } else {
                Keyboard.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun <T> UIInfiniteCarousel(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    data: List<T>,
    padding: Int = 3,
    spacing: Int = 3,
    activeIndicator: Color = Color.Black,
    inactiveIndicator: Color = Color.Gray,
    sizeIndicator: Float = 16F,
    content: @Composable (T) -> Unit,
) {
    // We start the pager in the middle of the raw number of pages
    val startIndex = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(initialPage = startIndex)
    Box(contentAlignment = Alignment.BottomCenter) {
        val pageCount = data.size

        HorizontalPager(
            modifier = modifier,
            count = Int.MAX_VALUE,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = padding.dp),
            itemSpacing = spacing.dp,

            ) { index ->
            val page = (index - startIndex).floorMod(pageCount)
            // Our page content
            content(data[page])
        }
        Row(modifier = Modifier.padding(bottom = 20.dp)) {
            (pagerState.currentPage - startIndex).floorMod(pageCount).apply {
                data.forEachIndexed { index, t ->
                    if (this == index) {
                        Canvas(
                            modifier = Modifier.padding(horizontal = (sizeIndicator * 2 / 3).dp),
                            onDraw = {
                                drawCircle(color = activeIndicator, radius = sizeIndicator)
                            })
                    } else {
                        Canvas(
                            modifier = Modifier.padding(horizontal = (sizeIndicator * 2 / 3).dp),
                            onDraw = {
                                drawCircle(color = inactiveIndicator, radius = sizeIndicator)
                            })
                    }
                }
            }
        }
    }
    val isLooping = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit, block = {
        while (isLooping.value) {
            delay(2000)
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    })
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                isLooping.value = true
            } else if (event == Lifecycle.Event.ON_STOP) {
                isLooping.value = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }

    }

}

private fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}