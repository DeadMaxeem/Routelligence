package com.routelligence.app.engine

import com.routelligence.app.model.RouteOption
import com.routelligence.app.model.RoutePreferenceType
import com.routelligence.app.model.UserProfile
import kotlin.math.max

/**
 * Ranks routes using provider-supplied route attributes and explicit user preferences.
 * This is a deterministic MVP heuristic, not a learned or safety assessment model.
 */
class RouteRecommendationEngine {

    fun rankRoutes(
        routeOptions: List<RouteOption>,
        profile: UserProfile
    ): List<RouteOption> {
        if (routeOptions.isEmpty()) return emptyList()

        val longestDuration = routeOptions.maxOf { it.durationMinutes }
        val longestDistance = routeOptions.maxOf { it.distanceKm }

        val scored = routeOptions.map { route ->
            var score = when (profile.routePreferenceType) {
                RoutePreferenceType.FASTEST -> relativeScore(longestDuration - route.durationMinutes, 2.0)
                RoutePreferenceType.SHORTEST -> relativeScore(longestDistance - route.distanceKm, 4.0)
                RoutePreferenceType.SCENIC -> route.scenicScore.coerceIn(0, 100) * 0.35
                RoutePreferenceType.COMFORTABLE -> route.comfortScore.coerceIn(0, 100) * 0.35
            }

            // Favor the user's secondary preference without letting it dominate the primary one.
            if (profile.preferScenic && profile.routePreferenceType != RoutePreferenceType.SCENIC) {
                score += route.scenicScore.coerceIn(0, 100) * 0.15
            }
            if (profile.avoidTolls && route.tollRoad) score -= 18.0
            if (profile.avoidHighways && route.highway) score -= 12.0
            score -= route.trafficDelayMinutes.coerceAtLeast(0).coerceAtMost(20) * 0.75

            route.copy(score = score.toInt().coerceIn(0, 100), isRecommended = false)
        }.sortedWith(compareByDescending<RouteOption> { it.score }.thenBy { it.durationMinutes })

        return scored.mapIndexed { index, route ->
            route.copy(isRecommended = index == 0)
        }
    }

    fun buildRecommendationLabel(profile: UserProfile): String = when {
        profile.avoidTolls && profile.avoidHighways -> "Priorytet: bez opłat i autostrad"
        profile.avoidTolls -> "Priorytet: unikaj dróg płatnych"
        profile.avoidHighways -> "Priorytet: unikaj autostrad"
        profile.preferScenic || profile.routePreferenceType == RoutePreferenceType.SCENIC ->
            "Priorytet: trasa widokowa"
        profile.routePreferenceType == RoutePreferenceType.FASTEST -> "Priorytet: najkrótszy czas przejazdu"
        profile.routePreferenceType == RoutePreferenceType.SHORTEST -> "Priorytet: najkrótszy dystans"
        else -> "Priorytet: komfort podróży"
    }

    private fun relativeScore(difference: Double, multiplier: Double): Double =
        max(0.0, difference) * multiplier
}
