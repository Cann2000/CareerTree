package com.example.careertree.view.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.careertree.adapter.FavoriteListAdapter
import com.example.careertree.databinding.FragmentFavoriteListBinding
import com.example.careertree.viewmodel.favorite.FavoriteListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteListFragment : Fragment() {

    private var _binding:FragmentFavoriteListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FavoriteListViewModel
    private var favoriteListAdapter = FavoriteListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(FavoriteListViewModel::class.java)
        viewModel.getData()

        binding.recyclerViewFavoriteList.layoutManager = GridLayoutManager(requireView().context,2)

        observeLiveData()
    }

    fun observeLiveData(){

        viewModel.favoriteList.observe(viewLifecycleOwner, Observer { favorite ->

            favorite?.let {

                if(favorite.data != null){

                    favoriteListAdapter = FavoriteListAdapter(ArrayList(favorite.data))
                    binding.recyclerViewFavoriteList.adapter = favoriteListAdapter
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}