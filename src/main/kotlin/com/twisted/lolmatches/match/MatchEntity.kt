package com.twisted.lolmatches.match

import com.twisted.lolmatches.dto.ListRegions
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Document(collection = "lol_matches")
data class MatchDocument(
        private val region: ListRegions,
        private val game_creation: Long,
        private val game_id: Long,
        @Id
        private val id: String = ""
)

@Repository
interface MatchRepository : MongoRepository<MatchDocument, Long> {
}
