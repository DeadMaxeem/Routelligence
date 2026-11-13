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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.routelligence.app.engine.RouteRecommendationEngine
import com.routelligence.app.model.RouteMode
import com.routelligence.app.model.RouteOption
import com.routelligence.app.model.RoutePreferenceType
import com.routelligence.app.model.UserProfile
import com.routelligence.app.ui.theme.RoutelligenceTheme

@Composable
fun RoutePlannerScreen() {
    val profile = UserProfile()
    val engine = RouteRecommendationEngine()

    val routeOptions = listOf(
        RouteOption(
            id = "1",
            title = "Najszybsza trasa",
            summary = "Najkrótszy czas przejazdu z uwzględnieniem aktualnego ruchu.",
            distanceLabel = "12,4 km",
            durationLabel = "19 min",
            etaLabel = "ETA 18:42",
            trafficLevel = "Ruch umiarkowany",
            color = Color(0xFF4CAF50),
            score = 52
        ),
        RouteOption(
            id = "2",
            title = "Trasa bez autostrady",
            summary = "Lepsza dla użytkownika preferującego kontroli i mniejszy stres.",
            distanceLabel = "14,1 km",
            durationLabel = "22 min",
            etaLabel = "ETA 18:45",
            trafficLevel = "Mniej ruchu",
            color = Color(0xFF2196F3),
            score = 46
        ),
        RouteOption(
            id = "3",
            title = "Trasa spokojna",
            summary = "Bez pośpiechu, komfort podróży i mniejsze ryzyko stresu.",
            distanceLabel = "15,8 km",
            durationLabel = "25 min",
            etaLabel = "ETA 18:49",
            trafficLevel = "Spokojnie",
            color = Color(0xFFFFA000),
            score = 43
        )
    )

    val rankedRoutes = engine.rankRoutes(routeOptions, profile)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Planowanie trasy",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = "ul. Polna 12, Warszawa",
                        onValueChange = {},
                        label = { Text("Punkt startowy") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = "ul. Królewska 5, Warszawa",
                        onValueChange = {},
                        label = { Text("Cel podróży") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Znajdź trasę")
                    }
                }
            }

            Text(
                text = engine.buildRecommendationLabel(profile),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            rankedRoutes.forEach { route ->
                RouteCard(route)
            }
        }
    }
}

@Composable
private fun RouteCard(route: RouteOption) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                    text = route.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (route.isRecommended) {
                    Text(
                        text = "Rekomendowana",
                        style = MaterialTheme.typography.labelMedium,
                        color = route.color,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = route.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = route.distanceLabel)
                Text(text = route.durationLabel)
                Text(text = route.etaLabel)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(route.color.copy(alpha = 0.12f), shape = MaterialTheme.shapes.small)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = route.trafficLevel)
                Text(text = "score: ${route.score}")
            }
        }
    }
}

@Composable
fun PreferencesScreen() {
    var avoidTolls by remember { mutableStateOf(true) }
    var avoidHighways by remember { mutableStateOf(false) }
    var preferScenic by remember { mutableStateOf(false) }
    var weatherAware by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Preferencje trasy",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            PreferenceToggle(
                title = "Unikaj płatnych odcinków",
                checked = avoidTolls,
                onCheckedChange = { avoidTolls = it }
            )

            PreferenceToggle(
                title = "Unikaj autostrad",
                checked = avoidHighways,
                onCheckedChange = { avoidHighways = it }
            )

            PreferenceToggle(
                title = "Preferuj trasy widokowe",
                checked = preferScenic,
                onCheckedChange = { preferScenic = it }
            )

            PreferenceToggle(
                title = "Uwzględniaj pogodę",
                checked = weatherAware,
                onCheckedChange = { weatherAware = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zapisz preferencje")
            }
        }
    }
}

@Composable
private fun PreferenceToggle(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoutePlannerScreenPreview() {
    RoutelligenceTheme {
        RoutePlannerScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreferencesScreenPreview() {
    RoutelligenceTheme {
        PreferencesScreen()
    }
}
