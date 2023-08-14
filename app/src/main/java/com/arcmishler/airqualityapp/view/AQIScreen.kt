package com.arcmishler.airqualityapp.view
import android.media.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arcmishler.airqualityapp.R
import com.arcmishler.airqualityapp.model.AQI
import com.arcmishler.airqualityapp.ui.theme.AirBlue
import com.arcmishler.airqualityapp.ui.theme.AirGreen
import com.arcmishler.airqualityapp.ui.theme.AirMaroon
import com.arcmishler.airqualityapp.ui.theme.AirOrange
import com.arcmishler.airqualityapp.ui.theme.AirPurple
import com.arcmishler.airqualityapp.ui.theme.AirRed
import com.arcmishler.airqualityapp.ui.theme.AirYellow
import com.arcmishler.airqualityapp.viewmodel.AirQualityViewModel

@Composable
fun AQIScreen(viewModel: AirQualityViewModel) {
    val aqi by viewModel.aqiData.collectAsState()
    val gcData by viewModel.geoCodeData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (aqi == null) {
            EmptyAirQuality()
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Display all AQI information
                LocationHeader(gcData?.name, aqi)
                Spacer(modifier = Modifier.height(10.dp))
                AQIDisplay(aqi, viewModel)
                AQIDetails(aqi)
            }
        }
    }
}

@Composable
fun AQIDisplay(aqi: AQI?, viewModel: AirQualityViewModel) {
    Box(modifier = Modifier
        .height(160.dp)
        .offset(y = 10.dp)
        .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AQIColorChart()
        AQIGauge(aqi, viewModel)
        AQIText(aqi?.value.toString())
    }
}

@Composable
fun LocationHeader(location: String?, aqi: AQI?) {
    Card(modifier = Modifier
        .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        elevation = CardDefaults.cardElevation(10.dp),
        border = BorderStroke(5.dp, aqi?.details!!.color)
    ) {
        Column(modifier = Modifier
            .widthIn(max = 300.dp)
            .padding(top = 16.dp, bottom = 16.dp, start = 55.dp, end = 55.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.air_quality_in),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White)
                Text(
                    text = location ?: stringResource(R.string.awaiting_location),
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
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(modifier = Modifier
            .align(Alignment.Center),
            text = stringResource(R.string.no_data_available))
    }
}

@Composable
fun AQIDetails(aqi: AQI?) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        ) {
            Image(
                painterResource(aqi?.details!!.image),
                contentDescription = aqi.details.description
            )
        }
        InformationCard(aqi)
    }
}

@Composable
fun InformationCard(aqi: AQI?) {
        Card(
            modifier = Modifier
                .padding(start = 26.dp, end = 24.dp, top = 8.dp, bottom = 8.dp),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        ) {
            Column () {
                Text(
                    modifier = Modifier
                        .padding(16.dp),
                    text = aqi?.details!!.detail1,
                    color = Color.White
                )
            }
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
        // For each color, create its slice of the meter
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
fun AQIGauge(aqi: AQI?, viewModel: AirQualityViewModel) {
    Canvas(modifier = Modifier
        .size(150.dp), onDraw = {
        val aqiArc = viewModel.calculateAQIGaugeAngle(aqi?.value)
        // Create start and end points for gauge needle
        val pointA = Offset(size.width / 5, size.height / 2)
        val pointB = Offset(0f, size.height / 2)
        // Surrounds the AQI text
        drawCircle(
            radius = 120f,
            color = Color.Black,
            style = Stroke(15f)
        )
        rotate(aqiArc - 45f) {
            // Needle
            drawLine(
                color = Color.Black,
                strokeWidth = 15f,
                cap = StrokeCap.Round,
                start = pointA,
                end = pointB)
        }
    })
}