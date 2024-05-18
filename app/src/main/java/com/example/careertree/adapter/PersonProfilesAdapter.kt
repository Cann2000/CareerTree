package com.example.careertree.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.careertree.R
import com.example.careertree.databinding.RecyclerRowPersonprofilesBinding
import com.example.careertree.model.profile.Profile
import com.example.careertree.utility.ClickListener
import com.example.careertree.utility.makePlaceHolder
import com.example.careertree.view.firebase.PersonProfilesFragmentDirections

class PersonProfilesAdapter(var profileList:ArrayList<Profile>): RecyclerView.Adapter<PersonProfilesAdapter.PersonProfilesHolder>() {

    class PersonProfilesHolder(val binding: RecyclerRowPersonprofilesBinding):RecyclerView.ViewHolder(binding.root),ClickListener{

        override fun dataClicked(view: View) {

            val profile = binding.profile

            if(profile?.token != null){

                val action = PersonProfilesFragmentDirections.actionPersonProfilesFragmentToPersonProfilesDetailsFragment(profile.token)
                Navigation.findNavController(view).navigate(action)

            }
        }

        override fun compareClicked(view: View) {
            TODO("Not yet implemented")
        }

        override fun starClicked(view: View) {
            TODO("Not yet implemented")
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonProfilesHolder {

        val binding = DataBindingUtil.inflate<RecyclerRowPersonprofilesBinding>(LayoutInflater.from(parent.context), R.layout.recycler_row_personprofiles,parent,false)
        return PersonProfilesHolder(binding)
    }


    override fun onBindViewHolder(holder: PersonProfilesHolder, position: Int) {

        holder.binding.profile = profileList[position]

        Glide.with(holder.itemView).load(profileList[position].imageUrl).error(R.drawable.baseline_account_circle_24).into(holder.binding.profileRecyclerImageView)
        holder.binding.listener = holder
    }

    override fun getItemCount(): Int {

        return profileList.size
    }

    fun dataListUpdate(newData:List<Profile>){
        profileList.clear()
        profileList.addAll(newData)
        notifyDataSetChanged()
    }
    fun setFilteredList(dataList:ArrayList<Profile>){
        this.profileList  = dataList
        notifyDataSetChanged()
    }
}