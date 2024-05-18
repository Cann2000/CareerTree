package com.example.careertree.view.ai

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.careertree.databinding.FragmentAiDetailsBinding
import com.example.careertree.viewmodel.ai.AiDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AiDetailsFragment : Fragment() {

    private var _binding: FragmentAiDetailsBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel:AiDetailsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAiDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AiDetailsViewModel::class.java)

        arguments?.let {

            val languageId = AiDetailsFragmentArgs.fromBundle(it).id
            viewModel.getRoomData(languageId)
        }

        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.aiLanguageLiveData.observe(viewLifecycleOwner, Observer { language ->

            language?.let {

                if(language.data != null){

                    binding.selectedLanguage = language.data
                    binding.possibleApplications.text = HtmlCompat.fromHtml(language.data.possibleApplications.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
                    binding.udemyImage.visibility = View.VISIBLE
                    binding.udemyImage.setOnClickListener{
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(language.data.videoLink))
                        startActivity(intent)
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}