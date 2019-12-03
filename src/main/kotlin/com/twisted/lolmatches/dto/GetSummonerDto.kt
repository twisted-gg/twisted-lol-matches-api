package com.twisted.lolmatches.dto

enum class ListRegions {
  LA1
}

data class GetSummonerDto(
        val summonerName: String,
        val region: ListRegions
)
