package com.twisted.lolmatches.match

import com.twisted.lolmatches.entity.match.MatchDocument
import com.twisted.lolmatches.entity.match.participant.*
import com.twisted.lolmatches.entity.match.team.MatchTeam
import com.twisted.lolmatches.entity.match.team.MatchTeamBans
import com.twisted.lolmatches.entity.match.team.MatchTeamStats
import com.twisted.lolmatches.summoners.SummonersService
import com.twisted.lolmatches.summoners.dto.GetSummonerDto
import com.twisted.lolmatches.summoners.dto.ListRegions
import com.twisted.lolmatches.summoners.dto.SummonerDto
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.ParticipantStats
import net.rithms.riot.api.endpoints.match.dto.ParticipantTimeline
import net.rithms.riot.api.endpoints.match.dto.TeamStats
import org.bson.types.ObjectId
import java.util.*

private val summonersService = SummonersService()

private fun isWin(value: String): Boolean {
  val condition = "Win"
  return value == condition
}

/**
 * Get team stats
 */
private fun teamStats(team: TeamStats): MatchTeamStats {
  return MatchTeamStats(
          baronKills = team.baronKills,
          dominionVictoryScore = team.dominionVictoryScore,
          dragonKills = team.dragonKills,
          inhibitorKills = team.inhibitorKills,
          riftHeraldKills = team.riftHeraldKills,
          towerKills = team.towerKills,
          vilemawKills = team.vilemawKills,
          firstBaron = team.isFirstBaron,
          firstBlood = team.isFirstBlood,
          firstDragon = team.isFirstDragon,
          firstInhibitor = team.isFirstInhibitor,
          firstRiftHerald = team.isFirstRiftHerald,
          firstTower = team.isFirstTower
  )
}

/**
 * Get team bans
 */
private fun teamBans(team: TeamStats): List<MatchTeamBans> {
  val response = mutableListOf<MatchTeamBans>()
  if (team.bans == null) {
    return response
  }
  for (ban in team.bans) {
    response.add(MatchTeamBans(
            pickTurn = ban.pickTurn,
            champion = ban.championId
    ))
  }
  return response
}

/**
 * Match teams
 */
private fun matchTeams(match: Match): List<MatchTeam> {
  val response = mutableListOf<MatchTeam>()
  for (team in match.teams) {
    val matchItem = MatchTeam(
            teamId = team.teamId,
            win = isWin(team.win),
            stats = teamStats(team),
            bans = teamBans(team)
    )
    response.add(matchItem)
  }
  return response
}

/**
 * IsRemake
 * When both of teams don't have "firstTower" the match was a remake
 */
private fun isRemake(match: Match): Boolean {
  for (team in match.teams) {
    if (team.isFirstTower) {
      return false
    }
  }
  return true
}

/**
 * Participant timeline
 * Convert riot response to required document
 */
private fun participantTimeline(timeline: ParticipantTimeline): MatchParticipantTimeline {
  return MatchParticipantTimeline(
          lane = timeline.lane,
          role = timeline.role,
          csDiffPerMinDeltas = timeline.csDiffPerMinDeltas,
          creepsPerMinDeltas = timeline.creepsPerMinDeltas,
          damageTakenDiffPerMinDeltas = timeline.damageTakenDiffPerMinDeltas,
          damageTakenPerMinDeltas = timeline.damageTakenPerMinDeltas,
          goldPerMinDeltas = timeline.goldPerMinDeltas,
          xpDiffPerMinDeltas = timeline.xpDiffPerMinDeltas,
          xpPerMinDeltas = timeline.xpPerMinDeltas
  )
}

/**
 * Extract participant stats
 */
