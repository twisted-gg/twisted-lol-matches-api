package com.twisted.lolmatches.mapper.match_loading

import com.twisted.dto.match_loading.MatchLoadingMatches
import com.twisted.lolmatches.entity.match_loading.MatchLoadingDocument
import net.rithms.riot.api.endpoints.match.dto.MatchReference
import org.bson.types.ObjectId

fun getLoadingPendingMatches(matchList: List<MatchReference>, region: String): List<MatchLoadingMatches> {
  val loadMatches = mutableListOf<MatchLoadingMatches>()
  for (match in matchList) {
    loadMatches.add(MatchLoadingMatches(
            game_id = match.gameId
    ))
  }
  return loadMatches
}

fun mapMatchLoading(id: String, region: String, matches: List<MatchLoadingMatches>) = MatchLoadingDocument(
        summoner = ObjectId(id),
        region = region,
        matches = matches
)