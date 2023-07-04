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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arcmishler.airqualityapp.api.AirPollutionAPIService
import com.arcmishler.airqualityapp.ui.theme.AirQualityAppTheme
import com.arcmishler.airqualityapp.viewmodel.AirQualityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AirQualityAppTheme {
               AirQualityApp()
            }
        }
    }
}

@Composable
fun AirQualityApp(viewModel: AirQualityViewModel = viewModel()) {
    val data = viewModel.airQualityData.collectAsState()

}

@Composable
fun Hello() {
    Text(
        "Hello, AirQualityApp!"
    )
}
@Preview(showBackground = true)
@Composable
fun HelloPreview() {
    AirQualityAppTheme {
        Hello()
    }
}