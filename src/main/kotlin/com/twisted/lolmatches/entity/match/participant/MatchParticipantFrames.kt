package com.twisted.lolmatches.entity.match.participant

data class MatchParticipantFrames(
        val position: MatchParticipantFramesPosition,
        val currentGold: Int,
        val totalGold: Int,
        val level: Int,
        val xp: Int,
        val totalMinionsKilled: Int,
        val minionsKilled: Int,
        val jungleMinionsKilled: Int,
        val teamScore: Int
)
