package com.example.careertree.view.mobile

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
import com.example.careertree.databinding.FragmentMobileDetailsBinding
import com.example.careertree.viewmodel.mobile.MobileDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MobileDetailsFragment : Fragment() {

    private var _binding: FragmentMobileDetailsBinding? = null
    private val binding get() = _binding!!

    private var libraryListAdapter = LibraryListAdapter(arrayListOf())

    private lateinit var viewModel: MobileDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMobileDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MobileDetailsViewModel::class.java)

        binding.recyclerViewLibrary.layoutManager = LinearLayoutManager(requireContext())

        arguments?.let {
            val languageId = MobileDetailsFragmentArgs.fromBundle(it).id
            viewModel.getRoomData(languageId)
        }

        observeLiveData()
    }

    fun observeLiveData(){

        viewModel.mobileLanguageLiveData.observe(viewLifecycleOwner, Observer {language ->

            language?.let {

                if(language.data!=null){

                    binding.selectedLanguage = language.data
                    libraryListAdapter = LibraryListAdapter(ArrayList(language.data?.library))
                    binding.recyclerViewLibrary.adapter = libraryListAdapter
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