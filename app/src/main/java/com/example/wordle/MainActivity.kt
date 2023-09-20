package com.example.wordle

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.Dictionary

interface FragmentCallBack {
    fun switchBackToMain(wordChosen : String)
}

class MainActivity : AppCompatActivity(), FragmentCallBack {
    val tag = "activity"
    private val layoutList = mutableListOf<LinearLayout>()
    private val editTextList = mutableListOf<EditText>()
    //Horizontal current layout
    private lateinit var currentLayout : LinearLayout
    private lateinit var summitedButton : Button
    private lateinit var resetButton : Button
    private lateinit var titleText : TextView
    private lateinit var wordList : ArrayList<String>
    private lateinit var word : String
    private lateinit var fragment : Fragment
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.setBackgroundColor(Color.WHITE)

        //Get intents
        val bundle : Bundle? = intent.getBundleExtra("wl")
        wordList = bundle?.getStringArrayList("bundle") as ArrayList<String>

        //Find Views the hard ways
        summitedButton = findViewById(R.id.summitButton)
        resetButton = findViewById(R.id.resetButton)
        titleText = findViewById(R.id.titleText)
        titleText.setTextColor(Color.BLACK)
        resetButton.isClickable = false

        run {
            layoutList.add(findViewById(R.id.h1))
            layoutList.add(findViewById(R.id.h2))
            layoutList.add(findViewById(R.id.h3))
            layoutList.add(findViewById(R.id.h4))
            layoutList.add(findViewById(R.id.h6))
        }
        run {editTextList.add(findViewById(R.id.e0_0))
            editTextList.add(findViewById(R.id.e0_1))
            editTextList.add(findViewById(R.id.e0_2))
            editTextList.add(findViewById(R.id.e0_3))
            editTextList.add(findViewById(R.id.e0_4))
            editTextList.add(findViewById(R.id.e1_0))
            editTextList.add(findViewById(R.id.e1_1))
            editTextList.add(findViewById(R.id.e1_2))
            editTextList.add(findViewById(R.id.e1_3))
            editTextList.add(findViewById(R.id.e1_4))
            editTextList.add(findViewById(R.id.e2_0))
            editTextList.add(findViewById(R.id.e2_1))
            editTextList.add(findViewById(R.id.e2_2))
            editTextList.add(findViewById(R.id.e2_3))
            editTextList.add(findViewById(R.id.e2_4))
            editTextList.add(findViewById(R.id.e3_0))
            editTextList.add(findViewById(R.id.e3_1))
            editTextList.add(findViewById(R.id.e3_2))
            editTextList.add(findViewById(R.id.e3_3))
            editTextList.add(findViewById(R.id.e3_4))
            editTextList.add(findViewById(R.id.e4_0))
            editTextList.add(findViewById(R.id.e4_1))
            editTextList.add(findViewById(R.id.e4_2))
            editTextList.add(findViewById(R.id.e4_3))
            editTextList.add(findViewById(R.id.e4_4))}

        //Assign listeners to all edit text
        for (item in editTextList) {
            item.filters += InputFilter.AllCaps()
            item.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // This method is called before the text changes
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val currentText = currentFocus as? EditText

                    if (count != 0) {
                        if ((currentText != null) && (currentText.length() > 0)) {
                            val nextText = currentText.focusSearch(View.FOCUS_RIGHT)
                            summitedButton.isClickable = nextText == null
                            nextText?.requestFocus()
                        }
                    } else {
                        if ((currentText != null) && (currentText.length() <= 0)) {
                            val nextText = currentText.focusSearch(View.FOCUS_LEFT)
                            summitedButton.isClickable = false
                            nextText?.requestFocus()
                        }
                    }

                    if ((currentText != null) && (currentText.focusSearch(View.FOCUS_RIGHT) == null))
                    {
                        val imm = currentText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(currentText.windowToken, 0)
                    }

                    resetButton.isClickable = false
                }

