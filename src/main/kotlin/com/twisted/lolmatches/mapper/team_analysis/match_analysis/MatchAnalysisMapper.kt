package com.twisted.lolmatches.mapper.team_analysis.match_analysis

import com.twisted.dto.team_analysis.match_analysis.MatchAnalysisTeams
import com.twisted.dto.team_analysis.match_analysis.MatchAnalysisTeamsParticipants
import com.twisted.dto.team_analysis.match_analysis.MatchAnalysisTeamsStats
import com.twisted.dto.team_analysis.match_analysis.plusAssign
import com.twisted.lolmatches.entity.match.MatchDocument

fun matchAnalysisMapper(match: MatchDocument): List<MatchAnalysisTeams> {
  val participants = match.participants
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
            stats = stats.copy()
    )
    val team = response.find { t -> t.teamId == participant.teamId }
    // Create if not exists
    if (team == null) {
      response.add(MatchAnalysisTeams(
              teamId = participant.teamId,
              stats = stats,
              participants = mutableListOf(participantObject)
      ))
      continue
    }
    // Update existing
    team.participants.add(participantObject)
    team.stats += stats
  }
  return response
}
