package com.example.wordle

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels



class AutoPlayFragment : Fragment() {
    private lateinit var playButton : Button
    private lateinit var backButton : Button
    private lateinit var editTextBox : EditText
    private lateinit var fragmentCallBack: FragmentCallBack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_auto_play, container, false)
        playButton = view.findViewById(R.id.playButton) as Button
        backButton = view.findViewById(R.id.backButton) as Button
        editTextBox = view.findViewById(R.id.guessWord) as EditText

        playButton.setOnClickListener {
            if (editTextBox.text.isEmpty())
                return@setOnClickListener
            fragmentCallBack.switchBackToMain(editTextBox.text.toString())
            backButton.isClickable = false
            editTextBox.isEnabled = false
            playButton.isClickable = false
        }
        backButton.setOnClickListener {
            fragmentCallBack.switchBackToMain(editTextBox.text.toString())
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = AutoPlayFragment().apply {
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentCallBack = context as FragmentCallBack
    }
}