package com.example.careertree.view.web

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.careertree.adapter.WebContentAdapter
import com.example.careertree.databinding.FragmentBackendContentBinding
import com.example.careertree.repository.WebQueryRepository
import com.example.careertree.viewmodel.web.WebBackendContentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WebBackendContentFragment : Fragment() {

    @Inject
    lateinit var webQueryRepository: WebQueryRepository

    private var _binding: FragmentBackendContentBinding? = null
    private val binding get() = _binding!!

    private var webAdapter = WebContentAdapter(arrayListOf())
    private lateinit var viewModel: WebBackendContentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBackendContentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WebBackendContentViewModel::class.java)
        viewModel.getData()

        binding.recyclerViewContent.layoutManager = GridLayoutManager(requireView().context,2)

        observeLiveData()
    }

    fun observeLiveData(){

        viewModel.languageList.observe(viewLifecycleOwner, Observer { language ->

            language?.let {

                if(language.data != null){

                    binding.recyclerViewContent.visibility = View.VISIBLE

                    webAdapter = WebContentAdapter(ArrayList(it.data))
                    binding.recyclerViewContent.adapter = webAdapter
                    webAdapter.webQueryRepository = webQueryRepository

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