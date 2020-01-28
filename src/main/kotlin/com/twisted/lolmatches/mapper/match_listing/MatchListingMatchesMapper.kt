package com.twisted.lolmatches.mapper.match_listing

import com.twisted.dto.match.participant.MatchParticipant
import com.twisted.dto.match.team.MatchTeam
import com.twisted.dto.match_details.teams.participant.MatchDetailsTeamsParticipantPerks
import com.twisted.dto.match_listing.matches.MatchListingObject
import com.twisted.dto.match_listing.matches.summoner.MatchListingSummonerObject
import com.twisted.dto.match_listing.matches.summoner.MatchListingSummonerStats
import com.twisted.dto.match_listing.matches.teams.MatchListingTeamObject
import com.twisted.dto.match_listing.matches.teams.MatchListingTeamParticipant
import com.twisted.dto.summoner.SummonerDocument
import com.twisted.enum.common.MapsDto
import com.twisted.enum.common.QueuesDto
import com.twisted.enum.common.SeasonDto
import com.twisted.enum.getMapValueFromKey
import com.twisted.enum.match.MatchGameMode
import com.twisted.enum.match.MatchGameTypes
import com.twisted.lolmatches.entity.match.MatchDocument
import com.twisted.lolmatches.errors.NotFoundException
import com.twisted.lolmatches.riot.RiotService
import com.twisted.lolmatches.summoners.SummonersService

private val summonerService = SummonersService()
private val api = RiotService().getApi()

private fun getSummonerParticipantObject(match: MatchDocument, summoner: SummonerDocument) = match.participants.find { participant -> participant.summoner.toString() == summoner._id }
        ?: throw Exception("Summoner has not found")

private fun getTeam(teamId: Int, match: MatchDocument) = match.teams.find { t -> t.teamId == teamId }
        ?: throw Exception("Team has not found")

private fun isWin(match: MatchDocument, summoner: SummonerDocument): Boolean {
  val participantObject = getSummonerParticipantObject(match, summoner)
  val team = getTeam(participantObject.teamId, match)
  return team.win
}

private fun calculateKillsParticipation(team: MatchTeam, participant: MatchParticipant): Float {
  val participantKills = participant.kda.kills
  if (participantKills == 0) {
    return 0f
  }
  return (participantKills / team.stats.championKills.toFloat()) * 100
}

private fun parseStats(team: MatchTeam, participant: MatchParticipant) = MatchListingSummonerStats(
        champLevel = participant.stats.champLevel,
        minionsKilled = participant.stats.totalMinionsKilled,
        wardsPlaced = participant.stats.wardsPlaced,
        killsParticipation = calculateKillsParticipation(team, participant)
)

private fun parseSummoner(match: MatchDocument, summoner: SummonerDocument): MatchListingSummonerObject {
  val participant = getSummonerParticipantObject(match, summoner)
  val currentTeam = match.teams.find { r -> r.teamId == participant.teamId } ?: throw NotFoundException()
  return MatchListingSummonerObject(
          champion = participant.championId,
          kda = participant.kda,
          items = participant.items,
          stats = parseStats(currentTeam, participant),
          spells = participant.spells,
          perks = MatchDetailsTeamsParticipantPerks(
                  main = participant.perks.first(),
                  subStyle = participant.perkSubStyle
          ),
          team = participant.teamId
  )
}

private fun parseTeams(match: MatchDocument): List<MatchListingTeamObject> {
  val response = mutableListOf<MatchListingTeamObject>()
  for (participant in match.participants) {
    val teamId = participant.teamId
    val currentTeam = response.find { r -> r.teamId == teamId }
    val summonerName = summonerService.getSummonerName(participant.summoner)
    val summonerData = MatchListingTeamParticipant(
            summonerName = summonerName,
            champion = participant.championId
    )
    if (currentTeam != null) {
      currentTeam.participants.add(summonerData)
    } else {
      response.add(
              MatchListingTeamObject(
                      teamId = teamId,
                      participants = mutableListOf(summonerData)
              )
      )
    }
  }
  return response
}

fun mapperMatchListingMatches(summoner: SummonerDocument, matches: List<MatchDocument>): List<MatchListingObject> {
  val response = mutableListOf<MatchListingObject>()
  val defaultValue = "NONE"
  for (match in matches) {
    response.add(
            MatchListingObject(
                    game_id = match.id,
                    remake = match.remake,
                    queue = getMapValueFromKey(QueuesDto, match.queue, defaultValue),
                    mode = getMapValueFromKey(MatchGameMode, match.mode, defaultValue),
                    type = getMapValueFromKey(MatchGameTypes, match.type, defaultValue),
                    map = getMapValueFromKey(MapsDto, match.map_id, defaultValue),
                    season = getMapValueFromKey(SeasonDto, match.season, defaultValue),
                    duration = match.duration,
                    win = isWin(match, summoner),
                    creation = match.creation,
                    summoner = parseSummoner(match, summoner),
                    teams = parseTeams(match)
            )
    )
  }
  return response
}
