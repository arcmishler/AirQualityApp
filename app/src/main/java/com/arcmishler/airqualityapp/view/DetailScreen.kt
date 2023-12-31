package com.arcmishler.airqualityapp.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arcmishler.airqualityapp.R
import com.arcmishler.airqualityapp.model.Pollutant
import com.arcmishler.airqualityapp.ui.theme.DimBlue
import com.arcmishler.airqualityapp.viewmodel.AirQualityViewModel

@Composable
fun DetailScreen(viewModel: AirQualityViewModel) {
    val pollutants by viewModel.pollutantList.collectAsState()
    val gcData by viewModel.geoCodeData.collectAsState()
    val aqi by viewModel.aqiData.collectAsState()

    Box(modifier = Modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.city_background_4),
            contentDescription = "Location",
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (pollutants == null) {
                EmptyAirQuality()
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    // Display all AQI information
                    LocationHeader(gcData?.name, aqi)
                    PollutantGrid(pollutants!!)
                }
            }
        }
    }
}

@Composable
fun PollutantGrid(pollutants: List<Pollutant>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(top = 10.dp, start = 0.dp, end = 0.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy((-25).dp),
        verticalArrangement = Arrangement.spacedBy((-5).dp)
    ) {
        items(pollutants) { pollutant ->
            AirQualityCard(pollutant)
        }
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
        colors = CardDefaults.cardColors(containerColor = DimBlue),
        border = BorderStroke(5.dp, pollutant.color),
        elevation = CardDefaults.cardElevation(10.dp)) {

        // Stack the pollutant name above the value
        Column(
            modifier = Modifier
                .padding(1.dp)
                .requiredHeight(110.dp)
                .requiredWidth(150.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.aligned(Alignment.CenterVertically)
        ) {
            // Add subtext to pollutants
            Text(fontSize = 22.sp,
                color = Color.White,
                text = buildAnnotatedString {
                    append(pollutant.name)
                    withStyle(subscript) {
                        append(pollutant.subscript)
                    }
                }
            )
            Spacer(Modifier.height(0.dp))
            Text(
                "${pollutant.value}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White)
        }
    }
}