package com.example.careertree.view.cyber_security

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.careertree.adapter.CyberSecurityContentAdapter
import com.example.careertree.databinding.FragmentCyberSecurityContentBinding
import com.example.careertree.repository.CyberSecurityQueryRepository
import com.example.careertree.viewmodel.cyber_security.CyberSecurityContentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CyberSecurityContentFragment : Fragment() {

    @Inject
    lateinit var cyberSecurityQueryRepository: CyberSecurityQueryRepository

    private var _binding: FragmentCyberSecurityContentBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: CyberSecurityContentViewModel

    private var cyberSecurityContentAdapter = CyberSecurityContentAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCyberSecurityContentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CyberSecurityContentViewModel::class.java)
        viewModel.getData()

        binding.recyclerViewContent.layoutManager = GridLayoutManager(requireView().context,2)

        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.languageList.observe(viewLifecycleOwner, Observer { language ->

            language?.let {

                if(language.data != null){

                    binding.recyclerViewContent.visibility = View.VISIBLE

                    cyberSecurityContentAdapter = CyberSecurityContentAdapter(ArrayList(language.data))
                    binding.recyclerViewContent.adapter = cyberSecurityContentAdapter
                    cyberSecurityContentAdapter.cyberSecurityQueryRepository = cyberSecurityQueryRepository
                }
            }
        })
        viewModel.salaryList.observe(viewLifecycleOwner, Observer { salary ->

            salary?.let {

                if(salary.data != null)
                {
                    binding.personalSalaryCount.text = it.data?.get(0)!!.personalSalaryCount
                    binding.priceMinSalary.text = it.data[1].price
                    binding.priceMaxSalary.text = it.data[2].price
                    binding.priceAverageSalary.text = it.data[3].price
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}