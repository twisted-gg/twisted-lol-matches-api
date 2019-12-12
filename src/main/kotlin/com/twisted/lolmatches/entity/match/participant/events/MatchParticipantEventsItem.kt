package com.twisted.lolmatches.entity.match.participant.events

data class MatchParticipantEventsItem(
        val type: String,
        val timestamp: Long,
        val itemId: Int
)
