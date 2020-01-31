package com.twisted.lolmatches.mapper.match_participant_frames

import com.twisted.dto.match.participant.MatchParticipant
import com.twisted.dto.match_participant_frames.MatchParticipantFrames
import com.twisted.dto.match_participant_frames.MatchParticipantFramesParticipant
import com.twisted.dto.match_participant_frames.MatchParticipantFramesParticipantFrames
import com.twisted.lolmatches.entity.match.MatchDocument

fun parseParticipantFrames(participant: MatchParticipant): MatchParticipantFramesParticipant {
  val frameEvent = participant.frames.sortedBy { f -> f.frame }
  val frames = mutableListOf<MatchParticipantFramesParticipantFrames>()
  for (frame in frameEvent) {
    frames.add(
            MatchParticipantFramesParticipantFrames(
                    frame = frame.frame,
                    gold = frame.totalGold,
                    exp = frame.xp,
                    cs = frame.totalMinionsKilled
            )
    )
  }
  return MatchParticipantFramesParticipant(
          championId = participant.championId,
          frames = frames
  )
}

fun parseTeamStats(match: MatchDocument, teamId: Int) = match.participants
        .filter { p -> p.teamId == teamId }
        .map { p -> parseParticipantFrames(p) }


fun matchParticipantFramesMapper(match: MatchDocument) = match.teams.map { t ->
  MatchParticipantFrames(
          framesInterval = match.framesInterval,
          teamId = t.teamId,
          participants = parseTeamStats(match, t.teamId)
  )
}