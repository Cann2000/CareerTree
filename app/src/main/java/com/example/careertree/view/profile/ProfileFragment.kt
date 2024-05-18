package com.example.careertree.view.profile

import android.R
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.careertree.databinding.FragmentProfileBinding
import com.example.careertree.utility.Constants
import com.example.careertree.utility.closeNavigationDrawer
import com.example.careertree.utility.downloadImage
import com.example.careertree.viewmodel.profile.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Float.min

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    var selectedImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerLauncher()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.loadProfileFromFirebase()

        binding.saveButton.setOnClickListener { saveButton() }
        binding.imageViewProfile.setOnClickListener { selectImage() }
        binding.deleteUserButton.setOnClickListener { deleteUserButton() }

        observeLiveData()

    }

    private fun observeLiveData() {

        viewModel.profileLiveData.observe(viewLifecycleOwner, Observer { profile ->

            profile?.let {

                if(profile.data != null){

                    binding.profileFragment.visibility = View.VISIBLE
                    binding.profile = profile.data
                }
            }
        })
        viewModel.dataLoaded.observe(viewLifecycleOwner, Observer {

            it?.let {

                if(it.data == true){

                    val action = ProfileFragmentDirections.actionProfileFragmentToHomePageFragment()
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
        })
        viewModel.universitiesLiveData.observe(viewLifecycleOwner, Observer {

            it?.let {

                if(it.data != null){

                    val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, it.data)
                    binding.autoCompleteSchools.setAdapter(adapter)
                }
            }
        })
        viewModel.departmentsLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {

                if(it.data != null){

                    val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, it.data)
                    binding.autoCompleteSchoolDepartment.setAdapter(adapter)
                }
            }
        })
        viewModel.skillsLiveData.observe(viewLifecycleOwner, Observer {

            it?.let {

                if(it.data != null){
                    val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, it.data)
                    binding.autoCompleteSkills.setAdapter(adapter)
                    binding.autoCompleteSkills.threshold = 1 // Kullanıcı yazmaya başladığında önerilerin gösterilmesi
                    binding.autoCompleteSkills.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer()) // Virgülle ayrılan öğeleri ayırma

                    binding.autoCompleteSkills.setOnKeyListener { _, keyCode, event ->
                        if (keyCode == KeyEvent.KEYCODE_SPACE && event.action == KeyEvent.ACTION_DOWN) {
                            binding.autoCompleteSkills.append(", ")
                            return@setOnKeyListener true
                        }
                        false
                    }
                }
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {

            it?.let {

                if(it.data == true){
                    binding.profileFragment.visibility = View.GONE
                    binding.errorText.visibility = View.GONE
                    binding.loadProgressBar.visibility = View.VISIBLE
                }
                else{
                    binding.loadProgressBar.visibility = View.GONE
                }
            }
        })

    }

    private fun saveButton(){

        val name = binding.editTextName.text.toString()
        val school = binding.autoCompleteSchools.text.toString()
        val universtiy_department = binding.autoCompleteSchoolDepartment.text.toString()
        val portfolio = binding.editTextPortfolio.text.toString()
        val skills = binding.autoCompleteSkills.text.toString()

        if(name == ""){

            val alertBuilder = AlertDialog.Builder(requireContext())
            alertBuilder.setMessage("Ad doldurulması zorunlu")
            alertBuilder.setPositiveButton("Tamam"){ dialog, which ->
                dialog.cancel()
            }
            alertBuilder.show()
        }
        else{

            viewModel.saveBtn(name, school, universtiy_department, portfolio, skills, selectedImage)

        }

    }

    private fun deleteUserButton(){

        val firstAlertBuilder = AlertDialog.Builder(requireContext())

        firstAlertBuilder.setTitle("Hesabınızı silmek istediğinize emin misiniz ?")
        firstAlertBuilder.setMessage("Bu işlem hesabınızı kalıcı olarak siler.")
        firstAlertBuilder.setPositiveButton("Evet"){ dialog, which ->

            Constants.firebaseAuth.currentUser?.delete()?.addOnCompleteListener {

                val secondAlertBuilder = AlertDialog.Builder(requireContext())

                secondAlertBuilder.setTitle("Hesap başarı ile silindi!")
                secondAlertBuilder.setMessage("Uygulamaya tekrar giriş yapınız.")
                secondAlertBuilder.setPositiveButton("Tamam"){ dialog, which ->
                    requireActivity().finish()
                }
                secondAlertBuilder.setCancelable(false) // Dialogun dışına tıklamayı engelle
                secondAlertBuilder.show()
            }
        }
        firstAlertBuilder.setNegativeButton("Hayır"){dialog, which ->
            dialog.cancel()
        }

        firstAlertBuilder.show()

    }

    private fun selectImage(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){

            if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){

                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.READ_MEDIA_IMAGES)) {
                    Snackbar.make(requireView(), "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission") {
                        permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                    }.show()
                }
                else {
                    permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                }
            }
            else{
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }
        else{

            if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(requireView(), "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission") {
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    }.show()
                }
                else {
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            else{
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }

        }
    }

    private fun registerLauncher(){

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->

            if(result.resultCode == RESULT_OK){

                val intentFromResult = result.data

                if (intentFromResult != null){

                    selectedImage = intentFromResult.data

                    selectedImage?.let {

                        //binding.imageViewProfile.setImageURI(selectedImage) //

                        downloadImage(binding.imageViewProfile,it.toString())

                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->

            if(result){
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
            else{
                Toast.makeText(requireActivity(), "Permission needed", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}