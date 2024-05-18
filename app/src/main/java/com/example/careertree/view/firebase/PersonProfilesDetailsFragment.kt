package com.example.careertree.view.firebase

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.careertree.R
import com.example.careertree.databinding.FragmentPersonProfilesDetailsBinding
import com.example.careertree.utility.isLinkValid
import com.example.careertree.viewmodel.profile.PersonProfilesDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonProfilesDetailsFragment : Fragment() {

    private var _binding: FragmentPersonProfilesDetailsBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: PersonProfilesDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPersonProfilesDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(PersonProfilesDetailsViewModel::class.java)

        arguments?.let {
            val token = PersonProfilesDetailsFragmentArgs.fromBundle(it).token
            viewModel.getDataFromFirebase(token)
        }

        observeLiveData()
    }

    fun observeLiveData(){

        viewModel.profileLiveData.observe(viewLifecycleOwner, Observer { profile ->

            profile?.let {

                if(profile.data != null){

                    binding.personProfilesFragment.visibility = View.VISIBLE

                    Glide.with(this@PersonProfilesDetailsFragment).load(profile.data.imageUrl).error(R.drawable.baseline_account_circle_24).into(binding.ImageViewProfile)
                    binding.profile = profile.data

                    val link = isLinkValid(profile.data.portfolio.toString())
                    binding.textPortfolio.setOnClickListener {

                        if(link){ //Link geçerli

                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(binding.textPortfolio.text.toString()))
                            startActivity(intent)
                        }
                        else{ // Link Bozuk

                            Toast.makeText(requireContext(),"Link çalışmıyor.",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {

            it?.let {

                if(it.data == true){
                    binding.personProfilesFragment.visibility = View.GONE
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