package com.arcmishler.airqualityapp.view
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Storm
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arcmishler.airqualityapp.R
import com.arcmishler.airqualityapp.ui.theme.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirQualityApp(viewModel: AirQualityViewModel = viewModel()
) {
    val tabs = listOf("Air Quality", "Pollutants")
    var selectedIndex by remember { mutableStateOf(0) }
    val (searchText, setSearchText) = remember { mutableStateOf("") }
    var zipError by remember { mutableStateOf(false) }
    var active by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        TabRow(
            selectedTabIndex = selectedIndex,
            containerColor = AirBlue,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    icon = {
                        when (index)  {
                            0 -> Icon(Icons.Default.Air, "Air quality")
                            1 -> Icon(Icons.Outlined.Storm, "Pollutants")
                        }
                    },
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.LightGray
                )
            }
        }

        when (selectedIndex) {
            0 -> AQIScreen(viewModel)
            1 -> DetailScreen(viewModel)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (zipError) {
                // Display error text
                Text(
                    text = stringResource(R.string.zip_error_response),
                    color = Color.Red,
                    fontStyle = FontStyle.Italic
                )
            }

            DockedSearchBar(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = SearchBarDefaults.colors(containerColor = LightBlue),
                query = searchText,
                onQueryChange = { setSearchText(it) },
                onSearch = {
                    active = false
                    if (viewModel.isValidZip(searchText)) {
                        viewModel.searchWithGeoCode(searchText)
                        zipError = false
                    } else {
                        zipError = true
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
        }
    }
}
