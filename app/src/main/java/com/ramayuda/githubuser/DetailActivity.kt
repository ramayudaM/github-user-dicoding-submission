package com.ramayuda.githubuser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import com.ramayuda.githubuser.databinding.ActivityDetailBinding
import com.ramayuda.githubuser.databinding.ToolbarActivityDetailBinding


class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var toolbar: ToolbarActivityDetailBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.toolbarDetailActivity
        setSupportActionBar(toolbar.toolbar)

        user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        binding.apply {
            tvName.text = user.name
            imgProfile.setImageResource(user.avatar)
            tvCompany.text = user.company
            tvLocation.text = user.location
            dataFollowers.text = user.follower
            dataFollowing.text = user.following
            dataRepository.text = user.repository
        }

        toolbar.actionBack.setOnClickListener(this)
        toolbar.actionShare.setOnClickListener(this)
        actionBarCustom()
        positionView()
    }

    private fun actionBarCustom() {
        toolbar.tvUsername.text = user.username
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun positionView() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.activityDetailRootLayout)

        if (user.location.length > user.company.length) {
            constraintSet.connect(binding.imgIconCompany.id, END, binding.tvLocation.id, START)
            constraintSet.connect(binding.tvCompany.id, START, binding.tvLocation.id, START, 0)
            constraintSet.clear(binding.tvCompany.id, END)
            constraintSet.connect(binding.tvLocation.id, START, PARENT_ID, START, 18)
            constraintSet.connect(binding.tvLocation.id, END, PARENT_ID, END)

        }

        constraintSet.applyTo(binding.activityDetailRootLayout)
    }

    override fun onClick(v: View?) {
        when (v!!) {
            toolbar.actionBack -> onBackPressed()
            toolbar.actionShare -> {

                val subjectValue = getString(R.string.subject_share, user.name)
                val msgValue = getString(R.string.msg_share,
                    user.name, user.username, user.company, user.location,
                    user.repository, user.follower, user.following).trimIndent()

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, subjectValue)
                    putExtra(Intent.EXTRA_TEXT, msgValue)
                }

                val shareIntent = Intent.createChooser(sendIntent, getString(R.string.text_share_github_user))
                startActivity(shareIntent)
            }
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}