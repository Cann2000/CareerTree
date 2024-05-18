package com.example.careertree.view.firebase.authentication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.example.careertree.R
import com.example.careertree.databinding.FragmentSignInBinding
import com.example.careertree.utility.Constants
import com.example.careertree.utility.isNetworkAvailable
import com.example.careertree.utility.loadPersonToFirebase
import com.example.careertree.utility.setupAuthenticationEditTextListeners
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider


class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleSignClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isNetworkAvailable(requireContext())

        val signInRequest = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constants.SERVER_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignClient = GoogleSignIn.getClient(requireContext(), signInRequest)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(isNetworkAvailable(requireContext())){
            val firebaseUser = Constants.firebaseAuth.currentUser
            if(firebaseUser != null){

                val action = SignInFragmentDirections.actionLogInFragmentToHomePageFragment()
                Navigation.findNavController(requireView()).navigate(action)

            }

            binding.buttonSignIn.setOnClickListener { signInWithMail() }
            binding.signInGoogleButton.setOnClickListener { signInWithGoogle() }
            binding.signInAnonymousButton.setOnClickListener { signInWithAnonymous() }

            binding.registerLinkTextView.setOnClickListener {
                val action = SignInFragmentDirections.actionLogInFragmentToRegisterFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }

            setupAuthenticationEditTextListeners(binding.editTextEmailAddress,binding.editTextPassword)

            loadPersonToFirebase()
        }
        else{
            val dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
            val titleText = dialogView.findViewById<TextView>(R.id.title)
            val message = dialogView.findViewById<TextView>(R.id.message)
            val buttonPositive = dialogView.findViewById<Button>(R.id.btn_positive)
            val buttonNegative = dialogView.findViewById<Button>(R.id.btn_negative)

            val alertBuilder = AlertDialog.Builder(requireContext()).apply {
                setView(dialogView)
            }.create()


            titleText.text = "İnternet Erişimi Gerekli!"
            message.text = "Lütfen internetinizi açıp tekrar giriş yapınız."
            buttonPositive.text = "Tamam"
            buttonPositive.setOnClickListener {

                requireActivity().finish()
                alertBuilder.cancel()
            }
            buttonNegative.visibility =View.GONE

            alertBuilder.setCancelable(false) // Dialogun dışına tıklamayı engelle
            alertBuilder.show()
        }
    }
    private fun signInWithMail()
    {
        val email = binding.editTextEmailAddress.text.toString()
        val password = binding.editTextPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){

            Constants.firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {

                        val action = SignInFragmentDirections.actionLogInFragmentToHomePageFragment()
                        Navigation.findNavController(requireView()).navigate(action)

                        loadPersonToFirebase()

                    } else {
                        binding.authenticationLayout.visibility = View.VISIBLE
                        binding.loadProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), "Hatalı Giriş.", Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }

    private fun signInWithAnonymous(){

        binding.authenticationLayout.visibility = View.INVISIBLE
        binding.loadProgressBar.visibility = View.VISIBLE

        Constants.firebaseAuth.signInAnonymously()
            .addOnCompleteListener(requireActivity()) { task ->

                if (task.isSuccessful) {

                    val action = SignInFragmentDirections.actionLogInFragmentToHomePageFragment()
                    Navigation.findNavController(requireView()).navigate(action)

                    loadPersonToFirebase()


                } else {

                    binding.authenticationLayout.visibility = View.VISIBLE
                    binding.loadProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), "Giriş Hatası", Toast.LENGTH_SHORT,).show()
                }
            }
    }


    private fun signInWithGoogle(){

        val intent = googleSignClient.signInIntent
        startActivityForResult(intent,100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            // Google SignIn işlemi için onActivityResult
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // İşlem başarısızsa
                Log.w("GoogleSignIn", "Google sign in failed", e)
                Toast.makeText(requireContext(), "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        Constants.firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Başarılı bir şekilde Firebase'e giriş yapıldı
                    val action = SignInFragmentDirections.actionLogInFragmentToHomePageFragment()
                    Navigation.findNavController(requireView()).navigate(action)
                    loadPersonToFirebase()
                } else {
                    // Giriş başarısız
                    Log.w("GoogleSignIn", "signInWithCredential:failure", task.exception)
                    Toast.makeText(requireContext(), "Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}