package com.twisted.lolmatches.entity.match.participant

import com.twisted.lolmatches.entity.match.participant.events.MatchParticipantEvents
import com.twisted.lolmatches.entity.match.participant.frames.MatchParticipantFrames
import com.twisted.lolmatches.entity.match.participant.items.MatchParticipantItems
import com.twisted.lolmatches.entity.match.participant.perks.MatchParticipantPerks
import com.twisted.lolmatches.entity.match.participant.stats.MatchParticipantKDA
import com.twisted.lolmatches.entity.match.participant.stats.MatchParticipantStats
import com.twisted.lolmatches.entity.match.participant.timeline.MatchParticipantTimeline

data class MatchParticipant(
        val summoner: MatchParticipantSummoner,
        val championId: Int,
        val spell1Id: Int,
        val spell2Id: Int,
        val teamId: Int,
        val kda: MatchParticipantKDA,
        val stats: MatchParticipantStats,
        val timeline: MatchParticipantTimeline,
        val items: MatchParticipantItems,
        val perks: MatchParticipantPerks,
        val frames: List<MatchParticipantFrames>,
        val events: MatchParticipantEvents
)
