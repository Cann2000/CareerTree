package com.example.careertree.view.gemini_ai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.careertree.databinding.FragmentAiChatbotBinding
import com.example.careertree.utility.closeInputFromWindow
import com.example.careertree.viewmodel.gemini_ai.AiChatBotViewModel

class AiChatBotFragment : Fragment() {

    private var _binding: FragmentAiChatbotBinding? = null
    private val binding get() = _binding!!

    private lateinit var aiChatBotViewModel:AiChatBotViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAiChatbotBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        aiChatBotViewModel = ViewModelProvider(this).get(AiChatBotViewModel::class.java)
        binding.btnSubmit.setOnClickListener { aiChatBotViewModel.geminiAiChatBotResult(binding.etQuestion.text.toString()) }

        observeLiveData()

    }

    private fun observeLiveData() {

        aiChatBotViewModel.aiTextResponse.observe(viewLifecycleOwner, Observer { result ->

            if(result != null && result.isNotEmpty()){

                binding.txtResponse.text = result.toString()
                binding.txtResponse.visibility = View.VISIBLE
                binding.messageAnim.visibility = View.GONE
            }
        })
        aiChatBotViewModel.loading.observe(viewLifecycleOwner, Observer {

            it?.let {

                if(it.data == true){

                    closeInputFromWindow(requireView(),requireContext())
                    binding.etQuestion.text?.clear()
                    binding.etQuestion.clearFocus()
                    binding.btnSubmit.visibility = View.INVISIBLE
                    binding.txtResponse.visibility = View.GONE
                    binding.messageAnim.visibility = View.VISIBLE
                }
                else{
                    binding.btnSubmit.visibility = View.VISIBLE
                }
            }
        })
        aiChatBotViewModel.errorMessage.observe(viewLifecycleOwner, Observer {

            it?.let {

                if(it.data == true){
                    println("${it.data} error")
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}