package com.example.sqldelightdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sqldelightdemo.databinding.ActivityMainBinding
import com.example.sqldelightdemo.DatabaseFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DatabaseFactory.create(this)
        val queries = db.songQueries

        binding.btnInsert.setOnClickListener {
            queries.transaction {
                queries.insertSong("Mask Off", "Future", "Chill")
                queries.insertSong("Hot", "Young Thug", "Energetic")
                queries.insertSong("Love Sosa", "Chief Keef", "Aggressive")
            }
            binding.txtOutput.text = "Inserted 3 demo songs."
        }

        binding.btnLoad.setOnClickListener {
            val songs = queries.selectAll().executeAsList()
            binding.txtOutput.text = songs.joinToString(separator = "\n") {
                "${it.id}: ${it.title} — ${it.artist} (${it.mood})"
            }.ifBlank { "(empty)" }
        }

        binding.btnDelete.setOnClickListener {
            queries.deleteAll()
            binding.txtOutput.text = "Deleted all songs."
        }

        binding.btnError.setOnClickListener {
            try {
                queries.insertWithId(1, "Duplicate Song", "Test Artist", "Error")
                queries.insertWithId(1, "Duplicate Song", "Test Artist", "Error")

                binding.txtOutput.text = "Unexpected: no error occurred"
            } catch (e: Exception) {
                binding.txtOutput.text =
                    "Constraint error triggered:\n${e.javaClass.simpleName}\n${e.message}"
            }
        }

    }
}
