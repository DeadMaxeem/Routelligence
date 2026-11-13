package com.routelligence.app.model

import androidx.compose.ui.graphics.Color

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
    val displayName: String = "Użytkownik",
    val email: String = "",
    val preferredMode: RouteMode = RouteMode.CAR,
    val routePreferenceType: RoutePreferenceType = RoutePreferenceType.FASTEST,
    val avoidTolls: Boolean = true,
    val avoidHighways: Boolean = false,
    val preferScenic: Boolean = false,
    val weatherAware: Boolean = true,
    val preferredDepartureTime: String = "08:30",
    val preferredRouteColor: Color = Color(0xFF2E7D32)
)

data class FavoritePlace(
    val id: String,
    val label: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val category: String = "favorite"
)

/** A route candidate. Numeric attributes are supplied by the routing provider. */
data class RouteOption(
    val id: String,
    val title: String,
    val summary: String,
    val distanceLabel: String,
    val durationLabel: String,
    val etaLabel: String,
    val trafficLevel: String,
    val color: Color,
    val isRecommended: Boolean = false,
    val score: Int = 0,
    val distanceKm: Double = 0.0,
    val durationMinutes: Int = 0,
    val tollRoad: Boolean = false,
    val highway: Boolean = false,
    val scenicScore: Int = 0,
    val comfortScore: Int = 0,
    val trafficDelayMinutes: Int = 0
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
