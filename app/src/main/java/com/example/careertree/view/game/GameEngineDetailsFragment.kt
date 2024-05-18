package com.example.careertree.view.game

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spanned
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.careertree.adapter.GameListAdapter
import com.example.careertree.databinding.FragmentGameEngineDetailsBinding
import com.example.careertree.viewmodel.game.GameEngineDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameEngineDetailsFragment : Fragment() {

    private var _binding: FragmentGameEngineDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: GameEngineDetailsViewModel

    private var gameListAdapter = GameListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGameEngineDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(GameEngineDetailsViewModel::class.java)

        binding.recyclerViewLibrary.layoutManager = LinearLayoutManager(requireContext())

        arguments?.let {
            val gameEngineId = GameEngineDetailsFragmentArgs.fromBundle(it).id
            viewModel.getRoomData(gameEngineId)
        }

        observeLiveData()
    }

    fun observeLiveData(){

        viewModel.gameEngineLiveData.observe(viewLifecycleOwner, Observer { gameEngine ->

            gameEngine?.let {

                if(gameEngine.data != null){

                    binding.selectedGameEngine = gameEngine.data
                    gameListAdapter = GameListAdapter(ArrayList(gameEngine.data?.projects))

                    binding.recyclerViewLibrary.adapter = gameListAdapter

                    val spanned: Spanned = HtmlCompat.fromHtml(gameEngine.data.features,HtmlCompat.FROM_HTML_MODE_LEGACY)

                    binding.features.text = spanned

                    binding.udemyImage.visibility = View.VISIBLE
                    binding.udemyImage.setOnClickListener{
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(gameEngine.data.videoLink))
                        startActivity(intent)
                    }

                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}