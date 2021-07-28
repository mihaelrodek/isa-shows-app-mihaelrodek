package com.android.infinum.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.infinum.R
import com.android.infinum.models.ReviewModel
import com.android.infinum.models.ShowsModel

class ShowsViewModel : ViewModel(){

    private val shows = mutableListOf(
        ShowsModel(
            "1", "Dark",
            "A family saga with a supernatural twist, set in a German town, where the disappearance of two young children exposes the relationships among four families.",
            listOf(), R.drawable.dark
        ),
        ShowsModel(
            "2",
            "Daredevil",
            "A blind lawyer by day, vigilante by night. Matt Murdock fights the crime of New York as Daredevil.",
            listOf(
                ReviewModel("Test", 5.0f, R.drawable.super_mario),
                ReviewModel
                    (
                    "Jako jako puno previše mnogo jako veliko puno veliki komentar na review ove serije u par redova da Brane bude sretan i da mogu krenuti na 5. i 6. zadacu",
                    1.0f,
                    R.drawable.super_mario
                )
            ),
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

    private val showsLiveData: MutableLiveData<List<ShowsModel>> by lazy {
        MutableLiveData<List<ShowsModel>>()
    }

    fun getShowsLiveData(): LiveData<List<ShowsModel>> {
        return showsLiveData
    }

    fun initShows() {
        showsLiveData.value = shows
    }

    fun addShow(superhero: ShowsModel) {
        shows.add(superhero)
        showsLiveData.value = shows
    }

}