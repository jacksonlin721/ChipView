package com.example.chipview

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chipview.library.views.ChipsView
import com.example.chipview.library.views.model.Contact
import com.google.android.material.chip.ChipGroup
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var chipGroup: ChipGroup
    private var mContacts: RecyclerView? = null
    private var mAdapter: ContactsAdapter? = null
    var mChipsView: ChipsView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContacts = findViewById(R.id.rv_contacts) as RecyclerView
        mContacts!!.setLayoutManager(LinearLayoutManager(this@MainActivity))
        mAdapter = ContactsAdapter()
        mContacts!!.setAdapter(mAdapter)

        mChipsView = findViewById<View>(R.id.cv_contacts) as ChipsView
        // mChipsView.useInitials(14, Typeface.createFromAsset(this.getAssets(), "fonts/FiraSans-Medium.ttf"), Color.RED);

        // change EditText config
        mChipsView!!.editText.isCursorVisible = true

        mChipsView!!.setChipsValidator(object : ChipsView.ChipValidator() {
            override fun isValid(contact: Contact): Boolean {
                return if (contact.getDisplayName().equals("asd@qwe.de")) {
                    false
                } else true
            }
        })

        mChipsView!!.setChipsListener(object : ChipsView.ChipsListener {
            override fun onChipAdded(chip: ChipsView.Chip?) {
                for (chipItem in mChipsView!!.chips) {
                    Log.d("ChipList", "chip: $chipItem")
                }
            }

            override fun onChipDeleted(chip: ChipsView.Chip?) {}
            override fun onTextChanged(text: CharSequence?) {
                Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
            }

            override fun onInputNotRecognized(text: String?): Boolean {
//                try {
//                    val fragmentManager: FragmentManager = this@MainActivity.supportFragmentManager
//                    val bundle = Bundle()
//                    bundle.putString(ChipsEmailDialogFragment.EXTRA_STRING_TEXT, text)
//                    bundle.putString(ChipsEmailDialogFragment.EXTRA_STRING_TITLE, "Title")
//                    bundle.putString(
//                        ChipsEmailDialogFragment.EXTRA_STRING_PLACEHOLDER,
//                        "ChipsDialogPlaceholder"
//                    )
//                    bundle.putString(
//                        ChipsEmailDialogFragment.EXTRA_STRING_CONFIRM,
//                        "ChipsDialogConfirm"
//                    )
//                    bundle.putString(
//                        ChipsEmailDialogFragment.EXTRA_STRING_CANCEL,
//                        "ChipsDialogCancel"
//                    )
//                    bundle.putString(
//                        ChipsEmailDialogFragment.EXTRA_STRING_ERROR_MSG,
//                        "ChipsDialogErrorMsg"
//                    )
//                    val chipsEmailDialogFragment = ChipsEmailDialogFragment()
//                    chipsEmailDialogFragment.setArguments(bundle)
//                    chipsEmailDialogFragment.setEmailListener(object : EmailListener() {
//                        fun onDialogEmailEntered(text: String?, displayName: String?) {
//                            mChipsView.addChip(
//                                displayName, null, Contact(
//                                    null, null, displayName,
//                                    text!!, null
//                                ), false
//                            )
//                            mChipsView.clearText()
//                        }
//                    })
//                    chipsEmailDialogFragment.show(
//                        fragmentManager,
//                        ChipsEmailDialogFragment::class.java.getSimpleName()
//                    )
//                } catch (e: ClassCastException) {
//                    Log.e("CHIPS", "Error ClassCast", e)
//                }
                return false
            }
        })
    }

    inner class ContactsAdapter :
        RecyclerView.Adapter<CheckableContactViewHolder?>() {
        private val data = arrayOf(
            "john@doe.com",
            "at@doodle.com",
            "asd@qwe.de",
            "verylongaddress@verylongserver.com",
            "thisIsMyEmail@address.com",
            "test@testeration.de",
            "short@short.com"
        )
        private val filteredList: MutableList<String> = ArrayList()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CheckableContactViewHolder {
            val itemView: View = LayoutInflater.from(this@MainActivity)
                .inflate(R.layout.item_checkable_contact, parent, false)
            return CheckableContactViewHolder(itemView)
        }


        override fun onBindViewHolder(
            holder: CheckableContactViewHolder,
            position: Int
        ) {
            holder.name.setText(filteredList[position])
        }

        override fun getItemCount(): Int {
            return filteredList.size
        }

        fun filterItems(text: CharSequence?) {
            filteredList.clear()
            if (TextUtils.isEmpty(text)) {
                Collections.addAll(filteredList, *data)
            } else {
                for (s in data) {
                    if (s.contains(text!!)) {
                        filteredList.add(s)
                    }
                }
            }
            notifyDataSetChanged()
        }

        override fun getItemViewType(position: Int): Int {
            return Math.abs(filteredList[position].hashCode())
        }

        init {
            Collections.addAll(filteredList, *data)
        }

    }

    inner class CheckableContactViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val name: TextView
        val selection: CheckBox
        override fun onClick(v: View) {
            val email = name.text.toString()
            val imgUrl =
                if (Math.random() > .7) null else Uri.parse("https://robohash.org/" + Math.abs(email.hashCode()))
            val contact = Contact(null, null, null, email, imgUrl)
            if (selection.isChecked) {
                val indelibe = Math.random() > 0.8f
                mChipsView!!.addChip(email, imgUrl, contact, indelibe)
            } else {
                mChipsView!!.removeChipBy(contact)
            }
        }

        init {
            name = itemView.findViewById<View>(R.id.tv_contact_name) as TextView
            selection = itemView.findViewById<View>(R.id.cb_contact_selection) as CheckBox
            selection.setOnClickListener(this)
            itemView.setOnClickListener { selection.performClick() }
        }
    }
}