package com.example.careertree.view.firebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.careertree.adapter.PersonProfilesAdapter
import com.example.careertree.databinding.FragmentPersonProfilesBinding
import com.example.careertree.viewmodel.profile.PersonProfilesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PersonProfilesFragment : Fragment() {

    private var _binding: FragmentPersonProfilesBinding? = null
    private val binding get() = _binding!!

    private var personProfilesAdapter = PersonProfilesAdapter(arrayListOf())

    private lateinit var viewModel: PersonProfilesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPersonProfilesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(PersonProfilesViewModel::class.java)
        viewModel.getDataFromFirebase()

        binding.personProfilesRecyclerView.setHasFixedSize(true)

        binding.personProfilesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                viewModel.searchViewFilterList(newText, personProfilesAdapter)

                return true
            }

        })

        observeLiveData()
    }

    fun observeLiveData(){

        viewModel.personProfileList.observe(viewLifecycleOwner, Observer { personProfile ->

            personProfile?.let {

                if(personProfile.data != null){

                    binding.personProfilesRecyclerView.visibility = View.VISIBLE

                    personProfilesAdapter = PersonProfilesAdapter(ArrayList(personProfile.data))
                    binding.personProfilesRecyclerView.adapter = personProfilesAdapter

                }
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {

            it?.let {

                if(it.data == true){
                    binding.personProfilesRecyclerView.visibility = View.GONE
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