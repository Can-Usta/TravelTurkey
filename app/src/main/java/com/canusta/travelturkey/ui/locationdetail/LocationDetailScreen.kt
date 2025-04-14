package com.canusta.travelturkey.ui.locationdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.canusta.travelturkey.R
import com.canusta.travelturkey.data.remote.model.Location
import com.canusta.travelturkey.ui.component.CustomErrorDialog
import com.canusta.travelturkey.ui.theme.PrimaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailScreen(
    locationId: Int?,
    navController: NavController,
    viewModel: LocationDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(locationId) {
        locationId?.let {
            viewModel.loadLocation(it)
        }
    }
    val location by viewModel.location.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    if (!errorMessage.isNullOrEmpty()) {
        CustomErrorDialog(
            message = errorMessage!!,
            onDismiss = { viewModel.clearError() }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(location?.name ?: "Lokasyon Detayı") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryColor)
            )
        }
    ) { innerPadding ->
        location?.let {
            LocationContent(
                location = it,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Lokasyon bilgisi bulunamadı.")
        }
    }
}

@Composable
fun LocationContent(location: Location, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        ImageCard(imageUrl = location.image)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Açıklama",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = location.description,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* İşlevsellik buraya gelecek */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Haritada Göster")
        }
    }
}

@Composable
fun ImageCard(imageUrl: String?) {
    if (imageUrl.isNullOrBlank()) {
        Image(
            painter = painterResource(id = R.drawable.no_image_found),
            contentDescription = "Görsel bulunamadı",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(5.dp, Color.Black)
        )
        return
    }

    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }

            isError -> {
                Image(
                    painter = painterResource(id = R.drawable.no_image_found),
                    contentDescription = "Görsel bulunamadı",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(5.dp, Color.Black)
                )
            }
        }

        AsyncImage(
            model = imageUrl,
            contentDescription = "Lokasyon görseli",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .alpha(if (isLoading || isError) 0f else 1f)
                .border(5.dp, Color.Black),
            onSuccess = {
                isLoading = false
            },
            onError = {
                isLoading = false
                isError = true
            }
        )
    }
}