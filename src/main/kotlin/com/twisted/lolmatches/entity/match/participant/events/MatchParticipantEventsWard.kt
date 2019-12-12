package com.twisted.lolmatches.entity.match.participant.events

data class MatchParticipantEventsWard(
        val type: String,
        val timestamp: Long,
        val wardType: String
)
