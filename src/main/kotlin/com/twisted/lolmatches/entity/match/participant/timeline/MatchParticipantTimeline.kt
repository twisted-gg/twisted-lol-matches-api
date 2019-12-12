package com.twisted.lolmatches.entity.match.participant.timeline

data class MatchParticipantTimeline(
        val lane: String,
        val role: String,
        val csDiffPerMinDeltas: Map<String, Double>,
        val goldPerMinDeltas: Map<String, Double>,
        val xpDiffPerMinDeltas: Map<String, Double>,
        val creepsPerMinDeltas: Map<String, Double>,
        val xpPerMinDeltas: Map<String, Double>,
        val damageTakenDiffPerMinDeltas: Map<String, Double>,
        val damageTakenPerMinDeltas: Map<String, Double>
)
