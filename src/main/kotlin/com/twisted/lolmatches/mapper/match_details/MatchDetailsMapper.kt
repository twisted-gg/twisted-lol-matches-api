package com.twisted.lolmatches.mapper.match_details

import com.twisted.dto.match.participant.MatchParticipant
import com.twisted.dto.match.team.MatchTeamStats
import com.twisted.dto.match_details.MatchDetails
import com.twisted.dto.match_details.teams.MatchDetailsTeams
import com.twisted.dto.match_details.teams.MatchDetailsTeamsStats
import com.twisted.dto.match_details.teams.participant.MatchDetailsTeamsParticipant
import com.twisted.dto.match_details.teams.participant.MatchDetailsTeamsParticipantStats
import com.twisted.lolmatches.entity.match.MatchDocument
import com.twisted.lolmatches.summoners.SummonersService

private val summonersService = SummonersService()

private fun parsePerks(perks: List<Int>) = listOf(perks[0], perks[1])

private fun parseTeamsParticipants(teamId: Int, participants: List<MatchParticipant>): List<MatchDetailsTeamsParticipant> {
  val response = mutableListOf<MatchDetailsTeamsParticipant>()
  val filterParticipants = participants.filter { p -> p.teamId == teamId }
  for (participant in filterParticipants) {
    val name = summonersService.getSummonerName(participant.summoner)
    val stats = MatchDetailsTeamsParticipantStats(
            damage = participant.stats.totalDamageDealtToChampions,
            wards = participant.stats.wardsPlaced,
            cs = participant.stats.totalMinionsKilled
    )
    response.add(
            MatchDetailsTeamsParticipant(
                    name = name,
                    champion = participant.championId,
                    spells = participant.spells,
                    items = participant.items,
                    perks = parsePerks(participant.perks),
                    perkPrimaryStyle = participant.perkPrimaryStyle,
                    perkSubStyle = participant.perkSubStyle,
                    kda = participant.kda,
                    stats = stats
            )
    )
  }
  return response
}

private fun parseTeamStats(stats: MatchTeamStats) = MatchDetailsTeamsStats(
        baronKills = stats.baronKills,
        dragonKills = stats.dragonKills,
        inhibitorKills = stats.inhibitorKills,
        riftHeraldKills = stats.riftHeraldKills,
        towerKills = stats.towerKills
)

private fun parseTeams(match: MatchDocument): List<MatchDetailsTeams> {
  val response = mutableListOf<MatchDetailsTeams>()
  for (team in match.teams) {
    response.add(
            MatchDetailsTeams(
                    teamId = team.teamId,
                    win = team.win,
                    stats = parseTeamStats(team.stats),
                    participants = parseTeamsParticipants(team.teamId, match.participants)
            )
    )
  }
  return response
}

fun matchDetailsMapper(match: MatchDocument) = MatchDetails(
        duration = match.duration,
        teams = parseTeams(match)
)