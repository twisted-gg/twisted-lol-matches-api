package com.twisted.lolmatches.match.mapper

import com.twisted.lolmatches.entity.match.participant.MatchParticipant
import com.twisted.lolmatches.entity.match.participant.MatchParticipantKDA
import com.twisted.lolmatches.entity.match.participant.MatchParticipantSummoner
import com.twisted.lolmatches.summoners.SummonersService
import com.twisted.lolmatches.summoners.dto.GetSummonerDto
import com.twisted.lolmatches.summoners.dto.ListRegions
import com.twisted.lolmatches.summoners.dto.SummonerDto
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.MatchTimeline
import net.rithms.riot.api.endpoints.match.dto.Participant
import net.rithms.riot.api.endpoints.match.dto.ParticipantStats
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

/**
 * Get match participants
 */
fun matchParticipants(match: Match, matchFrames: MatchTimeline): List<MatchParticipant> =
        try {
          val response = mutableListOf<MatchParticipant>()
          for (participant in match.participantIdentities) {
            val params = GetSummonerDto(
                    region = ListRegions.valueOf(match.platformId),
                    summonerName = participant.player.summonerName,
                    accountID = participant.player.currentAccountId
            )
            val summoner = summonersService.getSummoner(params)
            val info = getParticipantDetails(match, participant.participantId)
            response.add(MatchParticipant(
                    summoner = mapSummoner(summoner),
                    championId = info.championId,
                    spell1Id = info.spell1Id,
                    spell2Id = info.spell2Id,
                    teamId = info.teamId,
                    stats = participantStats(info.stats),
                    stats_timeline = participantTimeline(info.timeline),
                    items = participantItems(info.stats),
                    perks = participantPerks(info.stats),
                    kda = participantKDA(info.stats)
            ))
          }
          response
        } catch (e: Exception) {
          mutableListOf()
        }

