package com.twisted.lolmatches.entity.match.participant

import org.bson.types.ObjectId

data class MatchParticipantSummoner(
        val _id: ObjectId,
        val name: String,
        val puuid: String,
        val level: Int
)