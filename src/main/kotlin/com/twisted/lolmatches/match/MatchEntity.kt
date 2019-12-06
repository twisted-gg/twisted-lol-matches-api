package com.twisted.lolmatches.match

import net.rithms.riot.constant.Platform
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Document(collection = "lol_matches")
data class MatchDocument(
        private val region: Platform,
        private val game_id: Long,
        private val creation: Date,
        private val mode: String,
        private val type: String,
        private val version: String,
        private val map_id: Int,
        private val queue: Int,
        private val season: Int,
        @Id
        private val id: String = ""
)

@Repository
interface MatchRepository : MongoRepository<MatchDocument, Long> {
}
