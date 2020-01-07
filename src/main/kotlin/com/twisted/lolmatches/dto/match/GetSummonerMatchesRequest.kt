package com.twisted.lolmatches.dto.match

import com.twisted.enum.common.ListRegions

data class GetSummonerMatchesRequest(
        val summonerName: String,
        val region: ListRegions,
        val limit: Int = 0,
        val page: Int = 0
)
