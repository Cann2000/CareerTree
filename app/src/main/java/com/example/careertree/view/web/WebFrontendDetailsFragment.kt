package com.example.careertree.view.web

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.careertree.databinding.FragmentWebFrontendDetailsBinding
import com.example.careertree.viewmodel.web.WebFrontendDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebFrontendDetailsFragment : Fragment() {

    private var _binding: FragmentWebFrontendDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WebFrontendDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWebFrontendDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WebFrontendDetailsViewModel::class.java)

        arguments?.let {
            val languageId = WebFrontendDetailsFragmentArgs.fromBundle(it).id
            viewModel.getRoomData(languageId)
        }

        observeLiveData()
    }
    fun observeLiveData(){

        viewModel.webLanguageLiveData.observe(viewLifecycleOwner, Observer { language ->

            language?.let {

                if(language.data != null){

                    binding.selectedLanguage = language.data
                    binding.udemyImage.visibility = View.VISIBLE
                    binding.udemyImage.setOnClickListener{
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(language.data.videoLink))
                        startActivity(intent)
                    }
                }
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}