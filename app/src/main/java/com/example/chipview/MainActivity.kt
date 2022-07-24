package com.example.chipview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {

    lateinit var chipGroup: ChipGroup
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView = findViewById(R.id.search)
        searchView.setOnQueryTextFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, p1: Boolean) {
                chipGroup.removeAllViews()
            }
        })

        searchView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                chipGroup.removeAllViews()
                return true
            }
        })

        chipGroup = findViewById(R.id.chip_group)

        val chip = findViewById<Chip>(R.id.chip)
        chip.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                addNewChip("123")
                searchView.clearFocus()
                return true
            }
        })
    }

    private fun addNewChip(text: String) {
        val newChip =
            LayoutInflater.from(this@MainActivity).inflate(R.layout.chip_item, chipGroup, false) as Chip
        newChip.id = ViewCompat.generateViewId()
        newChip.text = text
        newChip.setOnCloseIconClickListener {
            chipGroup.removeView(newChip)
        }
        chipGroup.addView(newChip)
    }

}