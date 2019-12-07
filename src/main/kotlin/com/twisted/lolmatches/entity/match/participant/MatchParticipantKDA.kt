package com.twisted.lolmatches.entity.match.participant

data class MatchParticipantKDA(
        val kills: Int,
        val assists: Int,
        val deaths: Int,
        val kda: Float
)
