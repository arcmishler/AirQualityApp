package com.arcmishler.airqualityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arcmishler.airqualityapp.ui.theme.AirQualityAppTheme
import com.arcmishler.airqualityapp.viewmodel.AirQualityViewModel

class MainActivity : ComponentActivity() {
    private val airQualityViewModel: AirQualityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AirQualityAppTheme {
               AirQualityApp(airQualityViewModel)
            }
        }
    }
}

@Composable
fun AirQualityApp(viewModel: AirQualityViewModel) {
    Text(
        "Hello, AirQualityApp!"
    )
}

@Preview(showBackground = true)
@Composable
fun AirQualityAppPreview() {
    AirQualityAppTheme {
        AirQualityApp(viewModel = AirQualityViewModel())
    }
}