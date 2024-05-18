package com.example.careertree.view.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.careertree.adapter.EventListAdapter
import com.example.careertree.databinding.FragmentFollowedEventListBinding
import com.example.careertree.repository.EventQueryRepository
import com.example.careertree.viewmodel.event.FollowedEventListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FollowedEventListFragment : Fragment() {

    @Inject
    lateinit var eventQueryRepository: EventQueryRepository

    private var _binding: FragmentFollowedEventListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel:FollowedEventListViewModel

    private var adapter = EventListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowedEventListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(FollowedEventListViewModel::class.java)
        viewModel.getData()

        binding.followedEventRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.eventListLiveData.observe(viewLifecycleOwner, Observer { event ->

            event?.let {

                if(event.data != null){

                    binding.followedEventRecyclerView.visibility = View.VISIBLE

                    adapter = EventListAdapter(ArrayList(event.data))
                    binding.followedEventRecyclerView.adapter = adapter
                    adapter.eventQueryRepository = eventQueryRepository

                }
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {

            it?.let {


                if(it.data == true){
                    binding.followedEventRecyclerView.visibility = View.GONE
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