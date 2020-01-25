package com.twisted.lolmatches.entity.match

import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.CountQuery
import org.springframework.data.mongodb.repository.ExistsQuery
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MatchRepository : MongoRepository<MatchDocument, String> {
  @Query("{ game_id: ?0, region: '?1' }")
  fun findByIdAndRegion(gameId: Long, region: String): MatchDocument?

  @Query("{ participantsIds: { \$in: [ObjectId(?0)] } }")
  fun findSummonerMatches(id: String, pageable: Pageable): List<MatchDocument>

  @CountQuery("{ participantsIds: { \$in: [ObjectId(?0)] } }")
  fun countTotalMatches(id: String): Int

  @ExistsQuery("{ game_id: ?0, region: '?1' }")
  fun existsByGameId(gameId: Long, region: String): Boolean

  @Query("{ _id: ObjectId(?0) }")
  fun findMatch(id: String): MatchDocument?
}
