package com.example.careertree.view.web

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.careertree.databinding.FragmentWebContentBinding
import com.example.careertree.viewmodel.web.WebContentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebContentFragment : Fragment() {

    private var _binding: FragmentWebContentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WebContentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWebContentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backend.setOnClickListener { backend() }
        binding.frontend.setOnClickListener { frontend() }

        viewModel = ViewModelProvider(this).get(WebContentViewModel::class.java)
        viewModel.getDataFromInternet()

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
    }
    private fun backend(){

        val action = WebContentFragmentDirections.actionWebContentFragmentToBackendContentFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun frontend(){

        val action = WebContentFragmentDirections.actionWebContentFragmentToFrontendContentFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}