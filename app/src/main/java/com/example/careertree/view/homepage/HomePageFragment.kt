package com.example.careertree.view.homepage

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.careertree.R
import com.example.careertree.adapter.BlogContentAdapter
import com.example.careertree.adapter.HomePageAdapter
import com.example.careertree.databinding.FragmentHomePageBinding
import com.example.careertree.utility.Constants
import com.example.careertree.utility.closeInputFromWindow
import com.example.careertree.utility.closeNavigationDrawer
import com.example.careertree.utility.openNavigationDrawer
import com.example.careertree.viewmodel.homepage.HomePageViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Text

@AndroidEntryPoint
class HomePageFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private var homePageAdapter = HomePageAdapter(arrayListOf())
    private var blogContentAdapter = BlogContentAdapter(arrayListOf())


    private lateinit var viewModel: HomePageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Constants.loadHomePage = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        val view = binding.root
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                // Geri tuşuna basıldığında uygulamadan çıkış yap
                requireActivity().finish()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        closeInputFromWindow(requireView(),requireContext())

        viewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)
        viewModel.getData()

        binding.recyclerViewContent.setHasFixedSize(true)
        binding.recyclerViewContent.layoutManager =  GridLayoutManager(requireView().context,2)
        //binding.viewPager.autoScroll(20000)

        openNavigationDrawer(activity as AppCompatActivity)

        observeLiveData()

    }

    private fun observeLiveData(){

        viewModel.version_name.observe(viewLifecycleOwner, Observer {

            it?.let {

                if(it.data != null){

                    val versionName = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0).versionName

                    if(versionName != it.data && Constants.loadHomePage){

                        val dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
                        val alertBuilder = AlertDialog.Builder(requireContext()).apply {
                            setView(dialogView)
                        }.create()


                        dialogView.findViewById<Button>(R.id.btn_positive)?.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PLAYSTORE_URL))
                            startActivity(intent)
                            alertBuilder.cancel()
                        }
                        dialogView.findViewById<Button>(R.id.btn_negative)?.setOnClickListener {
                            alertBuilder.cancel()
                        }
                        alertBuilder.show()



                    }
                }
            }

        })
        viewModel.contentList.observe(viewLifecycleOwner, Observer {contents ->

            contents?.let {

                if(contents.data!=null){

                    binding.recyclerViewContent.visibility = View.VISIBLE
                    homePageAdapter = HomePageAdapter(ArrayList(contents.data))
                    binding.recyclerViewContent.adapter = homePageAdapter
                }
            }

        })

        viewModel.blogList.observe(viewLifecycleOwner, Observer { blogs ->

            blogs?.let {

                if(blogs.data != null){

                    binding.viewPager.visibility = View.VISIBLE
                    blogContentAdapter = BlogContentAdapter(ArrayList(blogs.data))
                    binding.viewPager.adapter = blogContentAdapter
                    TabLayoutMediator(binding.tabDots,binding.viewPager,true) {tab, position ->}.attach() // hangi sayfada olduğunu gösterir

                }
            }
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {

            it?.let {

                if(it.data == true){
                    binding.recyclerViewContent.visibility = View.GONE
                    binding.viewPager.visibility = View.INVISIBLE
                    binding.errorText.visibility = View.VISIBLE
                }
                else{

                    binding.errorText.visibility = View.INVISIBLE
                }
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {

            it?.let {

                if(it.data == true){
                    binding.recyclerViewContent.visibility = View.GONE
                    binding.viewPager.visibility = View.INVISIBLE
                    binding.errorText.visibility = View.GONE
                    binding.loadProgressBar.visibility = View.VISIBLE
                }
                else{
                    binding.loadProgressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()

        closeNavigationDrawer(activity as AppCompatActivity)
    }

    override fun onResume() {
        super.onResume()

        openNavigationDrawer(activity as AppCompatActivity)
    }

    override fun onDestroy() {
        super.onDestroy()

        Constants.loadHomePage = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}