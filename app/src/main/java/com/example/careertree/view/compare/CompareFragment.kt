package com.example.careertree.view.compare

import android.os.Bundle
import android.text.Spanned
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.careertree.databinding.FragmentCompareBinding
import com.example.careertree.utility.downloadImage
import com.example.careertree.viewmodel.compare.CompareViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompareFragment : Fragment() {

    private var _binding: FragmentCompareBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CompareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCompareBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this).get(CompareViewModel::class.java)

        arguments?.let {
            val key = CompareFragmentArgs.fromBundle(it).key
            viewModel.getCompareData(key)
        }

        observeLiveData()

    }
    fun observeLiveData(){

        viewModel.compareLiveData.observe(viewLifecycleOwner, Observer { compareData->

            compareData?.let {

                if(compareData.data != null){

                    if(compareData.data.size == 2){// zaten veri sayım 2 değilse bu fragment içine giremiyor

                        binding.relativeLayout.visibility = View.VISIBLE

                        val advantageData1: Spanned = HtmlCompat.fromHtml(compareData.data[0].advantage.toString(),HtmlCompat.FROM_HTML_MODE_LEGACY)
                        val disadvantageData1: Spanned = HtmlCompat.fromHtml(compareData.data[0].disadvantage.toString(),HtmlCompat.FROM_HTML_MODE_LEGACY)

                        val advantageData2: Spanned = HtmlCompat.fromHtml(compareData.data[1].advantage.toString(),HtmlCompat.FROM_HTML_MODE_LEGACY)
                        val disadvantageData2: Spanned = HtmlCompat.fromHtml(compareData.data[1].disadvantage.toString(),HtmlCompat.FROM_HTML_MODE_LEGACY)

                        binding.mainTitle.text = compareData.data[0].mainTitle
                        binding.mainTitle2.text = compareData.data[1].mainTitle
                        binding.advantage.text = advantageData1
                        binding.disadvantage.text = disadvantageData1
                        binding.advantage2.text = advantageData2
                        binding.disadvantage2.text = disadvantageData2

                        downloadImage(binding.mainTitleImage,compareData.data[0].imageUrl)
                        downloadImage(binding.mainTitle2Image,compareData.data[1].imageUrl)

                    }
                }
            }


        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {

            it?.let {

                if(it.data == true){
                    binding.relativeLayout.visibility = View.GONE
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