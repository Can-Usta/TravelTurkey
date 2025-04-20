package com.canusta.travelturkey.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.canusta.travelturkey.R
import com.canusta.travelturkey.data.remote.model.City
import com.canusta.travelturkey.ui.component.CustomErrorDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), navController: NavController) {
    val cities by viewModel.cities.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val expandedStates by viewModel.expandedStates.collectAsState()

    val anyExpanded = expandedStates.any { it.value }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Şehirler") })
        },
        floatingActionButton = {
            if (anyExpanded) {
                FloatingActionButton(
                    onClick = { viewModel.collapseAll() },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(painter = painterResource(R.drawable.expand_collapse_icon),"")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            LazyColumn {
                itemsIndexed(cities) { index, city ->
                    val isExpanded = expandedStates[index] ?: false

                    CityCard(
                        city = city,
                        navController = navController,
                        isExpanded = isExpanded,
                        onToggleExpand = {
                            viewModel.toggleCard(index)
                        },
                        index = index
                    )

                    if (index == cities.lastIndex && cities.isNotEmpty()) {
                        LaunchedEffect(index) {
                            viewModel.loadMore()
                        }
                    }
                }

                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            if (!errorMessage.isNullOrEmpty()) {
                CustomErrorDialog(
                    message = errorMessage!!,
                    onDismiss = { viewModel.clearError() }
                )
            }
        }
    }
}

@Composable
fun CityCard(
    city: City,
    navController: NavController,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    index : Int
) {
    val hasLocations = city.locations.isNotEmpty()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            if (hasLocations) {
                onToggleExpand()
            }
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (hasLocations) {
                    ExpandableIcon(
                        isExpanded = isExpanded,
                        onToggle = onToggleExpand,
                    )
                }

                Text(
                    text = city.city,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                if(hasLocations){
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Detay",
                        modifier = Modifier
                            .clickable {
                                navController.navigate("city_map/$index")
                            }
                            .padding(start = 8.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

            }

            if (isExpanded && hasLocations) {
                Spacer(modifier = Modifier.height(8.dp))
                city.locations.forEach { location ->
                    LocationItem(
                        locationName = location.name,
                        locationId = location.id,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandableIcon(isExpanded: Boolean, onToggle: () -> Unit) {
    Icon(
        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
        contentDescription = "Toggle expand",
        modifier = Modifier.clickable { onToggle() },
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun LocationItem(
    locationName: String,
    locationId: Int,
    navController: NavController
) {
    var isFavorite by remember { mutableStateOf(false) }

    val iconColor by animateColorAsState(
        targetValue = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        label = "FavoriteColorAnim"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                navController.navigate("location_detail/$locationId")
            },
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = locationName,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(
                onClick = { isFavorite = !isFavorite },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Favoriden çıkar" else "Favorilere ekle",
                    tint = iconColor
                )
            }
        }
    }
}