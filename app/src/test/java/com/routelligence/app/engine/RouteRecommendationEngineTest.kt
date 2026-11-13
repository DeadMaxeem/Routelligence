package com.routelligence.app.engine

import androidx.compose.ui.graphics.Color
import com.routelligence.app.model.RouteOption
import com.routelligence.app.model.RoutePreferenceType
import com.routelligence.app.model.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RouteRecommendationEngineTest {
    private val engine = RouteRecommendationEngine()

    private val candidates = listOf(
        RouteOption("quick", "Quick", "", "10 km", "10 min", "", "", Color.Green, durationMinutes = 10, distanceKm = 10.0),
        RouteOption("long", "Long", "", "20 km", "20 min", "", "", Color.Blue, durationMinutes = 20, distanceKm = 20.0, tollRoad = true)
    )

    @Test
    fun fastestPreferenceRanksQuickerRouteFirst() {
        val ranked = engine.rankRoutes(candidates, UserProfile(avoidTolls = false))
        assertEquals("quick", ranked.first().id)
        assertEquals(1, ranked.count { it.isRecommended })
    }

    @Test
    fun tollAvoidancePenalizesOnlyRoutesWithTolls() {
        val ranked = engine.rankRoutes(candidates, UserProfile(avoidTolls = true))
        assertTrue(ranked.first { it.id == "long" }.score < ranked.first { it.id == "quick" }.score)
    }

    @Test
    fun emptyCandidateListReturnsEmptyList() {
        assertTrue(engine.rankRoutes(emptyList(), UserProfile()).isEmpty())
    }
}
