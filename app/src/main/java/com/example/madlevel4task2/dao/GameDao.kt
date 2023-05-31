package com.example.madlevel4task2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.madlevel4task2.model.Game
import com.example.madlevel4task2.model.GameResult

@Dao
interface GameDao {

    @Query("SELECT * FROM gameTable")
    suspend fun getAllGames(): List<Game>

    @Insert
    suspend fun insertGame(game: Game)

    @Query("SELECT COUNT(*) FROM gameTable WHERE result = :gameResult")
    suspend fun getNumberOfGameResults(gameResult: Int): Int

    @Query("DELETE FROM gameTable")
    suspend fun deleteAllGames()

}