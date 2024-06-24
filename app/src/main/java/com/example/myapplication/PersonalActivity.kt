package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PersonalActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var telEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var ctlpImageView: ImageView
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal)

        nameEditText = findViewById(R.id.name)
        telEditText = findViewById(R.id.tel)
        saveButton = findViewById(R.id.saveButton)
        ctlpImageView = findViewById(R.id.ctlp)

        setupEditText(nameEditText, "Name")
        setupEditText(telEditText, "tel")

        position = intent.getIntExtra("position", -1)
        if (position != -1) {
            nameEditText.setText(intent.getStringExtra("name"))
            telEditText.setText(intent.getStringExtra("tel"))
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val tel = telEditText.text.toString().trim()

            val resultIntent = Intent()
            resultIntent.putExtra("position", position)
            resultIntent.putExtra("name", name)
            resultIntent.putExtra("tel", tel)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        ctlpImageView.setOnClickListener {
            val intent = Intent(this, ContactPersonActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupEditText(editText: EditText, defaultText: String) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (editText.text.toString() == defaultText) {
                    editText.setText("")
                }
            } else {
                if (editText.text.toString().isEmpty()) {
                    editText.setText(defaultText)
                }
            }
        }
    }
}
