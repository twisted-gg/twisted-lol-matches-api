package com.twisted.lolmatches.match

import com.twisted.lolmatches.entity.match.MatchDocument
import com.twisted.lolmatches.entity.match.team.MatchTeam
import com.twisted.lolmatches.entity.match.team.MatchTeamBans
import com.twisted.lolmatches.entity.match.team.MatchTeamStats
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.TeamStats
import java.util.*

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
 * Match to document
 * Convert match object to database document
 */
fun matchToDocument(match: Match): MatchDocument {
  return MatchDocument(
          region = match.platformId,
          remake = isRemake(match),
          game_id = match.gameId,
          creation = Date(match.gameCreation),
          mode = match.gameMode,
          type = match.gameType,
          version = match.gameVersion,
          map_id = match.mapId,
          queue = match.queueId,
          season = match.seasonId,
          teams = matchTeams(match)
  )
}
