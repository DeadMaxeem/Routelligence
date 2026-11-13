package com.routelligence.app.engine

import androidx.compose.ui.graphics.Color
import com.routelligence.app.model.RouteOption
import com.routelligence.app.model.RoutePreferenceType
import com.routelligence.app.model.UserProfile

class RouteRecommendationEngine {

    fun rankRoutes(
        routeOptions: List<RouteOption>,
        profile: UserProfile
    ): List<RouteOption> {
        return routeOptions.map { option ->
            val baseScore = option.score
            var adjustedScore = baseScore

            when (profile.routePreferenceType) {
                RoutePreferenceType.FASTEST -> adjustedScore += 12
                RoutePreferenceType.SHORTEST -> adjustedScore += 8
                RoutePreferenceType.SCENIC -> adjustedScore += 10
                RoutePreferenceType.COMFORTABLE -> adjustedScore += 14
            }

            if (profile.avoidTolls) adjustedScore -= 5
            if (profile.avoidHighways) adjustedScore -= 4
            if (profile.preferScenic) adjustedScore += 6
            if (profile.weatherAware) adjustedScore += 2

            option.copy(
                isRecommended = adjustedScore >= 50,
                score = adjustedScore
            )
        }.sortedByDescending { it.score }
    }

    fun buildRecommendationLabel(profile: UserProfile): String {
        return when {
            profile.avoidTolls && profile.avoidHighways -> "Unikaj płatnych odcinków i autostrad"
            profile.preferScenic -> "Preferuj spokojniejsze i bardziej atrakcyjne trasy"
            profile.routePreferenceType == RoutePreferenceType.FASTEST -> "Najbardziej optymalny czas przejazdu"
            else -> "Dostosowanie do zwykłych preferencji użytkownika"
        }
    }
}
