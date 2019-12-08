package com.twisted.lolmatches.match.mapper

import com.twisted.lolmatches.entity.match.MatchDocument
import com.twisted.lolmatches.entity.match.team.MatchTeam
import com.twisted.lolmatches.entity.match.team.MatchTeamBans
import com.twisted.lolmatches.entity.match.team.MatchTeamStats
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.MatchTimeline
import net.rithms.riot.api.endpoints.match.dto.TeamStats
import java.util.*

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
 * Match to document
 * Convert match object to database document
 */
fun matchToDocument(match: Match, matchFrames: MatchTimeline): MatchDocument {
  val participants = matchParticipants(match, matchFrames)
  val participantsIds = participants.map { p -> p.summoner._id }
  val badMatch = participants.count() == 0
  return MatchDocument(
          region = match.platformId,
          framesInterval = matchFrames.frameInterval,
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
