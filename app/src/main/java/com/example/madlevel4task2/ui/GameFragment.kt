package com.example.madlevel4task2.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.madlevel4task2.R
import com.example.madlevel4task2.databinding.FragmentPlayBinding
import com.example.madlevel4task2.model.Game
import com.example.madlevel4task2.model.GameResult
import com.example.madlevel4task2.model.Move
import com.example.madlevel4task2.repository.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime.now

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GameFragment : Fragment() {

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private lateinit var gameRepository: GameRepository
    private lateinit var binding: FragmentPlayBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlayBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameRepository = GameRepository(requireContext())

        updateStats()

        binding.imageRock.setOnClickListener { playGame(Move.ROCK) }
        binding.imagePaper.setOnClickListener { playGame(Move.PAPER) }
        binding.imageScissors.setOnClickListener { playGame(Move.SCISSORS) }
    }

    //It gets the player and computer move
    //It also gets the game result of both players
    @RequiresApi(Build.VERSION_CODES.O)
    private fun playGame(playerMove: Move) {
        Log.d("GAME", "Start")
        val computerMove = Move.values().random()
        val gameResult = getGameResult(playerMove, computerMove)

        val game = Game(
                movePlayer = playerMove.ordinal,
                moveComputer = computerMove.ordinal,
                result = gameResult.ordinal,
                dateTime = now().toString()
        )

        binding.ivPlayerMove.setImageResource(getGameMoveImageResource(playerMove.ordinal))
        binding.ivComputerMove.setImageResource(getGameMoveImageResource(computerMove.ordinal))
        binding.txtResult.text = getGameResultString(gameResult)

        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
            }
        }

        updateStats()
    }


    private fun getGameResult(playerMove: Move, computerMove: Move): GameResult {
        // 3x3 matrix containing win / lose / tie combinations
        val matrix = "TWLLTWWLT"
        // Getting result based of off input
        val result = matrix[computerMove.ordinal * 3 + playerMove.ordinal]

        return when(result) {
            'T' -> GameResult.DRAW
            'W' -> GameResult.WIN
            'L' -> GameResult.LOSE
            else -> GameResult.DRAW
        }
    }

    //It update the stats
    private fun updateStats() {

        mainScope.launch {
            val wins = withContext(Dispatchers.IO) { gameRepository.getAllGameWins()}
            val lose = withContext(Dispatchers.IO) { gameRepository.getAllGameLoses() }
            val draw = withContext(Dispatchers.IO) { gameRepository.getAllGameDraws() }

            binding.txtStats.text = getString(
                    R.string.statistics,
                    wins,
                    lose,
                    draw
            )
        }
    }

    companion object {
         fun getGameMoveImageResource(move: Int): Int {
            return when (move) {
                Move.ROCK.ordinal -> R.drawable.rock
                Move.PAPER.ordinal -> R.drawable.paper
                Move.SCISSORS.ordinal -> R.drawable.scissors
                else -> R.drawable.scissors
            }
        }
    }

    //It shows the game results
    private fun getGameResultString(gameResult: GameResult): String {
        return when (gameResult) {
            GameResult.WIN -> getString(R.string.win)
            GameResult.LOSE -> getString(R.string.lose)
            GameResult.DRAW -> getString(R.string.draw)
        }
    }
}