private fun participantStats(stats: ParticipantStats): MatchParticipantStats {
  return MatchParticipantStats(
          altarsCaptured = stats.altarsCaptured,
          altarsNeutralized = stats.altarsNeutralized,
          champLevel = stats.champLevel,
          combatPlayerScore = stats.combatPlayerScore,
          damageDealtToObjectives = stats.damageDealtToObjectives,
          damageDealtToTurrets = stats.damageDealtToTurrets,
          damageSelfMitigated = stats.damageSelfMitigated,
          doubleKills = stats.doubleKills,
          firstBloodAssist = stats.isFirstBloodAssist,
          firstBloodKill = stats.isFirstBloodKill,
          firstInhibitorAssist = stats.isFirstInhibitorAssist,
          firstInhibitorKill = stats.isFirstInhibitorKill,
          firstTowerAssist = stats.isFirstTowerAssist,
          firstTowerKill = stats.isFirstTowerKill,
          goldEarned = stats.goldEarned,
          goldSpent = stats.goldSpent,
          inhibitorKills = stats.inhibitorKills,
          killingSprees = stats.killingSprees,
          largestCriticalStrike = stats.largestCriticalStrike,
          largestKillingSpree = stats.largestKillingSpree,
          largestMultiKill = stats.largestMultiKill,
          longestTimeSpentLiving = stats.longestTimeSpentLiving,
          magicDamageDealt = stats.magicDamageDealt,
          magicDamageDealtToChampions = stats.magicDamageDealtToChampions,
          magicalDamageTaken = stats.magicalDamageTaken,
          neutralMinionsKilled = stats.neutralMinionsKilled,
          neutralMinionsKilledEnemyJungle = stats.neutralMinionsKilledEnemyJungle,
          neutralMinionsKilledTeamJungle = stats.neutralMinionsKilledTeamJungle,
          nodeCapture = stats.nodeCapture,
          nodeCaptureAssist = stats.nodeCaptureAssist,
          nodeNeutralize = stats.nodeNeutralize,
          nodeNeutralizeAssist = stats.nodeNeutralizeAssist,
          objectivePlayerScore = stats.objectivePlayerScore,
          participantId = stats.participantId,
          pentaKills = stats.pentaKills,
          physicalDamageDealt = stats.physicalDamageDealt,
          physicalDamageDealtToChampions = stats.physicalDamageDealtToChampions,
          physicalDamageTaken = stats.physicalDamageTaken,
          quadraKills = stats.quadraKills,
          sightWardsBoughtInGame = stats.sightWardsBoughtInGame,
          teamObjective = stats.teamObjective,
          timeCCingOthers = stats.timeCCingOthers.toLong(),
          totalDamageDealt = stats.totalDamageDealt,
          totalDamageDealtToChampions = stats.totalDamageDealtToChampions,
          totalDamageTaken = stats.totalDamageTaken,
          totalHeal = stats.totalHeal,
          totalMinionsKilled = stats.totalMinionsKilled,
          totalPlayerScore = stats.totalPlayerScore,
          totalScoreRank = stats.totalScoreRank,
          totalTimeCrowdControlDealt = stats.totalTimeCrowdControlDealt,
          totalUnitsHealed = stats.totalUnitsHealed,
          tripleKills = stats.tripleKills,
          trueDamageDealt = stats.trueDamageDealt,
          trueDamageDealtToChampions = stats.trueDamageDealtToChampions,
          trueDamageTaken = stats.trueDamageTaken,
          turretKills = stats.turretKills,
          unrealKills = stats.unrealKills,
          visionScore = stats.visionScore,
          visionWardsBoughtInGame = stats.visionWardsBoughtInGame,
          wardsKilled = stats.wardsKilled,
          wardsPlaced = stats.wardsPlaced,
          playerScore0 = stats.playerScore0.toInt(),
          playerScore1 = stats.playerScore1.toInt(),
          playerScore2 = stats.playerScore2.toInt(),
          playerScore3 = stats.playerScore3.toInt(),
          playerScore4 = stats.playerScore4.toInt(),
          playerScore5 = stats.playerScore5.toInt(),
          playerScore6 = stats.playerScore6.toInt(),
          playerScore7 = stats.playerScore7.toInt(),
          playerScore8 = stats.playerScore8.toInt(),
          playerScore9 = stats.playerScore9.toInt(),
          perkPrimaryStyle = stats.perkPrimaryStyle,
          perkSubStyle = stats.perkSubStyle,
          statPerk0 = stats.statPerk0,
          statPerk1 = stats.statPerk1,
          statPerk2 = stats.statPerk2
  )
}

/**
 * Participant perks
 */
