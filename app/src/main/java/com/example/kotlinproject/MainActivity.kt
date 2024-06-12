package com.example.kotlinproject

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.NumberPicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.kotlinproject.ui.theme.KotLinProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotLinProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var numberOfRounds by remember { mutableStateOf("5") }
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Enter your name")
        Spacer(modifier = Modifier.height(20.dp))
        TextField(value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(20.dp))

        ValueSection(numberOfRounds.toString(), changeValue = {
            numberOfRounds = it
        })
    }
}

@Composable
fun ValueSection(
    initialValue: String = "2",
    changeValue: (it: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val values = remember { (1..99).map { it.toString() } }
    val valuesPickerState = rememberPickerState()
    Text(text = "Active Duration by Minutes")
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = { /*TODO*/ }) {
            Text(text = "-")
        }
        TextField(
            value = initialValue,
            onValueChange = { changeValue(it) },
            modifier = Modifier
                .width(80.dp),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )



        Button(onClick = { /*TODO*/ }) {
            Text(text = "+")
        }
    }

    Text(text = "Choose number of Rounds :")

    val openMinimalDialog = remember { mutableStateOf(false) }
    Button(onClick = { openMinimalDialog.value = true }) {
        Text(text = "Open")
    }
    Picker(
        state = valuesPickerState,
        items = values,
        visibleItemsCount = 3,
        modifier = modifier
            .width(70.dp),
        textModifier = Modifier.padding(8.dp),
    )
    if (openMinimalDialog.value) {
        Dialog(onDismissRequest = { openMinimalDialog.value = false }) {
            Column(modifier = Modifier.background(Color.White).width(300.dp).height(300.dp)) {
                Picker(
                    state = valuesPickerState,
                    items = values,
                    visibleItemsCount = 7,
                    modifier = modifier
                        .width(70.dp),
                    textModifier = Modifier.padding(8.dp),
                )
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotLinProjectTheme {
        Scaffold {
            Greeting("Android", modifier = Modifier.padding(it))
        }
    }
}