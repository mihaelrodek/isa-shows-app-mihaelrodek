package com.android.infinum

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.infinum.ShowsFragment.ShowData.shows
import com.android.infinum.databinding.FragmentShowsBinding


class ShowsFragment : Fragment() {

    companion object {
        private const val EXTRA_USERNAME = "EXTRA_USERNAME"

        fun buildIntent(activity: Activity, username: String): Intent {
            val intent = Intent(activity, ShowDetailsFragment::class.java)
            intent.putExtra(EXTRA_USERNAME, username)
            return intent
        }
    }

    object ShowData {
        val shows = listOf(
            ShowsModel(
                "1", "Dark",
                "A family saga with a supernatural twist, set in a German town, where the disappearance of two young children exposes the relationships among four families.",
                listOf(), R.drawable.dark
            ),
            ShowsModel(
                "2",
                "Daredevil",
                "A blind lawyer by day, vigilante by night. Matt Murdock fights the crime of New York as Daredevil.",
                listOf(),
                R.drawable.daredevil
            ),
            ShowsModel(
                "3",
                "Punisher",
                "After the murder of his family, Marine veteran Frank Castle becomes the vigilante known as \"The Punisher,\" with only one goal in mind: to avenge them.",
                listOf(),
                R.drawable.punisher
            ),
            ShowsModel(
                "4",
                "Jessica Jones",
                "Following the tragic end of her brief superhero career, Jessica Jones tries to rebuild her life as a private investigator, dealing with cases involving people with remarkable abilities in New York City.",
                listOf(),
                R.drawable.jessica_jones
            ),
            ShowsModel(
                "5",
                "Luke Cage",
                "When a sabotaged experiment gives him super strength and unbreakable skin, Luke Cage becomes a fugitive attempting to rebuild his life in Harlem and must soon confront his past and fight a battle for the heart of his city.",
                listOf(),
                R.drawable.luke_cage
            )
        )
    }

    private var _binding: FragmentShowsBinding? = null

    private val binding get() = _binding!!

    private var showsAdapter: ShowsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initShowsRecycler()

        binding.refresh.setOnClickListener {
            binding.emptyStateLabel.isVisible = !binding.emptyStateLabel.isVisible
            binding.showsRecyclerView.isVisible = !binding.emptyStateLabel.isVisible
        }

        binding.logoutButton.setOnClickListener {
            findNavController().navigate(R.id.action_second_to_first)
        }

    }

    private fun initShowsRecycler() {

        binding.showsRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.showsRecyclerView.adapter = ShowsAdapter(shows) { item ->
            val action = ShowsFragmentDirections.actionSecondToThird(item.toInt())
            findNavController().navigate(action)
        }
        showsAdapter?.setItems(shows)
    }

}