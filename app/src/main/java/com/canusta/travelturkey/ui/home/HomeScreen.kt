package com.canusta.travelturkey.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val cities by viewModel.cities.collectAsState()

    // LazyColumn içerisinde her city için Card oluşturuyoruz.
    LazyColumn {
        itemsIndexed(cities) { index, city ->
            var isExpanded by remember { mutableStateOf(false) }

            // Card için tıklanabilir alan
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    isExpanded = !isExpanded // Tıklanınca genişleme durumunu değiştir
                }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Şehir ismi
                        Text(
                            text = city.city,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        // Ok simgesi
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle",
                            modifier = Modifier.clickable { isExpanded = !isExpanded },
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(8.dp)) // Aralarına boşluk ekle
                        city.locations.forEach { location ->
                            Text(
                                text = location.name,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }

            // Scroll sonuna gelindiğinde bir kez loadMore çağır
            if (index == cities.lastIndex && cities.isNotEmpty()) {
                LaunchedEffect(key1 = index) {
                    viewModel.loadMore()
                }
            }
        }
    }
}