private fun participantPerks(stats: ParticipantStats): MatchParticipantPerks {
  return MatchParticipantPerks(
          perk0 = stats.perk0,
          perk1 = stats.perk1,
          perk2 = stats.perk2,
          perk3 = stats.perk3,
          perk4 = stats.perk4,
          perk5 = stats.perk5,
          perk0Var1 = stats.perk0Var1.toInt(),
          perk0Var2 = stats.perk0Var2.toInt(),
          perk0Var3 = stats.perk0Var3.toInt(),
          perk1Var1 = stats.perk1Var1.toInt(),
          perk1Var2 = stats.perk1Var2.toInt(),
          perk1Var3 = stats.perk1Var3.toInt(),
          perk2Var1 = stats.perk2Var1.toInt(),
          perk2Var2 = stats.perk2Var2.toInt(),
          perk2Var3 = stats.perk2Var3.toInt(),
          perk3Var1 = stats.perk3Var1.toInt(),
          perk3Var2 = stats.perk3Var2.toInt(),
          perk3Var3 = stats.perk3Var3.toInt(),
          perk4Var1 = stats.perk4Var1.toInt(),
          perk4Var2 = stats.perk4Var2.toInt(),
          perk4Var3 = stats.perk4Var3.toInt(),
          perk5Var1 = stats.perk5Var1.toInt(),
          perk5Var2 = stats.perk5Var2.toInt(),
          perk5Var3 = stats.perk5Var3.toInt()
  )
}

/**
 * Participant items
 */
private fun participantItems(stats: ParticipantStats): MatchParticipantItems {
  return MatchParticipantItems(
          item0 = stats.item0,
          item1 = stats.item1,
          item2 = stats.item2,
          item3 = stats.item3,
          item4 = stats.item4,
          item5 = stats.item5,
          item6 = stats.item6
  )
}

/**
 * Participant KDA
 * Kill + assists / death
 */
private fun participantKDA(stats: ParticipantStats): MatchParticipantKDA {
  val kda = (stats.kills + stats.assists) / stats.deaths.toFloat()
  return MatchParticipantKDA(
          kills = stats.kills,
          assists = stats.assists,
          deaths = stats.deaths,
          kda = kda
  )
}

/**
 * Only save summoner details
 */
private fun mapSummoner(summoner: SummonerDto): MatchParticipantSummoner {
  return MatchParticipantSummoner(
          _id = ObjectId(summoner._id),
          name = summoner.name,
          puuid = summoner.puuid,
          level = summoner.summonerLevel
  )
}

/**
 * Get match participants
 */
private fun getParticipants(match: Match): List<MatchParticipant> {
  return try {
    val response = mutableListOf<MatchParticipant>()
    for (participant in match.participantIdentities) {
      val params = GetSummonerDto(
              region = ListRegions.valueOf(match.platformId),
              summonerName = participant.player.summonerName,
              accountID = participant.player.currentAccountId
      )
      val summoner = summonersService.getSummoner(params)
      val info = match.participants.find { p -> p.participantId == participant.participantId }
              ?: throw Exception()
      response.add(MatchParticipant(
              summoner = mapSummoner(summoner),
              championId = info.championId,
              spell1Id = info.spell1Id,
              spell2Id = info.spell2Id,
              teamId = info.teamId,
              stats = participantStats(info.stats),
              stats_timeline = participantTimeline(info.timeline),
              items = participantItems(info.stats),
              perks = participantPerks(info.stats),
              kda = participantKDA(info.stats)
      ))
    }
    response
  } catch (e: Exception) {
    mutableListOf()
  }
}

/**
 * Match to document
 * Convert match object to database document
 */
fun matchToDocument(match: Match): MatchDocument {
  val participants = getParticipants(match)
  val participantsIds = participants.map { p -> p.summoner._id }
  val badMatch = participants.count() == 0
  return MatchDocument(
          region = match.platformId,
          remake = isRemake(match),
          match_break = badMatch,
          game_id = match.gameId,
          creation = Date(match.gameCreation),
          duration = match.gameDuration,
          mode = match.gameMode,
          type = match.gameType,
          version = match.gameVersion,
          map_id = match.mapId,
          queue = match.queueId,
          season = match.seasonId,
          teams = matchTeams(match),
          participants = participants,
          participantsIds = participantsIds
  )
}
