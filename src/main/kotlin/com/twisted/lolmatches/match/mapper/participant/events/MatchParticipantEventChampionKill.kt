package com.twisted.lolmatches.match.mapper.participant.events

import com.twisted.lolmatches.contants.match.MatchEventsEnum
import com.twisted.lolmatches.entity.match.participant.events.MatchParticipantEventsChampionKill
import com.twisted.lolmatches.entity.match.participant.frames.MatchParticipantFramesPosition
import com.twisted.lolmatches.summoners.dto.SummonerDto
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.MatchEvent
import org.bson.types.ObjectId

fun isChampionKillEvent(event: MatchEvent): Boolean =
        MatchEventsEnum.CHAMPION_KILL.toString() == event.type

private fun findSummonerByParticipantId(participantId: Int, match: Match, participants: List<SummonerDto>): ObjectId {
  val participantIdentity = match.participantIdentities.find { p -> p.participantId == participantId }
          ?: throw Exception()
  val summoner = participants.find { s -> s.accountId == participantIdentity.player.currentAccountId }
          ?: throw Exception()
  return ObjectId(summoner._id)
}

private fun findTargetId(event: MatchEvent, match: Match, participants: List<SummonerDto>) = findSummonerByParticipantId(event.victimId, match, participants)

private fun findAssistingIds(event: MatchEvent, match: Match, participants: List<SummonerDto>) = event.assistingParticipantIds.map { id -> findSummonerByParticipantId(id, match, participants) }

private fun getPosition(event: MatchEvent) = MatchParticipantFramesPosition(
        x = event.position.x,
        y = event.position.y
)

fun parseChampionKillEvent(event: MatchEvent, match: Match, participants: List<SummonerDto>): MatchParticipantEventsChampionKill {
  val target = findTargetId(event, match, participants)
  val assistingIds = findAssistingIds(event, match, participants)
  val position = getPosition(event)
  return MatchParticipantEventsChampionKill(
          timestamp = event.timestamp,
          position = position,
          target = target,
          assistingIds = assistingIds
  )
}
