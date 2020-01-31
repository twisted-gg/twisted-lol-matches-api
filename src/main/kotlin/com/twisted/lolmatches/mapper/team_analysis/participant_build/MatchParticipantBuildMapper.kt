package com.twisted.lolmatches.mapper.team_analysis.participant_build

import com.twisted.dto.match.participant.MatchParticipant
import com.twisted.dto.team_analysis.participant_build.ParticipantBuildTeams
import com.twisted.dto.team_analysis.participant_build.ParticipantBuildTeamsParticipant
import com.twisted.dto.team_analysis.participant_build.ParticipantBuildTeamsParticipantBuild
import com.twisted.enum.getMapValueFromKey
import com.twisted.enum.match.participants.events.MatchParticipantsEventsType
import com.twisted.lolmatches.entity.match.MatchDocument

private fun mapParticipantBuild(participant: MatchParticipant, match: MatchDocument): Map<Int, List<ParticipantBuildTeamsParticipantBuild>> {
  val response = mutableMapOf<Int, List<ParticipantBuildTeamsParticipantBuild>>()
  val framesInterval = match.framesInterval
  var currentFrame: Long = 0
  var index = 0
  while (currentFrame <= match.duration) {
    val end = currentFrame + framesInterval
    val events = participant.events.item.filter { e ->
      e.timestamp in currentFrame until end
    }
    if (events.count() > 0) {
      response[index++] = events.map { e ->
        ParticipantBuildTeamsParticipantBuild(
                type = getMapValueFromKey(MatchParticipantsEventsType, e.type),
                itemId = e.itemId
        )
      }
    }
    currentFrame += framesInterval
  }
  return response
}

private fun parseSkills(participant: MatchParticipant) = participant
        .events
        .skillLevelUp
        .sortedBy { p -> p.timestamp }
        .map { e -> e.skillSlot }

private fun mapTeamParticipants(teamId: Int, match: MatchDocument) = match
        .participants
        .filter { p -> p.teamId == teamId }
        .map { p ->
          ParticipantBuildTeamsParticipant(
                  championId = p.championId,
                  build = mapParticipantBuild(p, match),
                  perks = p.perks,
                  skills = parseSkills(p)
          )
        }

fun participantBuildMapper(match: MatchDocument) = match.teams.map { t ->
  ParticipantBuildTeams(
          teamId = t.teamId,
          participants = mapTeamParticipants(t.teamId, match)
  )
}
