package com.twisted.lolmatches.mapper.match_listing

import com.twisted.dto.match.participant.stats.MatchParticipantStats
import com.twisted.dto.match_listing.matches.MatchListingObject
import com.twisted.dto.match_listing.matches.summoner.MatchListingSummonerObject
import com.twisted.dto.match_listing.matches.summoner.MatchListingSummonerStats
import com.twisted.dto.match_listing.matches.teams.MatchListingTeamObject
import com.twisted.dto.match_listing.matches.teams.MatchListingTeamParticipant
import com.twisted.dto.summoner.SummonerDocument
import com.twisted.lolmatches.entity.match.MatchDocument
import com.twisted.lolmatches.summoners.SummonersService

private val summonerService = SummonersService()

private fun getSummonerParticipantObject(match: MatchDocument, summoner: SummonerDocument) = match.participants.find { participant -> participant.summoner.toString() == summoner._id }
        ?: throw Exception("Summoner has not found")

private fun getTeam(teamId: Int, match: MatchDocument) = match.teams.find { t -> t.teamId == teamId }
        ?: throw Exception("Team has not found")

private fun isVictory(match: MatchDocument, summoner: SummonerDocument): Boolean {
  val participantObject = getSummonerParticipantObject(match, summoner)
  val team = getTeam(participantObject.teamId, match)
  return team.win
}

private fun parseStats(stats: MatchParticipantStats) = MatchListingSummonerStats(
        champLevel = stats.champLevel,
        minionsKilled = stats.totalMinionsKilled,
        wardsPlaced = stats.wardsPlaced
)

private fun parseSummoner(match: MatchDocument, summoner: SummonerDocument): MatchListingSummonerObject {
  val participant = getSummonerParticipantObject(match, summoner)
  return MatchListingSummonerObject(
          champion = participant.championId,
          kda = participant.kda,
          items = participant.items,
          stats = parseStats(participant.stats),
          spells = participant.spells,
          perks = participant.perks,
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
  for (match in matches) {
    response.add(
            MatchListingObject(
                    game_id = match.id,
                    remake = match.remake,
                    queue = match.queue,
                    mode = match.mode,
                    type = match.type,
                    map_id = match.map_id,
                    season = match.season,
                    duration = match.duration,
                    victory = isVictory(match, summoner),
                    creation = match.creation,
                    summoner = parseSummoner(match, summoner),
                    teams = parseTeams(match)
            )
    )
  }
  return response
}
