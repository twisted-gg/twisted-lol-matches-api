package com.twisted.lolmatches.mapper.match.participant

import com.twisted.dto.match.participant.MatchParticipant
import com.twisted.dto.match.participant.MatchParticipantSummoner
import com.twisted.dto.match.participant.events.MatchParticipantEvents
import com.twisted.dto.match.participant.stats.MatchParticipantKDA
import com.twisted.lolmatches.mapper.match.participant.events.matchParticipantEventMapper
import com.twisted.lolmatches.mapper.match.participant.frames.matchParticipantFrames
import com.twisted.lolmatches.mapper.match.participant.items.participantItems
import com.twisted.lolmatches.mapper.match.participant.perks.participantPerks
import com.twisted.lolmatches.mapper.match.participant.stats.participantStats
import com.twisted.lolmatches.summoners.SummonersService
import com.twisted.lolmatches.summoners.dto.GetSummonerDto
import com.twisted.lolmatches.summoners.dto.ListRegions
import com.twisted.lolmatches.summoners.dto.SummonerDto
import kotlinx.coroutines.runBlocking
import net.rithms.riot.api.endpoints.match.dto.*
import org.bson.types.ObjectId

private val summonersService = SummonersService()

/**
 * Participant KDA
 * Kill + assists / death
 */
private fun participantKDA(stats: ParticipantStats): MatchParticipantKDA {
  val kda = (stats.kills + stats.assists) / stats.deaths.toFloat()
  return MatchParticipantKDA(
          kills = stats.kills,
          assists = stats.assists,
          deaths = stats.deaths,
          kda = kda
  )
}

/**
 * Only save summoner details
 */
private fun mapSummoner(summoner: SummonerDto): MatchParticipantSummoner =
        MatchParticipantSummoner(
                _id = ObjectId(summoner._id),
                name = summoner.name,
                puuid = summoner.puuid,
                level = summoner.summonerLevel
        )

/**
 * Get participant details
 */
private fun getParticipantDetails(match: Match, participantId: Int): Participant =
        match.participants.find { p -> p.participantId == participantId }
                ?: throw Exception()

fun getSummonerList(match: Match): List<SummonerDto> {
  val params = mutableListOf<GetSummonerDto>()
  for (participant in match.participantIdentities) {
    params.add(GetSummonerDto(
            region = ListRegions.valueOf(match.platformId),
            summonerName = participant.player.summonerName,
            accountID = participant.player.currentAccountId
    ))
  }
  return runBlocking {
    summonersService.getSummonerList(params)
  }
}

private fun mapInstance(match: Match, matchFrames: MatchTimeline, summoner: SummonerDto, participantId: Int, events: MatchParticipantEvents): MatchParticipant {
  val participant = getParticipantDetails(match = match, participantId = participantId)
  val frames = matchParticipantFrames(
          frames = matchFrames.frames,
          participantId = participantId
  )
  return MatchParticipant(
          summoner = mapSummoner(summoner),
          championId = participant.championId,
          spell1Id = participant.spell1Id,
          spell2Id = participant.spell2Id,
          teamId = participant.teamId,
          stats = participantStats(participant.stats),
          items = participantItems(participant.stats),
          perks = participantPerks(participant.stats),
          kda = participantKDA(participant.stats),
          frames = frames,
          events = events
  )
}

/**
 * Find summoner
 */
fun findSummonerByParticipant(participant: ParticipantIdentity, list: List<SummonerDto>) = list.find { p -> p.accountId == participant.player.currentAccountId }

/**
 * Get match participants
 */
fun matchParticipants(match: Match, matchFrames: MatchTimeline): List<MatchParticipant> =
        try {
          val response = mutableListOf<MatchParticipant>()
          val participantsList = getSummonerList(match)
          for (participant in match.participantIdentities) {
            val summoner = findSummonerByParticipant(participant, participantsList)
                    ?: throw Exception()
            val events = matchParticipantEventMapper(
                    match = match,
                    frames = matchFrames,
                    participantId = participant.participantId,
                    participants = participantsList
            )
            response.add(mapInstance(
                    match = match,
                    matchFrames = matchFrames,
                    summoner = summoner,
                    participantId = participant.participantId,
                    events = events
            ))
          }
          response
        } catch (e: Exception) {
          mutableListOf()
        }
