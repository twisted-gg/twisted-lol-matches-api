package com.twisted.lolmatches.entity.match.participant.frames

data class MatchParticipantFrames(
        val position: MatchParticipantFramesPosition,
        val frame: Byte,
        val currentGold: Int,
        val totalGold: Int,
        val level: Int,
        val xp: Int,
        val totalMinionsKilled: Int,
        val minionsKilled: Int,
        val jungleMinionsKilled: Int,
        val teamScore: Int
)
