package com.android.infinum

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.infinum.ShowData.shows
import com.android.infinum.databinding.ActivityShowsBinding


class ShowsActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_USERNAME = "EXTRA_USERNAME"

        fun buildIntent(activity: Activity, username: String): Intent {
            val intent = Intent(activity, ShowDetailsActivity::class.java)
            intent.putExtra(EXTRA_USERNAME, username)
            return intent
        }
    }


    private lateinit var binding: ActivityShowsBinding

    private var showsAdapter: ShowsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.extras?.getString(EXTRA_USERNAME)

        initShowsRecycler()

        binding.refresh.setOnClickListener {
            binding.emptyStateLabel.isVisible = !binding.emptyStateLabel.isVisible
            binding.showsRecyclerView.isVisible = !binding.emptyStateLabel.isVisible
        }

    }

    private fun initShowsRecycler() {

        binding.showsRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.showsRecyclerView.adapter = ShowsAdapter(shows) { item ->
            val intent = ShowDetailsActivity.buildIntent(this, item)
            startActivity(intent)
        }
        showsAdapter?.setItems(shows)
    }

}