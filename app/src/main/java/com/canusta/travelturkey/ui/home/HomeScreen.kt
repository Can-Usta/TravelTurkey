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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.canusta.travelturkey.data.remote.model.City
import com.canusta.travelturkey.ui.component.CustomErrorDialog

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), navController: NavController) {
    val cities by viewModel.cities.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            itemsIndexed(cities) { index, city ->
                CityCard(city = city, navController = navController)

                if (index == cities.lastIndex && cities.isNotEmpty()) {
                    LaunchedEffect(key1 = index) {
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

@Composable
fun CityCard(city: City, navController: NavController) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = { isExpanded = !isExpanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = city.city, style = MaterialTheme.typography.bodyMedium)
                ExpandableIcon(isExpanded = isExpanded) { isExpanded = !isExpanded }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                city.locations.forEach { location ->
                    LocationItem(locationName = location.name, locationId = location.id,navController = navController)
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
            Text(text = locationName, style = MaterialTheme.typography.bodyMedium)
            IconButton(onClick = { isFavorite = !isFavorite }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Favoriden çıkar" else "Favorilere ekle",
                    tint = iconColor
                )
            }
        }
    }
}