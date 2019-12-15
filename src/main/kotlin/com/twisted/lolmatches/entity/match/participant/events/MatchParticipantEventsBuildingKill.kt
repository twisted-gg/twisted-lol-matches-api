package com.twisted.lolmatches.entity.match.participant.events

import com.twisted.lolmatches.entity.match.participant.frames.MatchParticipantFramesPosition
import org.bson.types.ObjectId

data class MatchParticipantEventsBuildingKill(
        val timestamp: Long,
        val position: MatchParticipantFramesPosition,
        val assistingIds: List<ObjectId>,
        val buildingType: String,
        val laneType: String
)
