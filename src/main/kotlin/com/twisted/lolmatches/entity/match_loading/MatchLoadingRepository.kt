package com.twisted.lolmatches.entity.match_loading

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MatchLoadingRepository : MongoRepository<MatchLoadingDocument, String> {

  @Query("{ matches: { \$elemMatch: { game_id: ?0, loading: true } }, region: \"?1\" }")
  fun findMatch(game_id: Long, region: String): List<MatchLoadingDocument>

  @Query("{ summoner: ObjectId(?0), \"matches.loading\": true }")
  fun findSummonerLoadingMatches(summoner: String): List<MatchLoadingDocument>
}
