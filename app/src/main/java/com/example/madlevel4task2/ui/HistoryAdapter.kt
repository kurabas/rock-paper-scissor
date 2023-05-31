package com.example.madlevel4task2.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel4task2.R
import com.example.madlevel4task2.databinding.ItemHistoryBinding
import com.example.madlevel4task2.model.Game
import com.example.madlevel4task2.model.GameResult

class HistoryAdapter(private val games: List<Game>):
        RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemHistoryBinding.bind(itemView)

        fun dataBind(game: Game) {
            binding.txtDateTime.text = game.dateTime
            binding.ivComputerMove.setImageResource(
                    GameFragment.getGameMoveImageResource(game.moveComputer)
            )
            binding.ivPlayerMove.setImageResource(
                    GameFragment.getGameMoveImageResource(game.movePlayer)
            )
            binding.txtResult.text = GameResult.values()[game.result].toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
       return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false))
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        holder.dataBind(games[position])
    }
}