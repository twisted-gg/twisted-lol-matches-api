package com.twisted.lolmatches.mapper.match_utils

import com.twisted.dto.match.participant.MatchParticipant
import com.twisted.dto.summoner.GetSummonerNameById
import com.twisted.lolmatches.summoners.SummonersService

private val summonerService = SummonersService()

fun getParticipantsNames(participants: List<MatchParticipant>): List<GetSummonerNameById> {
  return summonerService.getMultipleSummonerName(participants.map { m -> m.summoner }).users
}