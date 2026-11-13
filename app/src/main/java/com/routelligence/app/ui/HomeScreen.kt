package com.routelligence.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.routelligence.app.model.FavoritePlace
import com.routelligence.app.model.RouteMode
import com.routelligence.app.model.RouteOption
import com.routelligence.app.model.RoutePreferenceType
import com.routelligence.app.model.UserProfile
import com.routelligence.app.ui.theme.RoutelligenceTheme

@Composable
fun HomeScreen(
    profile: UserProfile = UserProfile(),
    favorites: List<FavoritePlace> = listOf(
        FavoritePlace("f1", "Dom", "ul. Wesoła 12, Warszawa", 52.2297, 21.0122, "home"),
        FavoritePlace("f2", "Praca", "ul. Słowiańska 5, Warszawa", 52.2363, 21.0157, "work"),
        FavoritePlace("f3", "Kawiarnia", "ul. Filtrowa 9, Warszawa", 52.2231, 21.0136, "coffee")
    ),
    routeOptions: List<RouteOption> = listOf(
        RouteOption(
            id = "r1",
            title = "Najszybsza trasa",
            summary = "Najkrótszy czas przejazdu, minimalna liczba przestojów.",
            distanceLabel = "12,4 km",
            durationLabel = "19 min",
            etaLabel = "ETA 18:42",
            trafficLevel = "Prawdopodobnie umiarkowany ruch",
            color = Color(0xFF4CAF50),
            isRecommended = true
        ),
        RouteOption(
            id = "r2",
            title = "Trasa bez autostrady",
            summary = "Wybór z uwzględnieniem preferencji użytkownika i pominięciem płatnych odcinków.",
            distanceLabel = "14,1 km",
            durationLabel = "22 min",
            etaLabel = "ETA 18:45",
            trafficLevel = "Mniej ruchu",
            color = Color(0xFF2196F3)
        ),
        RouteOption(
            id = "r3",
            title = "Trasa spokojna",
            summary = "Lepsze warunki dla jazdy w spokojnym tempie, z mniejszą liczbą skrętów i szybkich zmian.",
            distanceLabel = "15,8 km",
            durationLabel = "25 min",
            etaLabel = "ETA 18:49",
            trafficLevel = "Spokojnie",
            color = Color(0xFFFFA000)
        )
    )
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HeaderCard(profile = profile)
            }

            item {
                SectionTitle("Ulubione miejsca")
            }

            items(favorites) { place ->
                FavoritePlaceCard(place)
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                SectionTitle("Proponowane trasy")
            }

            items(routeOptions) { option ->
                RouteOptionCard(option)
            }
        }
    }
}

@Composable
private fun HeaderCard(profile: UserProfile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Routelligence",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "Witaj, ${profile.displayName}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "Tryb: ${profile.preferredMode.name.lowercase()} • Preferencja: ${profile.routePreferenceType.name.lowercase()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 4.dp, top = 8.dp)
    )
}

@Composable
private fun FavoritePlaceCard(place: FavoritePlace) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = place.label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = place.address,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RouteOptionCard(option: RouteOption) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (option.isRecommended) {
                    Text(
                        text = "REKOMENDOWANE",
                        style = MaterialTheme.typography.labelSmall,
                        color = option.color,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Divider()

            Text(
                text = option.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = option.distanceLabel)
                Text(text = option.durationLabel)
                Text(text = option.etaLabel)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = option.color.copy(alpha = 0.12f),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(vertical = 8.dp, horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = option.trafficLevel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "•",
                    color = option.color,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (option.isRecommended) "Najlepszy dobór" else "Alternatywa",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RoutelligenceTheme {
        HomeScreen()
    }
}
