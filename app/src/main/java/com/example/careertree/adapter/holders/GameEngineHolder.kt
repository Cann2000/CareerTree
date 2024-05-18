package com.example.careertree.adapter.holders

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.databinding.RecyclerRowGameEngineContentBinding
import com.example.careertree.repository.GameEngineQueryRepository
import com.example.careertree.utility.ClickListener
import com.example.careertree.utility.compareClickedUtil
import com.example.careertree.view.game.GameEngineContentFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameEngineHolder(val binding: RecyclerRowGameEngineContentBinding,val gameEngineQueryRepository: GameEngineQueryRepository): RecyclerView.ViewHolder(binding.root),
    ClickListener {

    override fun dataClicked(view: View) {

        val gameEngine = binding.gameEngine

        if(gameEngine != null){

            val action = GameEngineContentFragmentDirections.actionGameEngineContentFragmentToGameEngineDetailsFragment(gameEngine.uuid!!)
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun compareClicked(view: View) {

        CoroutineScope(Dispatchers.Main).launch {

            val gameEngine = binding.gameEngine

            gameEngine?.let {

                binding.compareButton.compareClickedUtil(!gameEngine.compared)

                gameEngineQueryRepository.updateGameEngineToCompare(gameEngine,view)
            }
        }
    }

    override fun starClicked(view: View) {

        CoroutineScope(Dispatchers.Main).launch {

            val gameEngine = binding.gameEngine

            gameEngine?.let {

                //binding.starButton.starClickedUtil(!gameEngine.favorites)

                gameEngineQueryRepository.updateGameEngineToStar(gameEngine)

            }
        }
    }
}