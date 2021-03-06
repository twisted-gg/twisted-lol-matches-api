package com.twisted.lolmatches.entity.match_loading

import com.twisted.dto.match_loading.IMatchLoadingDocument
import com.twisted.dto.match_loading.MatchLoadingMatches
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "lol_matches_loading")
data class MatchLoadingDocument(
        @Id
        val id: ObjectId? = null,
        override val summoner: ObjectId,
        override val region: String,
        override val matches: List<MatchLoadingMatches>,
        override var healthy: Boolean = true
) : IMatchLoadingDocument
