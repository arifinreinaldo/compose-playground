package com.rei.compose.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rei.compose.playground.ui.compose.ComboBoxData
import com.rei.compose.playground.ui.compose.UIComboBox
import com.rei.compose.playground.ui.compose.UIInput
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
                    Column {
                        UIInput(onValueChange = {}, value = "", label = "")
                        UIInput(onValueChange = {}, value = "", label = "")
                        UIInput(onValueChange = {}, value = "", label = "")
                        UIInput(onValueChange = {}, value = "", label = "")
                        UIInput(onValueChange = {}, value = "", label = "")
                        UIInput(onValueChange = {}, value = "", label = "")
                        UIInput(onValueChange = {}, value = "", label = "")
                        UIComboBox(
                            modifier = Modifier,
                            label = "",
                            options = listOf(
                                ComboBoxData("", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara"),
                                ComboBoxData("gurara", "gurara")
                            ),
                            onValueChange = {},
                            value = "combobo"
                        )
                    }
                }
            }
        }
    }
}

