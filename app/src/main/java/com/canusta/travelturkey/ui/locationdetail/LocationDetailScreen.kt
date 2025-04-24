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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.canusta.travelturkey.R
import com.canusta.travelturkey.data.remote.model.Location
import com.canusta.travelturkey.ui.component.CustomAppBar
import com.canusta.travelturkey.ui.component.CustomErrorDialog
import com.canusta.travelturkey.ui.favorite.FavoriteLocationViewModel
import com.canusta.travelturkey.ui.theme.PrimaryColor
import com.canusta.travelturkey.util.toFavoriteEntity

@Composable
fun LocationDetailScreen(
    locationId: Int?,
    navController: NavController,
    viewModel: LocationDetailViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteLocationViewModel = hiltViewModel()
) {
    LaunchedEffect(locationId) {
        locationId?.let {
            viewModel.loadLocation(it)
        }
    }

    val location by viewModel.location.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(location?.id) {
        location?.id?.let {
            isFavorite = favoriteViewModel.isFavorite(it)
        }
    }

    if (!errorMessage.isNullOrEmpty()) {
        CustomErrorDialog(
            message = errorMessage!!,
            onDismiss = { viewModel.clearError() }
        )
    }

    Scaffold(
        topBar = {
            CustomAppBar(
                title = location?.name.orEmpty(),
                onBack = { navController.popBackStack() },
                actions = {
                    if (location != null) {
                        IconButton(onClick = {
                            isFavorite = !isFavorite
                            val fav = location!!.toFavoriteEntity()
                            favoriteViewModel.toggleFavorite(fav, isFavorite)
                        }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = stringResource(R.string.favorite_text),
                                tint = PrimaryColor
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        location?.let {
            LocationContent(
                location = it,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                navController = navController
            )
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.location_not_found_text))
        }
    }
}

@Composable
fun LocationContent(location: Location, modifier: Modifier = Modifier, navController: NavController) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        ImageCard(imageUrl = location.image)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.description_text),
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
            onClick = {
                navController.navigate("location_map/${location.id}") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.show_on_map_text))
        }
    }
}

@Composable
fun ImageCard(imageUrl: String?) {
    if (imageUrl.isNullOrBlank()) {
        Image(
            painter = painterResource(id = R.drawable.no_image_found),
            contentDescription = stringResource(R.string.image_not_found_text),
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
                    contentDescription = stringResource(R.string.image_not_found_text),
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
            contentDescription = stringResource(R.string.location_image_text),
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