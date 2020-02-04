package com.twisted.lolmatches.mapper.team_graph

import com.twisted.dto.match.participant.MatchParticipant
import com.twisted.dto.team_graph.TeamGraph
import com.twisted.dto.team_graph.TeamGraphParticipant
import com.twisted.lolmatches.entity.match.MatchDocument

private fun mapperParticipant(participant: MatchParticipant) = TeamGraphParticipant(
        championId = participant.championId,
        goldEarned = participant.stats.goldEarned,
        goldSpent = participant.stats.goldSpent,
        totalDamageDealtToChampions = participant.stats.totalDamageDealtToChampions,
        physicalDamageDealtToChampions = participant.stats.physicalDamageDealtToChampions,
        magicDamageDealtToChampions = participant.stats.magicDamageDealtToChampions,
        trueDamageDealtToChampions = participant.stats.trueDamageDealtToChampions,
        physicalDamageDealt = participant.stats.physicalDamageDealt,
        magicDamageDealt = participant.stats.magicDamageDealt,
        trueDamageDealt = participant.stats.trueDamageDealt,
        damageDealtToTurrets = participant.stats.damageDealtToTurrets,
        damageDealtToObjectives = participant.stats.damageDealtToObjectives,
        totalHeal = participant.stats.totalHeal,
        totalDamageTaken = participant.stats.totalDamageTaken,
        physicalDamageTaken = participant.stats.physicalDamageTaken,
        magicalDamageTaken = participant.stats.magicalDamageTaken,
        trueDamageTaken = participant.stats.trueDamageTaken,
        damageSelfMitigated = participant.stats.damageSelfMitigated,
        visionScore = participant.stats.visionScore,
        wardsPlaced = participant.stats.wardsPlaced,
        wardsKilled = participant.stats.wardsKilled,
        visionWardsBoughtInGame = participant.stats.visionWardsBoughtInGame,
        inhibitorKills = participant.stats.inhibitorKills,
        killingSprees = participant.stats.killingSprees,
        doubleKills = participant.stats.doubleKills,
        tripleKills = participant.stats.tripleKills,
        quadraKills = participant.stats.quadraKills,
        pentaKills = participant.stats.pentaKills
)

fun teamGraphMapper(match: MatchDocument): List<TeamGraph> {
  val response = mutableListOf<TeamGraph>()
  for (team in match.teams) {
    val participants = mutableListOf<TeamGraphParticipant>()
    val participantsFiltered = match.participants.filter { p -> p.teamId == team.teamId }
    for (participant in participantsFiltered) {
      participants.add(mapperParticipant(participant))
    }
    response.add(TeamGraph(team.teamId, participants))
  }
  return response
}