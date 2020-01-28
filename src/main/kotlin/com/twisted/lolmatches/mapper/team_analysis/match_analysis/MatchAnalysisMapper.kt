package com.twisted.lolmatches.mapper.team_analysis.match_analysis

import com.twisted.dto.match.participant.MatchParticipant
import com.twisted.dto.team_analysis.match_analysis.MatchAnalysis
import com.twisted.dto.team_analysis.match_analysis.teams.MatchAnalysisTeams
import com.twisted.dto.team_analysis.match_analysis.teams.MatchAnalysisTeamsParticipants
import com.twisted.dto.team_analysis.match_analysis.teams.MatchAnalysisTeamsStats
import com.twisted.dto.team_analysis.match_analysis.teams.plusAssign
import com.twisted.lolmatches.entity.match.MatchDocument

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

fun matchAnalysisMapper(match: MatchDocument) = MatchAnalysis(
        teams = getTeamMapper(match.participants
        )
)
