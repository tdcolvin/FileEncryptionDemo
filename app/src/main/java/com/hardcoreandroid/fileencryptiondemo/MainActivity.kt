package com.hardcoreandroid.fileencryptiondemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hardcoreandroid.fileencryptiondemo.ui.theme.FileEncryptionDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FileEncryptionDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    FileEncryptionDemoScreen()
                }
            }
        }
    }
}

@Composable
fun FileEncryptionDemoScreen(viewModel: FileEncryptionDemoViewModel = viewModel()) {
    Column {
        EncryptedSharedPreferencesDemo(viewModel)
        Divider(modifier = Modifier.padding(0.dp, 20.dp), color = Color.Black, thickness = 2.dp)
        EncryptedFileDemo(viewModel)
    }
}

@Composable
fun EncryptedSharedPreferencesDemo(viewModel: FileEncryptionDemoViewModel) {
    var valueRead by remember { mutableStateOf("") }
    Column {
        Button(
            onClick = { viewModel.writeToSharedPrefs("hello") }
        ) {
            Text("Write to encrypted shared prefs")
        }

        Button(
            onClick = { valueRead = viewModel.readFromSharedPrefs() ?: "-" }
        ) {
            Text("Read from encrypted shared prefs")
        }

        Text("Value read: $valueRead")
    }
}

@Composable
fun EncryptedFileDemo(viewModel: FileEncryptionDemoViewModel) {
    var valueRead by remember { mutableStateOf("") }
    Column {
        Button(
            onClick = { viewModel.writeToEncryptedFile("This content is very secure") }
        ) {
            Text("Write to encrypted file")
        }

        Button(
            onClick = { valueRead = viewModel.readFromEncryptedFile() }
        ) {
            Text("Read from encrypted file")
        }

        Text("Value read: $valueRead")
    }
}