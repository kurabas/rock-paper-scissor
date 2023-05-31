package com.example.madlevel4task2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madlevel4task2.R
import com.example.madlevel4task2.databinding.FragmentHistoryBinding
import com.example.madlevel4task2.model.Game
import com.example.madlevel4task2.repository.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HistoryFragment : Fragment() {

    private val games = arrayListOf<Game>()
    private val historyAdapter = HistoryAdapter(games)
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private lateinit var gameRepository: GameRepository
    private lateinit var binding: FragmentHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.delete -> {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        gameRepository.deleteAllGames()
                    }
                    getGamesFromDatabase()
                    Toast.makeText(context, R.string.games_deleted, Toast.LENGTH_SHORT).show()
                }
                return false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameRepository = GameRepository(requireContext())

        initRv()
        getGamesFromDatabase()
    }


    private fun initRv() {
        binding.rvHistory.layoutManager = LinearLayoutManager(activity)
        binding.rvHistory.adapter = historyAdapter
        binding.rvHistory.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    }

    //gets the games from the database
    private fun getGamesFromDatabase() {
        mainScope.launch {
                val gamesFromDatabase = withContext(Dispatchers.IO) {
                    gameRepository.getAllGames()
            }

            games.clear()
            games.addAll(gamesFromDatabase)
            historyAdapter.notifyDataSetChanged()
        }
    }
}