package com.twisted.lolmatches.mapper.team_analysis.team_frames

import com.twisted.dto.team_analysis.team_frames.TeamFramesTeam
import com.twisted.dto.team_analysis.team_frames.TeamFramesTeamFrames
import com.twisted.lolmatches.entity.match.MatchDocument

private fun getFramesFromTeam(teamId: Int, match: MatchDocument): TeamFramesTeam {
  val response = TeamFramesTeam(
          teamId = teamId,
          frames = mutableListOf()
  )
  val teamParticipants = match.participants.filter { p -> p.teamId == teamId }
  for (participant in teamParticipants) {
    for (frame in participant.frames) {
      response.saveFrame(
              TeamFramesTeamFrames(
                      frame = frame.frame,
                      gold = frame.totalGold,
                      minions = frame.totalMinionsKilled,
                      xp = frame.xp

              )
      )
    }
  }
  return response
}

fun teamFramesMapper(match: MatchDocument) = match.teams.map { t -> getFramesFromTeam(t.teamId, match) }
