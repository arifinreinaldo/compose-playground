package com.rei.compose.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.rei.compose.playground.ui.compose.*
import com.rei.compose.playground.ui.theme.ComposePlayGroundTheme

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
                    Column(modifier = Modifier.padding(horizontal = 5.dp)) {
                        UIInput(
                            modifier = Modifier,
                            onValueChange = { text.value = it },
                            value = text.value,
                            label = "Input"
                        )
                        UIComboBox(
                            modifier = Modifier,
                            label = "Hohoho",
                            options = listOf(
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara")
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
                            onValueChange = { text.value = it })
                        UIDescriptiveLink(
                            modifier = Modifier.onGloballyPositioned {
                                target[2] = UITutorialPosition(
                                    2,
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
                                    1,
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
                                    0,
                                    it,
                                    "Selamat pagi jangan lupa tidur",
                                    "Kerja lagi tetapi asik",
                                    Color.Red,
                                    Color.White
                                )
                            }
                        ) {

                        }
                    }

                    UITutorials(target, MaterialTheme.colorScheme.primary) {

                    }
                }
            }
        }
    }

}