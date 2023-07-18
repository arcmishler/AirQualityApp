package com.arcmishler.airqualityapp
import com.arcmishler.airqualityapp.ui.theme.*
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
    val no2: Double? = data.value?.components?.get("no2")
    val (searchText, setSearchText) = remember { mutableStateOf("") }

    AirScreen(searchText, setSearchText, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AirScreen(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    viewModel: AirQualityViewModel
) {
    val aqData = viewModel.airQualityData.collectAsState()
    val gcData = viewModel.geoCodeData.collectAsState()
    var active by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = searchText,
            onQueryChange = { onSearchTextChange(it) },
            onSearch = {
                active = false
                if (searchText.isNotEmpty()) {
                    // TODO: Input validation
                    viewModel.fetchGeoCode(searchText)
                }
            },
            active = active, onActiveChange = { active = it },
            placeholder = {
                Text("Enter zipcode")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (active) {
                            active = false
                        }
                    },
                ) {
                    Icon(Icons.Default.Close, "Close button")
                }
            }
        ) {
        }
        LocationHeader(gcData.value?.name)
        val airQuality by viewModel.airQualityData.collectAsState()
        if (airQuality == null) {
            EmptyAirQuality()
        } else {
            AqiIndicator(aqi = airQuality!!.main.aqi)
            AirQualityCard(component = "AQI", value = airQuality!!.main.aqi.toDouble())
            airQuality?.let {
                    data ->
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(data.components.entries.toList()) { (component, value) ->
                        AirQualityCard(component = component, value = value)
                    }
                }
            }
        }
    }
}

@Composable
fun LocationHeader(location: String?) {
    Card(modifier = Modifier
        .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        elevation = CardDefaults.cardElevation(10.dp)) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Air quality in...", style = MaterialTheme.typography.bodySmall)
            Text(location ?: "Awaiting location", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)

        }
    }
}
@Composable
fun EmptyAirQuality() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(modifier = Modifier
            .align(Alignment.Center),
            text = "No air quality data available.\n" +
                "Try searching for your location.")
    }
}

@Composable
fun AqiIndicator(aqi: Int) {
    val aqiColor = getAQIColor(aqi)
    val aqiPercentage = aqi * .001
    CircularProgressIndicator(
        modifier = Modifier
            .size(150.dp)
            .rotate(270f),
        progress = aqiPercentage.toFloat(),
        color = aqiColor,
        strokeWidth = 50.dp)
}

@Preview
@Composable
fun AQIPreview() { 
    val aqi: Int = 37
    AqiIndicator(aqi = aqi)
}
fun getAQIColor(aqi: Int): Color {
    return when (aqi) {
        in 0..50 -> AirGreen
        in 51..100 -> AirYellow
        in 101..150 -> AirOrange
        in 151..200 -> AirRed
        in 201..300 -> AirPurple
        in 301..500 -> AirMaroon
        else -> {Color.Black}
    }
}

@Composable
fun AirQualityCard(component: String, value: Double) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.aligned(Alignment.CenterVertically)
        ) {
            Text(
                component,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text("$value", fontSize = 24.sp)
        }
    }
}

fun parseSearch(text: String): List<String> {
    return text.trim().split(",")
}

@Preview(showBackground = true)
@Composable
fun AirQualityCardPreview() {
    AirQualityAppTheme {
        AirQualityCard("NO2", 10.0)
    }
}