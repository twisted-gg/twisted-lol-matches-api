package com.twisted.lolmatches.entity.match.participant

data class MatchParticipant(
        val summoner: MatchParticipantSummoner,
        val championId: Int,
        val spell1Id: Int,
        val spell2Id: Int,
        val teamId: Int,
        val kda: MatchParticipantKDA,
        val stats: MatchParticipantStats,
        val stats_timeline: MatchParticipantTimeline,
        val items: MatchParticipantItems,
        val perks: MatchParticipantPerks
)