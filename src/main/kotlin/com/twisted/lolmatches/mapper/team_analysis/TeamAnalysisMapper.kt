package com.twisted.lolmatches.mapper.team_analysis

import com.twisted.dto.match.team.MatchTeam
import com.twisted.dto.team_analysis.TeamAnalysis
import com.twisted.lolmatches.entity.match.MatchDocument
import com.twisted.lolmatches.mapper.team_analysis.match_analysis.matchAnalysisMapper
import com.twisted.lolmatches.mapper.team_analysis.participant_build.participantBuildMapper
import com.twisted.lolmatches.mapper.team_analysis.team_frames.teamFramesMapper

private fun getWinnerTeam(teams: List<MatchTeam>): Int {
  var winner = 0
  for (team in teams) {
    if (team.win) {
      winner = team.teamId
      break
    }
  }
  return winner
}

fun teamAnalysisMapper(match: MatchDocument) = TeamAnalysis(
        framesInterval = match.framesInterval,
        teamWinner = getWinnerTeam(match.teams),
        matchAnalysis = matchAnalysisMapper(match),
        teamFrames = teamFramesMapper(match),
        participantsBuild = participantBuildMapper(match)
)