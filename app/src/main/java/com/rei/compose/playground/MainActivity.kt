package com.rei.compose.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.rei.compose.playground.ui.compose.*
import com.rei.compose.playground.ui.theme.ComposePlayGroundTheme
import com.skydoves.landscapist.glide.GlideImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlayGroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var target = remember {
                        mutableStateMapOf<Int, UITutorialPosition>()
                    }
                    var combo = remember { mutableStateOf("") }
                    var text = remember { mutableStateOf("") }
                    var otp = remember { mutableStateOf("") }
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .verticalScroll(
                                rememberScrollState()
                            )
                    ) {
                        UIInput(
                            modifier = Modifier.onGloballyPositioned {
                                target[3] = UITutorialPosition(
                                    UITutorialType.SQUARE,
                                    it,
                                    "Selamat pagi jangan lupa tidur",
                                    "Kerja lagi tetapi asik",
                                    Color.Red,
                                    Color.White
                                )
                            },
                            onValueChange = { text.value = it },
                            value = text.value,
                            label = "Input",
                            error = "Hihih"
                        )
                        UIComboBox(
                            modifier = Modifier,
                            label = "Hohoho",
                            options = listOf(
                                ComboBoxData("1", "1"),
                                ComboBoxData("2", "2"),
                                ComboBoxData("3", "3"),
                                ComboBoxData("4", "4"),
                                ComboBoxData("5", "5"),
                                ComboBoxData("6", "6"),
                                ComboBoxData("7", "7"),
                                ComboBoxData("8", "8"),
                                ComboBoxData("9", "9"),
                                ComboBoxData("10", "10"),
                                ComboBoxData("11", "11"),
                                ComboBoxData("12", "12"),
                                ComboBoxData("13", "13"),
                                ComboBoxData("14", "14"),
                                ComboBoxData("15", "15"),
                                ComboBoxData("16", "16"),
                                ComboBoxData("17", "17"),
                                ComboBoxData("18", "18")
                            ),
                            onValueChange = {
                                combo.value = it
                            },
                            value = combo.value
                        )
                        UIPassword(
                            modifier = Modifier,
                            label = "Password",
                            value = text.value,
                            error = "Salah dong password"
                        ) { text.value = it }
                        UIDescriptiveLink(
                            modifier = Modifier.onGloballyPositioned {
                                target[2] = UITutorialPosition(
                                    UITutorialType.SQUARE,
                                    it,
                                    "Selamat pagi jangan lupa tidur",
                                    "Kerja lagi tetapi asik",
                                    Color.Red,
                                    Color.White
                                )
                            },
                            description = "Click here to",
                            url = "Reset password"
                        ) {
                            text.value = "clicked"
                        }
                        UIOTP(
                            modifier = Modifier.onGloballyPositioned {
                                target[1] = UITutorialPosition(
                                    UITutorialType.SQUARE,
                                    it,
                                    "Selamat pagi jangan lupa tidur",
                                    "Kerja lagi tetapi asik",
                                    Color.Red,
                                    Color.White
                                )
                            },
                            value = otp.value,
                            otp = { otp.value = it },
                            submit = { otp.value = it })
                        FloatingActionButton(
                            onClick = {},
                            modifier = Modifier.onGloballyPositioned {
                                target[0] = UITutorialPosition(
                                    UITutorialType.CIRCLE,
                                    it,
                                    "Selamat pagi jangan lupa tidur",
                                    "Kerja lagi tetapi asik",
                                    Color.Red,
                                    Color.White
                                )
                            }
                        ) {

                        }
                        UIInfiniteCarousel(
                            Modifier,
                            data = listOf(
                                "https://m.media-amazon.com/images/I/61exwJF3YPL._AC_SL1500_.jpg",
                                "https://m.media-amazon.com/images/I/61a8EZ2c8rL._AC_SX679_.jpg",
                                "https://images-na.ssl-images-amazon.com/images/I/614E-BXGotS.__AC_SX300_SY300_QL70_ML2_.jpg"
                            ),
                            padding = 20,
                            spacing = 10
                        ) { url ->
                            GlideImage(
                                imageModel = url,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(CircleShape)                       // clip to the circle shape
                                    .border(2.dp, Color.Gray, CircleShape),
                                contentScale = ContentScale.Fit,
                                loading = {
                                    Box(
                                        Modifier.matchParentSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                },
                            )
//                            SubcomposeAsyncImage(
//                                modifier = Modifier
//                                    .size(200.dp)
//                                    .clip(CircleShape)                       // clip to the circle shape
//                                    .border(2.dp, Color.Gray, CircleShape),
//                                model = url,
//                                loading = {
//                                    CircularProgressIndicator()
//                                },
//                                contentDescription = stringResource(R.string.description)
//                            )
//                            val painter = rememberAsyncImagePainter(
//                                model = ImageRequest.Builder(LocalContext.current)
//                                    .data(url)
//                                    .scale(Scale.FIT)
//                                    .build()
//                            )
//
//                            if (painter.state is AsyncImagePainter.State.Success) {
//                                // This will be executed during the first composition if the image is in the memory cache.
//                            }
//
//                            Image(
//                                modifier = Modifier
//                                    .size(200.dp)
//                                    .clip(CircleShape)                       // clip to the circle shape
//                                    .border(2.dp, Color.Gray, CircleShape),
//                                painter = painter,
//                                contentDescription = stringResource(R.string.description)
//                            )
                        }
                    }

                    UITutorials(target, MaterialTheme.colorScheme.primary) {

                    }
                }
            }
        }
    }

}