                override fun afterTextChanged(s: Editable?) {
                    // This method is called after the text changes
                }
            })

            item.setOnKeyListener { _, keyCode, _ ->
                val currentText = currentFocus as? EditText
                if (currentText != null) {
                    if ((keyCode == KeyEvent.KEYCODE_DEL) && (currentText.length() == 0)) {
                        val nextText = currentText.focusSearch(View.FOCUS_LEFT)
                        nextText?.requestFocus()
                    }
                }
                false
            }

        }

        for (i in 1..4)
        {
            layoutList[i].visibility = View.INVISIBLE
        }
        currentLayout = layoutList[0]
        word = randomizeWord(wordList)
    }

    //Submitted button listener
    fun buttonClick(view: View) {
        index++
        if (index < 5)
        {
            val result : Boolean = checkWord(word)
            if (result)
            {
                Toast.makeText(this, "You win", Toast.LENGTH_LONG).show()
                summitedButton.isClickable = false
                resetButton.isClickable = true
                return
            }
            currentLayout = layoutList[index]
            currentLayout.visibility = View.VISIBLE

            val currentText = currentFocus as? EditText

            if ((currentText != null) && (currentText.length() > 0)) {
                var nextText = currentText.focusSearch(View.FOCUS_DOWN)
                nextText?.requestFocus()

                for (i in 0..4)
                {
                    val backText = nextText.focusSearch(View.FOCUS_LEFT)
                    backText?.requestFocus()
                    nextText = backText
                }
            }
            summitedButton.isClickable = false
            resetButton.isClickable = true
        }
        else
        {
            summitedButton.isClickable = false
            val result : Boolean = checkWord(word)
            if (result)
                Toast.makeText(this, "You win", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this, "You lose! The word is: $word", Toast.LENGTH_LONG).show()
            //Switch to reset button
            resetButton.isClickable = true
        }
    }

    //Randomize the list of word
    private fun randomizeWord(words: ArrayList<String>): String {
        return words.random()
    }

    //Check the word
    private fun checkWord(word : String) : Boolean
    {
        val charList = word.uppercase().toList()
        val greenList = mutableListOf<Char>()
        for ((charIndex, char) in charList.withIndex())
        {
            for (i in 0..<currentLayout.childCount)
            {
                val currentEditText= currentLayout.getChildAt(i) as EditText
                val currentChar = currentEditText.text.toString().toCharArray()[0]
                if (currentChar == char)
                {
                    if (charIndex == i)
                    {
                        currentEditText.setBackgroundColor(Color.GREEN)
                        greenList.add(currentChar)
                        continue
                    }
                    else
                    {
                        if (greenList.contains(currentChar))
                            continue
                        currentEditText.setBackgroundColor(Color.YELLOW)
                    }
                }
                currentEditText.isEnabled = false
            }
        }

        return greenList.size == 5
    }

    data class ReturnCheck(val greenList: List<Char>, val yellowList: List<Char>, val greyList: List<Char>, val result: Boolean)

    private fun checkWordFromAutoPlay(charGuessList: CharArray, charChosenList: CharArray): ReturnCheck
    {
        val greenList = mutableListOf<Char>()
        val yellowList = mutableListOf<Char>()
        val greyList = mutableListOf<Char>()

        //Apply words to edittext
        for (i in 0..<currentLayout.childCount) {
                val currentEditText= currentLayout.getChildAt(i) as EditText
                currentEditText.setText(charChosenList[i].toString())

        }

        //Check the guess
        for ((charIndex, char) in charGuessList.withIndex())
        {
            for (i in 0..<currentLayout.childCount)
            {
                val currentEditText = currentLayout.getChildAt(i) as EditText
                val currentChar = charChosenList[i]
                if (currentChar == char)
                {
                    if (charIndex == i)
                    {
                        currentEditText.setBackgroundColor(Color.GREEN)
                        greenList.add(currentChar)
                        continue
                    }
                    else
                    {
                        if (greenList.contains(currentChar))
                            continue
                        currentEditText.setBackgroundColor(Color.YELLOW)
                        yellowList.add(currentChar)
                    }
                }
                else
                {
                    greyList.add(currentChar)
                }
                currentEditText.isEnabled = false
            }
        }
        return ReturnCheck(greenList, yellowList, greyList, greenList.size == 5)
    }

    //Reset the game
    fun resetGame(view: View? = null)
    {
        //Destroy fragment
        destroyFragment()

        //Reset editText
        for (item in editTextList)
        {
            item.setText("")
            item.setBackgroundColor(resources.getColor(R.color.editTextColor))
            item.isEnabled = true
        }
        index = 0

        //Reset layout
        for (i in 1..4)
        {
            layoutList[i].visibility = View.INVISIBLE
        }
        currentLayout = layoutList[0]

        //Reset focus point
        val originText = findViewById<EditText>(R.id.e0_0)
        originText?.requestFocus()

        //Reset button and randomize word
        resetButton.isClickable = false
        word = randomizeWord(wordList)
    }

    //Show and hide the fragment
    @SuppressLint("PrivateResource")
    fun showAndHideFragment(fragment: Fragment, state : Boolean)
    {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(androidx.fragment.R.animator.fragment_fade_enter, androidx.fragment.R.animator.fragment_fade_exit)
        if (fragment.isHidden && state)
            transaction.show(fragment)
        else if (!fragment.isHidden && !state)
            transaction.hide(fragment)
        transaction.commit()
    }

    //Autoplay button listener
    fun switchToAutoPlay(view : View)
    {
        for (item in editTextList)
            item.isEnabled = false

        //Create fragment
        createFragment()

        //Delete any structure on edittext
        for (item in editTextList)
        {
            if (item.text.isNotEmpty())
                item.setText("")
        }

        summitedButton.isClickable = false
        resetButton.isClickable = false
    }

    //AutoPlay
    private fun autoPlay(wordChosen : String)
    {
        val chosenCharList = wordChosen.toCharArray()
        val charInWord = mutableListOf<Char>()
        //val charInIndex =
        val charNotInWord = mutableListOf<Char>()
        val wordListCopy = ArrayList<String>(wordList)
        var guess : CharArray
        //Toast.makeText(this, wordChosen, Toast.LENGTH_LONG).show()

        //Iteration
        for (i in 0..4)
        {
            Thread() {
                guess = randomizeWord(wordListCopy).toCharArray()
                val (g, y, gr, result) = checkWordFromAutoPlay(guess, chosenCharList)
                charInWord.addAll(y)
                //charInIndex.addAll(g)
                charNotInWord.addAll(gr)

                //Filter
                for (item in wordListCopy)
                {
                    //Check the word not in list and word in yellow
                    if (item.contains(charNotInWord.toString()) || !item.contains(charInWord.toString()))
                    {
                        wordListCopy.remove(item)
                        continue
                    }

                    //Check the word in green
                    for ((index, char) in item.withIndex())
                    {
                        //if (char != charInIndex[index])
                        //{
                            //wordListCopy.remove(item)
                            //continue
                        //}
                    }
                }

                Thread.sleep(1000)
                //Next iteration preparation
                index++
                if (index < 5) {
                    if (result) {
                        Toast.makeText(this, "You win", Toast.LENGTH_LONG).show()
                        Thread.interrupted()
                    }
                    currentLayout = layoutList[index]
                    currentLayout.visibility = View.VISIBLE
                }
            }.start()
        }
    }

    //Fragment creation
    private fun createFragment()
    {
        fragment = AutoPlayFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentLayout, fragment)
        transaction.setReorderingAllowed(true)
        transaction.addToBackStack(null)
        transaction.commit()
        showAndHideFragment(fragment, true)
    }

    //Fragment destroy
    private fun destroyFragment()
    {
        supportFragmentManager.popBackStack()
    }

    //Listener for the fragment interaction
    override fun switchBackToMain(wordChosen: String) {
        if (wordChosen.isEmpty())
        {
            resetButton.isClickable = true
            destroyFragment()
            resetGame()
        }
        else
        {
            Toast.makeText(this, "AutoPlay started", Toast.LENGTH_LONG).show()
            summitedButton.isClickable = false
            resetButton.isClickable = false
            autoPlay(wordChosen.uppercase())
        }

    }
}