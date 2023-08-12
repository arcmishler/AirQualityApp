package com.arcmishler.airqualityapp
import com.arcmishler.airqualityapp.ui.theme.*
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arcmishler.airqualityapp.model.GeoCodeResponse
import com.arcmishler.airqualityapp.model.Pollutant
import com.arcmishler.airqualityapp.model.PollutantType
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
    val (searchText, setSearchText) = remember { mutableStateOf("") }
    val pollutants by viewModel.pollutantList.collectAsState()
    val airQuality by viewModel.aqiData.collectAsState()
    val gcData by viewModel.geoCodeData.collectAsState()

    AirScreen(searchText, setSearchText, pollutants, airQuality, gcData, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AirScreen(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    pollutants: List<Pollutant>?,
    airQuality: Int?,
    gcData: GeoCodeResponse?,
    viewModel: AirQualityViewModel
) {
    var active by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = searchText,
                onQueryChange = { onSearchTextChange(it) },
                onSearch = {
                    active = false
                    if (searchText.isNotEmpty()) {
                        // TODO: Input validation
                        viewModel.searchWithGeoCode(searchText)
                    }
                },
                active = active, onActiveChange = { active = it },
                placeholder = {
                    Text(stringResource(R.string.enter_zipcode))
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
            if (pollutants == null) {
                EmptyAirQuality()
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AQIDisplay(airQuality)
                    Spacer(modifier = Modifier.height(4.dp))
                    LocationHeader(gcData?.name)
                    PollutantGrid(pollutants)
                }
            }
        }

        // Place the Bottom Navigation Bar at the bottom of the screen
        var selectedItem by remember { mutableStateOf(0) }
        val screens = listOf("Current", "Historic", "Trends")
        NavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            screens.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = item) },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index }
                )
            }
        }
    }
}

@Composable
fun AQIDisplay(airQuality: Int?) {
    Box(modifier = Modifier
        .height(160.dp)
        .offset(y = 10.dp)
        .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AQIColorChart()
        AQIGauge(airQuality,)
        AQIText(airQuality.toString())
    }
}

@Composable
fun PollutantGrid(pollutants: List<Pollutant>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(top = 10.dp, start = 0.dp, end = 0.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy((-50).dp),
        verticalArrangement = Arrangement.spacedBy((-5).dp)
    ) {
        items(pollutants) { pollutant ->
            AirQualityCard(pollutant)
        }
    }
}

@Composable
fun LocationHeader(location: String?) {
    Card(modifier = Modifier,
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(modifier = Modifier
            .widthIn(max = 300.dp)
            .padding(top = 8.dp, bottom = 8.dp, start = 40.dp, end = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Air quality in...",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White)
            Text(location ?: stringResource(R.string.awaiting_location),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White)
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
            text = stringResource(R.string.no_data_available)
                    )
    }
}

@Composable
fun AQIText(aqi: String) {
    Column(modifier = Modifier
        .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy((-8).dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.aqi), fontSize = 22.sp)
        Text(aqi, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AQIColorChart() {
    val colors: List<Color> = listOf(AirGreen, AirYellow, AirOrange, AirRed, AirPurple, AirMaroon)

    Canvas(modifier = Modifier
        .size(150.dp), onDraw = {
        for ((i, color) in colors.withIndex()) {
            val sweepAngle = 45f
            val startAngle = (135 + (i * sweepAngle))
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(75f)
            )
        }
    })
}

@Composable
fun AQIGauge(aqi: Int?, viewModel: AirQualityViewModel = viewModel()) {
    Canvas(modifier = Modifier
        .size(150.dp), onDraw = {
        val aqiArc = viewModel.calculateAQIGaugeAngle(aqi)
        val pointA = Offset(size.width / 5, size.height / 2)
        val pointB = Offset(0f, size.height / 2)
        drawCircle(
            radius = 120f,
            color = Color.Black,
            style = Stroke(15f)
        )
        rotate(aqiArc - 45f) {
            drawLine(
                color = Color.Black,
                strokeWidth = 15f,
                cap = StrokeCap.Round,
                start = pointA,
                end = pointB)
        }
    })
}

//@Preview
//@Composable
//fun LocationHeaderPreview() {
//    LocationHeader(location = "Salt Lake City")
//}
//
//@Preview
//@Composable
//fun AQIMeterPreview() {
//    val aqi: Int = 150
//    AQIGauge(aqi)
//}
//
//@Preview
//@Composable
//fun AQIPreview() {
//    val aqi: Int = 150
//    AQIColorChart(aqi)
//}
fun getAQIColor(aqi: Int?): Color {
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
fun AirQualityCard(pollutant: Pollutant) {
    val subscript = SpanStyle(
        baselineShift = BaselineShift.None,
        fontSize = 16.sp,
    )
    Card(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize(),
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        border = BorderStroke(5.dp, pollutant.color),
        elevation = CardDefaults.cardElevation(10.dp)) {
        Column(
            modifier = Modifier
                .padding(1.dp)
                .requiredHeight(110.dp)
                .requiredWidth(150.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.aligned(Alignment.CenterVertically)
        ) {
            when (pollutant.type) {
                PollutantType.PM25 -> {
                    Text(fontSize = 22.sp,
                        color = Color.White,
                        text = buildAnnotatedString {
                            append(stringResource(R.string.pm))
                            withStyle(subscript) {
                                append(stringResource(R.string._2_5))
                            }
                        })
                }
                PollutantType.PM10 -> {
                    Text(fontSize = 22.sp,
                        color = Color.White,
                        text = buildAnnotatedString {
                            append(stringResource(R.string.pm))
                            withStyle(subscript) {
                                append(stringResource(R.string._10))
                            }
                        })
                }
                else -> {
                    Text(
                        pollutant.type.toString(),
                        fontSize = 22.sp,
                        color = Color.White
                    )
                }
            }
            Spacer(Modifier.height(0.dp))
            Text(
                "${pollutant.value}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White)
        }
    }
}

fun parseSearch(text: String): List<String> {
    return text.trim().split(",")
}

//@Preview(showBackground = true)
//@Composable
//fun AirQualityCardPreview() {
//    val fakePollutant = Pollutant(
//        PollutantType.PM2_5, 45.toDouble()
//    )
//    AirQualityAppTheme {
//        AirQualityCard(fakePollutant)
//    }
//}