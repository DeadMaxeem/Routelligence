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
import androidx.compose.material3.TextButton
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
import com.routelligence.app.model.RouteOption
import com.routelligence.app.model.RoutePreferenceType
import com.routelligence.app.model.UserProfile
import com.routelligence.app.ui.theme.RoutelligenceTheme

@Composable
fun RoutePlannerScreen(profile: UserProfile = UserProfile()) {
    var origin by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var hasSearched by remember { mutableStateOf(false) }
    var validationMessage by remember { mutableStateOf<String?>(null) }

    val sampleRoutes = remember {
        listOf(
            RouteOption(
                id = "fast", title = "Najszybsza trasa",
                summary = "Przykładowa trasa — rzeczywiste dane dojdą po integracji z routingiem.",
                distanceLabel = "12,4 km", durationLabel = "19 min", etaLabel = "ETA —",
                trafficLevel = "Dane testowe", color = Color(0xFF4CAF50),
                distanceKm = 12.4, durationMinutes = 19, highway = true, trafficDelayMinutes = 4
            ),
            RouteOption(
                id = "no-tolls", title = "Bez dróg płatnych",
                summary = "Alternatywa przykładowa z mniejszym udziałem dróg płatnych.",
                distanceLabel = "14,1 km", durationLabel = "22 min", etaLabel = "ETA —",
                trafficLevel = "Dane testowe", color = Color(0xFF2196F3),
                distanceKm = 14.1, durationMinutes = 22, tollRoad = false,
                scenicScore = 30, comfortScore = 65, trafficDelayMinutes = 1
            ),
            RouteOption(
                id = "scenic", title = "Spokojniejsza trasa",
                summary = "Przykładowa opcja oceniona pod kątem komfortu i walorów trasy.",
                distanceLabel = "15,8 km", durationLabel = "25 min", etaLabel = "ETA —",
                trafficLevel = "Dane testowe", color = Color(0xFFFFA000),
                distanceKm = 15.8, durationMinutes = 25, highway = false,
                scenicScore = 82, comfortScore = 88, trafficDelayMinutes = 0
            )
        )
    }
    val rankedRoutes = remember(hasSearched, profile) {
        if (hasSearched) RouteRecommendationEngine().rankRoutes(sampleRoutes, profile) else emptyList()
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Planowanie trasy", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                "To prototyp: trasy poniżej są przykładowe i nie pochodzą z Google Maps.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = origin, onValueChange = { origin = it; validationMessage = null },
                        label = { Text("Punkt startowy") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                    )
                    OutlinedTextField(
                        value = destination, onValueChange = { destination = it; validationMessage = null },
                        label = { Text("Cel podróży") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                    )
                    validationMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    Button(
                        onClick = {
                            if (origin.isBlank() || destination.isBlank()) {
                                validationMessage = "Wpisz punkt startowy i cel podróży."
                                hasSearched = false
                            } else {
                                validationMessage = null
                                hasSearched = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Pokaż przykładowe trasy") }
                }
            }

            if (hasSearched) {
                Text(RouteRecommendationEngine().buildRecommendationLabel(profile), color = MaterialTheme.colorScheme.primary)
                rankedRoutes.forEach { RouteCard(it) }
            }
        }
    }
}

@Composable
private fun RouteCard(route: RouteOption) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(route.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (route.isRecommended) Text("Polecana", color = route.color, fontWeight = FontWeight.Bold)
            }
            Text(route.summary, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(route.distanceLabel)
                Text(route.durationLabel)
                Text("Wynik ${route.score}")
            }
            Row(
                modifier = Modifier.fillMaxWidth().background(route.color.copy(alpha = 0.12f), MaterialTheme.shapes.small).padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(route.trafficLevel)
                Text(if (route.isRecommended) "Najlepsze dopasowanie" else "Alternatywa")
            }
        }
    }
}

@Composable
fun PreferencesScreen(
    profile: UserProfile = UserProfile(),
    onProfileChange: (UserProfile) -> Unit = {}
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Preferencje trasy", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Zmiany są zapisywane lokalnie w stanie aplikacji.", style = MaterialTheme.typography.bodyMedium)
            Text("Priorytet: ${preferenceLabel(profile.routePreferenceType)}", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                RoutePreferenceType.entries.forEach { type ->
                    TextButton(onClick = { onProfileChange(profile.copy(routePreferenceType = type)) }) {
                        Text(preferenceLabel(type), color = if (type == profile.routePreferenceType) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
            PreferenceToggle("Unikaj dróg płatnych", profile.avoidTolls) { onProfileChange(profile.copy(avoidTolls = it)) }
            PreferenceToggle("Unikaj autostrad", profile.avoidHighways) { onProfileChange(profile.copy(avoidHighways = it)) }
            PreferenceToggle("Preferuj trasy widokowe", profile.preferScenic) { onProfileChange(profile.copy(preferScenic = it)) }
            PreferenceToggle("Uwzględniaj pogodę (w przyszłości)", profile.weatherAware) { onProfileChange(profile.copy(weatherAware = it)) }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PreferenceToggle(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

private fun preferenceLabel(type: RoutePreferenceType): String = when (type) {
    RoutePreferenceType.FASTEST -> "Najszybsza"
    RoutePreferenceType.SHORTEST -> "Najkrótsza"
    RoutePreferenceType.SCENIC -> "Widokowa"
    RoutePreferenceType.COMFORTABLE -> "Komfortowa"
}

@Preview(showBackground = true)
@Composable
fun RoutePlannerScreenPreview() { RoutelligenceTheme { RoutePlannerScreen() } }

@Preview(showBackground = true)
@Composable
fun PreferencesScreenPreview() { RoutelligenceTheme { PreferencesScreen() } }
