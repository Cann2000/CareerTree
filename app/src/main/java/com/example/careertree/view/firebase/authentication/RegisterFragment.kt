package com.example.careertree.view.firebase.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.careertree.databinding.FragmentRegisterBinding
import com.example.careertree.utility.Constants
import com.example.careertree.utility.setupAuthenticationEditTextListeners

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener { register() }

        setupAuthenticationEditTextListeners(binding.editTextEmailAddress,binding.editTextPassword1)
    }
    private fun register()
    {
        val email = binding.editTextEmailAddress.text.toString()
        val password1 = binding.editTextPassword1.text.toString()
        val password2 = binding.editTextPassword2.text.toString()

        if(email.isNotEmpty() && password1.isNotEmpty() && password2.isNotEmpty()){

            binding.authenticationLayout.visibility = View.INVISIBLE
            binding.loadProgressBar.visibility = View.VISIBLE

            if(password1 == password2){

                Constants.firebaseAuth.createUserWithEmailAndPassword(email, password1)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            val action = RegisterFragmentDirections.actionRegisterFragmentToLogInFragment()
                            Navigation.findNavController(requireView()).navigate(action)
                        }
                        else {
                            binding.authenticationLayout.visibility = View.VISIBLE
                            binding.loadProgressBar.visibility = View.INVISIBLE
                            Toast.makeText(requireView().context, "Bu mail daha önceden kullanılmış", Toast.LENGTH_SHORT,).show()
                        }
                    }
            }
            else{

                binding.authenticationLayout.visibility = View.VISIBLE
                binding.loadProgressBar.visibility = View.INVISIBLE
                Toast.makeText(requireView().context, "Şifreler Eşleşmiyor.", Toast.LENGTH_SHORT,).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}