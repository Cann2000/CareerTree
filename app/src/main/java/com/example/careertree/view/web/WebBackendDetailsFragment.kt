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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.careertree.adapter.LibraryListAdapter
import com.example.careertree.databinding.FragmentWebBackendDetailsBinding
import com.example.careertree.viewmodel.web.WebBackendDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebBackendDetailsFragment : Fragment() {

    private var _binding: FragmentWebBackendDetailsBinding? = null
    private val binding get() = _binding!!

    private var libraryAdapter = LibraryListAdapter(arrayListOf())

    private lateinit var viewModel: WebBackendDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWebBackendDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WebBackendDetailsViewModel::class.java)

        binding.recyclerViewLibrary.layoutManager = LinearLayoutManager(requireContext())

        arguments?.let {

            val languageId = WebBackendDetailsFragmentArgs.fromBundle(it).id
            viewModel.getRoomData(languageId)

        }

        observeLiveData()


    }
    fun observeLiveData(){

        viewModel.webLanguageLiveData.observe(viewLifecycleOwner, Observer { language ->

            language?.let {

                if(language.data != null){
                    binding.selectedLanguage = language.data
                    libraryAdapter = LibraryListAdapter(ArrayList(language.data?.library))
                    binding.recyclerViewLibrary.adapter = libraryAdapter
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