package com.example.careertree.view.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.careertree.adapter.GameEngineContentAdapter
import com.example.careertree.databinding.FragmentGameEngineContentBinding
import com.example.careertree.repository.GameEngineQueryRepository
import com.example.careertree.viewmodel.game.GameEngineContentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GameEngineContentFragment : Fragment() {

    @Inject
    lateinit var gameEngineQueryRepository: GameEngineQueryRepository

    private var _binding: FragmentGameEngineContentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: GameEngineContentViewModel

    private var gameEngineAdapter = GameEngineContentAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGameEngineContentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(GameEngineContentViewModel::class.java)
        viewModel.getData()

        binding.recyclerViewContent.layoutManager = GridLayoutManager(requireView().context,2)

        observeLiveData()
    }

    fun observeLiveData(){

        viewModel.salaryList.observe(viewLifecycleOwner, Observer { salary ->

            salary?.let {

                if(salary.data != null){

                    binding.personalSalaryCount.text = it.data?.get(0)!!.personalSalaryCount
                    binding.priceMinSalary.text = it.data?.get(1)!!.price
                    binding.priceMaxSalary.text = it.data?.get(2)!!.price
                    binding.priceAverageSalary.text = it.data?.get(3)!!.price
                }
            }
        })
        viewModel.gameEngineList.observe(viewLifecycleOwner, Observer { gameEngine ->

            gameEngine?.let {

                if(gameEngine.data != null){

                    binding.recyclerViewContent.visibility = View.VISIBLE

                    gameEngineAdapter = GameEngineContentAdapter(ArrayList(it.data))
                    binding.recyclerViewContent.adapter = gameEngineAdapter
                    gameEngineAdapter.gameEngineQueryRepository = gameEngineQueryRepository

                }
            }
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {

            it?.let {

                if(it.data == true){
                    binding.recyclerViewContent.visibility = View.GONE
                    binding.errorText.visibility = View.VISIBLE
                }
                else{

                    binding.errorText.visibility = View.INVISIBLE
                }
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {

            it?.let {

                if(it.data == true){
                    binding.recyclerViewContent.visibility = View.GONE
                    binding.errorText.visibility = View.GONE
                    binding.loadProgressBar.visibility = View.VISIBLE
                }
                else{
                    binding.loadProgressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}