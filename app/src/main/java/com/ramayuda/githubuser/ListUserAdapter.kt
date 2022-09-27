package com.ramayuda.githubuser

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ramayuda.githubuser.databinding.RowItemUserBinding

class ListUserAdapter(private var listUser: ArrayList<User>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    class ListViewHolder(var binding: RowItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(RowItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, name, avatar, company, location, repository, follower, following) = listUser[position]
        holder.binding.apply {
            imgItemProfile.setImageResource(avatar)
            tvItemUsername.text = username
            tvItemCompany.text = company

            cvItemUser.setOnClickListener {
                val user = User(
                    username, name, avatar, company,
                    location, repository, follower, following
                )
                val intent = Intent(it.context, DetailActivity::class.java)
                    .putExtra(DetailActivity.EXTRA_USER, user)
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = listUser.size

}