package com.twisted.lolmatches.entity.match

import com.twisted.lolmatches.entity.match.participant.MatchParticipant
import com.twisted.lolmatches.entity.match.team.MatchTeam
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "lol_matches")
data class MatchDocument(
        // Mongo fields
        @Id
        val id: String = "",
        // Match details
        val region: String,
        val game_id: Long,
        val match_break: Boolean,
        val creation: Date,
        val mode: String,
        val type: String,
        val version: String,
        val map_id: Int,
        val queue: Int,
        val season: Int,
        val remake: Boolean,
        val duration: Long,
        val teams: List<MatchTeam>,
        val participantsIds: List<ObjectId>,
        val participants: List<MatchParticipant>,
        val framesInterval: Long,
        // Timestamps
        val createdAt: Date = Date(),
        val updatedAt: Date = Date()
)

