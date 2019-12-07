package com.twisted.lolmatches.entity.match.participant

import com.twisted.lolmatches.dto.SummonerDto

data class MatchParticipant(
        val summoner: SummonerDto,
        val championId: Int,
        val spell1Id: Int,
        val spell2Id: Int,
        val teamId: Int,
        val kda: MatchParticipantKDA,
        val stats: MatchParticipantStats,
        val timeline: MatchParticipantTimeline,
        val items: MatchParticipantItems,
        val perks: MatchParticipantPerks
)
