import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast

class YourActivityName : Activity() {
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContentView(R.layout.your_layout_file_name) // Replace with your layout file name

        val editText = findViewById<EditText>(R.id.editText) // Replace with your EditText's ID

        // Create a TextWatcher
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changing
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has changed
                val inputText = s.toString()
                Toast.makeText(applicationContext, "Text changed: $inputText", Toast.LENGTH_SHORT).show()
            }
        }

        // Attach the TextWatcher to the EditText
        editText.addTextChangedListener(textWatcher)
    }
}
