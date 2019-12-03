package com.twisted.lolmatches.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SummonerDto(
        val _id: String,
        val name: String,
        val profileIconId: Int,
        val summonerLevel: Int,
        val id: String,
        val puuid: String,
        val accountId: String,
        val loading: Boolean,
        val bot: Boolean,
        val region: String
)
