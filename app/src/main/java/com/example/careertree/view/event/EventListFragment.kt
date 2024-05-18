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
import com.example.careertree.databinding.FragmentEventListBinding
import com.example.careertree.repository.EventQueryRepository
import com.example.careertree.viewmodel.event.EventListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EventListFragment : Fragment() {

    @Inject
    lateinit var eventQueryRepository: EventQueryRepository

    private var _binding: FragmentEventListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel:EventListViewModel

    private var eventListAdapter = EventListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEventListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(EventListViewModel::class.java)
        viewModel.getData()

        binding.eventListRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.eventListLiveData.observe(viewLifecycleOwner, Observer { event ->

            event?.let {

                if(event.data != null){

                    binding.eventListRecyclerView.visibility = View.VISIBLE

                    eventListAdapter = EventListAdapter(ArrayList(event.data))
                    binding.eventListRecyclerView.adapter = eventListAdapter
                    eventListAdapter.eventQueryRepository = eventQueryRepository
                }
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {

            it?.let {

                if(it.data == true){
                    binding.eventListRecyclerView.visibility = View.GONE
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