package com.example.madlevel4task2.repository

import android.content.Context
import com.example.madlevel4task2.dao.GameDao
import com.example.madlevel4task2.database.GameRoomDatabase
import com.example.madlevel4task2.model.Game
import com.example.madlevel4task2.model.GameResult

class GameRepository(context: Context) {

    private val gameDao: GameDao

    init {
        val database = GameRoomDatabase.getDatabase(context)
        gameDao = database!!.getGameDao()
    }

    suspend fun getAllGames(): List<Game> {
        return gameDao.getAllGames()
    }

    suspend fun insertGame(game: Game) {
        gameDao.insertGame(game)
    }

    suspend fun deleteAllGames() {
        gameDao.deleteAllGames()
    }

    suspend fun getAllGameWins(): Int {
        return gameDao.getNumberOfGameResults(GameResult.WIN.ordinal)
    }

    suspend fun getAllGameLoses(): Int {
        return gameDao.getNumberOfGameResults(GameResult.LOSE.ordinal)
    }

    suspend fun getAllGameDraws(): Int {
        return gameDao.getNumberOfGameResults(GameResult.DRAW.ordinal)
    }
}