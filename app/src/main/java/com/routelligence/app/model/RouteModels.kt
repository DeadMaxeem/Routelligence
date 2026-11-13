package com.routelligence.app.model

enum class RouteMode {
    CAR,
    WALK,
    TRANSIT
}

enum class RoutePreferenceType {
    FASTEST,
    SHORTEST,
    SCENIC,
    COMFORTABLE
}

data class UserProfile(
    val id: String = "user-001",
    val displayName: String = "Michał",
    val email: String = "michal@example.com",
    val preferredMode: RouteMode = RouteMode.CAR,
    val routePreferenceType: RoutePreferenceType = RoutePreferenceType.FASTEST,
    val avoidTolls: Boolean = true,
    val avoidHighways: Boolean = false,
    val preferScenic: Boolean = false,
    val weatherAware: Boolean = true,
    val preferredDepartureTime: String = "08:30"
)

data class FavoritePlace(
    val id: String,
    val label: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val category: String = "favorite"
)

data class RouteOption(
    val id: String,
    val title: String,
    val summary: String,
    val distanceLabel: String,
    val durationLabel: String,
    val etaLabel: String,
    val trafficLevel: String,
    val color: androidx.compose.ui.graphics.Color,
    val isRecommended: Boolean = false,
    val score: Int = 0
)

data class RoutePlanRequest(
    val origin: String,
    val destination: String,
    val routeMode: RouteMode = RouteMode.CAR,
    val routePreferenceType: RoutePreferenceType = RoutePreferenceType.FASTEST,
    val avoidTolls: Boolean = false,
    val avoidHighways: Boolean = false,
    val preferScenic: Boolean = false,
    val weatherAware: Boolean = false
)
