package com.twisted.lolmatches.mapper.match_loading

import com.twisted.dto.match_loading.dto.MatchLoadingSummonerStatus
import com.twisted.lolmatches.entity.match_loading.MatchLoadingDocument

private fun getPercentage(totalMatches: Int, totalLoaded: Int): Float {
  if (totalMatches == 0) {
    return 0f
  }
  return (totalLoaded.toFloat() / totalMatches) * 100
}

fun mapSummonerLoadingMatchesStatus(id: String, info: List<MatchLoadingDocument>): MatchLoadingSummonerStatus {
  var isLoading = false
  var totalMatches = 0
  var totalLoaded = 0
  for (instance in info) {
    for (match in instance.matches) {
      totalMatches++
      if (!match.loading) {
        totalLoaded++
      } else {
        isLoading = true
      }
    }
  }
  return MatchLoadingSummonerStatus(
          summoner = id,
          loading = isLoading,
          totalMatches = totalMatches,
          totalLoaded = totalLoaded,
          percentageLoaded = getPercentage(totalMatches, totalLoaded)
  )
}

