package com.twisted.lolmatches.entity.match

import com.twisted.lolmatches.entity.match.team.MatchTeam
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "lol_matches")
data class MatchDocument(
        // Mongo fields
        @Id
        private val id: String = "",
        // Match details
        private val region: String,
        private val game_id: Long,
        private val creation: Date,
        private val mode: String,
        private val type: String,
        private val version: String,
        private val map_id: Int,
        private val queue: Int,
        private val season: Int,
        private val remake: Boolean,
        private val teams: List<MatchTeam>,
        // Timestamps
        private val createdAt: Date = Date(),
        private val updatedAt: Date = Date()
)

