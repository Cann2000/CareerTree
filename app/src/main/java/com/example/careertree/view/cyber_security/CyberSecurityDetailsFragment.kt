package com.example.careertree.view.cyber_security

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
import com.example.careertree.databinding.FragmentCyberSecurityDetailsBinding
import com.example.careertree.viewmodel.cyber_security.CyberSecurityDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CyberSecurityDetailsFragment : Fragment() {

    private var _binding: FragmentCyberSecurityDetailsBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: CyberSecurityDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCyberSecurityDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CyberSecurityDetailsViewModel::class.java)

        arguments?.let {

            val languageId = CyberSecurityDetailsFragmentArgs.fromBundle(it).id
            viewModel.getRoomData(languageId)
        }

        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.cyberSecurityLanguageLiveData.observe(viewLifecycleOwner, Observer { language ->

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}