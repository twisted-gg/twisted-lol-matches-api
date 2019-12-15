package com.twisted.lolmatches.entity.match.participant.events

import com.twisted.lolmatches.entity.match.participant.frames.MatchParticipantFramesPosition

data class MatchParticipantEventsEliteMonsterKill(
        val timestamp: Long,
        val position: MatchParticipantFramesPosition,
        val type: String,
        val subType: String?
)
