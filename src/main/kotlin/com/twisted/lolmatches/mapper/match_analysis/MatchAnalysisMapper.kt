package com.twisted.lolmatches.mapper.match_analysis

import com.twisted.dto.match.participant.MatchParticipant
import com.twisted.dto.match_analysis.MatchAnalysis
import com.twisted.dto.match_analysis.teams.MatchAnalysisTeams
import com.twisted.dto.match_analysis.teams.MatchAnalysisTeamsParticipants
import com.twisted.dto.match_analysis.teams.MatchAnalysisTeamsStats
import com.twisted.lolmatches.entity.match.MatchDocument

private fun sumStats(a: MatchAnalysisTeamsStats, b: MatchAnalysisTeamsStats) = MatchAnalysisTeamsStats(
        kills = a.kills + b.kills,
        goldEarned = a.goldEarned + b.goldEarned,
        damageToChampions = a.damageToChampions + b.damageToChampions,
        damageTaken = a.damageTaken + b.damageTaken,
        cs = a.cs + b.cs,
        wardsPlaces = a.wardsPlaces + b.wardsPlaces
)

private fun getTeamMapper(participants: List<MatchParticipant>): List<MatchAnalysisTeams> {
  val response = mutableListOf<MatchAnalysisTeams>()
  for (participant in participants) {
    val stats = MatchAnalysisTeamsStats(
            kills = participant.kda.kills,
            goldEarned = participant.stats.goldEarned,
            damageToChampions = participant.stats.totalDamageDealtToChampions,
            damageTaken = participant.stats.totalDamageTaken,
            cs = participant.stats.totalMinionsKilled,
            wardsPlaces = participant.stats.wardsPlaced
    )
    val participantObject = MatchAnalysisTeamsParticipants(
            champion = participant.championId,
            stats = stats
    )
    val team = response.find { t -> t.teamId == participant.teamId }
    if (team == null) {
      response.add(MatchAnalysisTeams(
              teamId = participant.teamId,
              stats = stats,
              participants = mutableListOf(participantObject)
      ))
      continue
    }
    team.stats = sumStats(team.stats, stats)
  }
  return response
}

fun matchAnalysisMapper(match: MatchDocument) = MatchAnalysis(
        teams = getTeamMapper(match.participants
        )
)
