package com.example.careertree.view.mobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.careertree.adapter.MobileContentAdapter
import com.example.careertree.databinding.FragmentMobileContentBinding
import com.example.careertree.repository.MobileQueryRepository
import com.example.careertree.utility.Constants
import com.example.careertree.viewmodel.mobile.MobileContentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MobileContentFragment : Fragment() {

    @Inject
    lateinit var mobileQueryRepository: MobileQueryRepository

    private var _binding: FragmentMobileContentBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: MobileContentViewModel

    private var mobileContentAdapter = MobileContentAdapter(arrayListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMobileContentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MobileContentViewModel::class.java)
        viewModel.getData()

        binding.recyclerViewContent.layoutManager = GridLayoutManager(requireView().context,2)

        observeLiveData()

    }

    private fun observeLiveData() {

        viewModel.salaryList.observe(viewLifecycleOwner, Observer { salary ->

            salary?.let {

                if(salary.data != null)
                {
                    binding.personalSalaryCount.text = it.data?.get(0)!!.personalSalaryCount
                    binding.priceMinSalary.text = it.data[1].price
                    binding.priceMaxSalary.text = it.data[2].price
                    binding.priceAverageSalary.text = it.data.get(3).price
                }
            }
        })

        viewModel.languageList.observe(viewLifecycleOwner, Observer { language ->

            language?.let {

                if(language.data != null){

                    binding.recyclerViewContent.visibility = View.VISIBLE

                    mobileContentAdapter = MobileContentAdapter(ArrayList(language.data))
                    binding.recyclerViewContent.adapter = mobileContentAdapter
                    mobileContentAdapter.mobileQueryRepository = mobileQueryRepository

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