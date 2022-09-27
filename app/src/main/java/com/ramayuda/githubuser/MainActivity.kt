package com.ramayuda.githubuser

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ramayuda.githubuser.databinding.ActivityMainBinding
import com.ramayuda.githubuser.databinding.ToolbarActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: ToolbarActivityMainBinding
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.toolbarActivityMain
        setSupportActionBar(toolbar.toolbar)

        list.addAll(listUsers)

        binding.rvUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ListUserAdapter(list)
        }

        scrollListener()
        searchListener()
    }

    override fun onResume() {
        super.onResume()
        toolbar.actionSearch.clearFocus()
    }

    private val listUsers:  ArrayList<User>
        get() {
            val dataUsername = resources.getStringArray(R.array.username)
            val dataName = resources.getStringArray(R.array.name)
            val dataAvatar = resources.obtainTypedArray(R.array.avatar)
            val dataCompany = resources.getStringArray(R.array.company)
            val dataLocation = resources.getStringArray(R.array.location)
            val dataRepository = resources.getStringArray(R.array.repository)
            val dataFollowers = resources.getStringArray(R.array.followers)
            val dataFollowing = resources.getStringArray(R.array.following)

            val listUser = ArrayList<User>()
            for (index in dataUsername.indices) {
                listUser.add(
                    User(
                        dataUsername[index], dataName[index], dataAvatar.getResourceId(index, -1),
                        dataCompany[index], dataLocation[index], dataRepository[index], dataFollowers[index],
                        dataFollowing[index]
                    )
                )
            }
            dataAvatar.recycle()
            return listUser
        }

    private fun scrollListener() {
        binding.rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var checkScrollDown: Boolean = true

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_SETTLING -> {
                        if (checkScrollDown) {
                            toolbar.borderBottomToolbar.visibility = View.INVISIBLE
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    toolbar.borderBottomToolbar.visibility = View.VISIBLE
                    checkScrollDown = false
                } else if (dy < 0) {
                    toolbar.borderBottomToolbar.visibility = View.VISIBLE
                    checkScrollDown = true
                }
            }
        })
    }

    private fun searchListener() {
        toolbar.actionSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    list.clear()
                    val search = newText.lowercase(Locale.getDefault())

                    for (position in listUsers.indices) {
                        val (username) = listUsers[position]
                        if (username.lowercase(Locale.getDefault()).contains(search)) {
                            list.add(listUsers[position])
                        }
                        binding.rvUsers.adapter?.notifyDataSetChanged()
                    }

                } else {
                    list.clear()
                    list.addAll(listUsers)
                    binding.rvUsers.adapter?.notifyDataSetChanged()
                }

                return false
            }

        })
    }

}