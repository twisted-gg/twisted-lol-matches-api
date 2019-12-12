package com.twisted.lolmatches.match.mapper.participant.events

import com.twisted.lolmatches.contants.match.MatchEventsEnum
import com.twisted.lolmatches.entity.match.participant.events.MatchParticipantEventsSkillLevelUp
import net.rithms.riot.api.endpoints.match.dto.MatchEvent

fun isSkillLevelUpEvent(event: MatchEvent): Boolean =
        MatchEventsEnum.SKILL_LEVEL_UP.toString() == event.type

fun parseSkillLevelUpEvent(event: MatchEvent) =
        MatchParticipantEventsSkillLevelUp(
                timestamp = event.timestamp,
                skillSlot = event.skillSlot
        )
