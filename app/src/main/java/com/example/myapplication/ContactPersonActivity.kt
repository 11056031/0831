package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactPersonActivity : AppCompatActivity() {
    private lateinit var addImageView: ImageView
    private lateinit var bookImageView: ImageView
    private lateinit var recyclerView: RecyclerView

    private val data = mutableListOf<Pair<String, String>>()
    private lateinit var adapter: ContactAdapter
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contactperson)

        addImageView = findViewById(R.id.addp)
        bookImageView = findViewById(R.id.book)
        recyclerView = findViewById(R.id.recyclerView)

        userDao = AppDatabase.getDatabase(applicationContext).userDao()

        addImageView.setOnClickListener {
            val intent = Intent(this, PersonalActivity::class.java)
            startActivityForResult(intent, PERSONAL_ACTIVITY_REQUEST_CODE)
        }

        bookImageView.setOnClickListener {
            val intent = Intent(this, MemberActivity::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ContactAdapter(this, data,
            editListener = { position -> editContact(position) },
            deleteListener = { position -> deleteContact(position) }
        )
        recyclerView.adapter = adapter

        // 加載聯絡人數據
        loadContacts()
    }

    private fun loadContacts() {
        lifecycleScope.launch {
            val users = withContext(Dispatchers.IO) { userDao.getAllUsers() }
            data.clear()
            data.addAll(users.map { Pair(it.name, it.tel) })
            adapter.notifyDataSetChanged()
        }
    }

    private fun editContact(position: Int) {
        val intent = Intent(this, PersonalActivity::class.java)
        intent.putExtra("position", position)
        intent.putExtra("name", data[position].first)
        intent.putExtra("tel", data[position].second)
        startActivityForResult(intent, PERSONAL_ACTIVITY_REQUEST_CODE)
    }

    private fun deleteContact(position: Int) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val user = User(tel = data[position].second, name = data[position].first)
                userDao.delete(user)
            }
            data.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PERSONAL_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val position = data?.getIntExtra("position", -1) ?: return
            val name = data.getStringExtra("name") ?: return
            val tel = data.getStringExtra("tel") ?: return

            val user = User(tel, name)

            lifecycleScope.launch {
                if (position == -1) {
                    // 添加新聯絡人
                    withContext(Dispatchers.IO) { userDao.insert(user) }
                    this@ContactPersonActivity.data.add(Pair(name, tel))
                    adapter.notifyItemInserted(this@ContactPersonActivity.data.size - 1)
                } else {
                    // 更新現有聯絡人
                    withContext(Dispatchers.IO) { userDao.update(user) }
                    this@ContactPersonActivity.data[position] = Pair(name, tel)
                    adapter.notifyItemChanged(position)
                }
            }
        }
    }

    companion object {
        private const val PERSONAL_ACTIVITY_REQUEST_CODE = 1
    }
}
