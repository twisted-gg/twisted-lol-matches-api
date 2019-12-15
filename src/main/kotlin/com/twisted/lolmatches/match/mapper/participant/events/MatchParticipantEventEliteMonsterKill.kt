package com.twisted.lolmatches.match.mapper.participant.events

import com.twisted.lolmatches.contants.match.MatchEventsEnum
import com.twisted.lolmatches.entity.match.participant.events.MatchParticipantEventsEliteMonsterKill
import net.rithms.riot.api.endpoints.match.dto.MatchEvent

fun isEliteMonsterKillEvent(event: MatchEvent): Boolean =
        MatchEventsEnum.ELITE_MONSTER_KILL.toString() == event.type

fun parseEliteMonsterKillEvent(event: MatchEvent) = MatchParticipantEventsEliteMonsterKill(
        timestamp = event.timestamp,
        position = getPosition(event),
        type = event.monsterType,
        subType = event.monsterSubType
)
