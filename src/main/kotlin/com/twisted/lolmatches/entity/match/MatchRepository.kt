package com.twisted.lolmatches.entity.match

import org.springframework.data.mongodb.repository.ExistsQuery
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MatchRepository : MongoRepository<MatchDocument, String> {
  @Query("{ game_id: ?0, region: '?1' }")
  fun findByIdAndRegion(gameId: Long, region: String): MatchDocument?

  @Query("{ participantsIds: { \$in: [ObjectId(?0)] } }")
  fun findSummonerMatches(id: String): List<MatchDocument>

  @ExistsQuery("{ game_id: ?0, region: '?1' }")
  fun existsByGameId(gameId: Long, region: String): Boolean
}
