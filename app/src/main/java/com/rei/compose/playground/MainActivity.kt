package com.rei.compose.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.rei.compose.playground.ui.compose.*
import com.rei.compose.playground.ui.theme.ComposePlayGroundTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
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
                    Column(modifier = Modifier.padding(horizontal = 5.dp)) {
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
                            data = listOf("Habib", "Power", "Stall"),
                            padding = 20,
                            spacing = 10
                        ) { page ->
                            Text(
                                modifier = Modifier
                                    .height(100.dp)
                                    .fillMaxWidth()
                                    .background(Color.Blue),
                                text = "Halaman $page"
                            )
                        }
                    }

                    UITutorials(target, MaterialTheme.colorScheme.primary) {

                    }
                }
            }
        }
    }

}