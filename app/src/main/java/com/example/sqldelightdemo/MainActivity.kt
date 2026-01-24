package com.example.sqldelightdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sqldelightdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DatabaseFactory.create(this)
        val songQueries = db.songQueries
        val artistQueries = db.artistQueries

        binding.btnInsert.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                songQueries.transaction {
                    artistQueries.insertArtist("Future", "USA", "Hip-Hop")
                    artistQueries.insertArtist("Young Thug", "USA", "Hip-Hop")
                    artistQueries.insertArtist("Chief Keef", "USA", "Drill")

                    songQueries.insertSong("Mask Off", "Future", "Chill")
                    songQueries.insertSong("Hot", "Young Thug", "Energetic")
                    songQueries.insertSong("Love Sosa", "Chief Keef", "Aggressive")
                }

                withContext(Dispatchers.Main) {
                    binding.txtOutput.text = "Inserted demo songs + artists."
                }
            }
        }

        binding.btnLoad.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val merged = songQueries.selectSongsWithArtist().executeAsList()

                val text = merged.joinToString("\n") {
                    "${it.id}: ${it.title} — ${it.artist} (${it.artistCountry}, ${it.artistGenre}) [${it.mood}]"
                }.ifBlank { "(empty)" }

                withContext(Dispatchers.Main) {
                    binding.txtOutput.text = text
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                songQueries.deleteAll()
                withContext(Dispatchers.Main) {
                    binding.txtOutput.text = "Deleted all songs."
                }
            }
        }

        binding.btnError.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val text = try {
                    songQueries.insertWithId(1, "Duplicate Song", "Test Artist", "Error")
                    songQueries.insertWithId(1, "Duplicate Song", "Test Artist", "Error")
                    "Unexpected: no error occurred"
                } catch (e: Exception) {
                    "Constraint error triggered:\n${e.javaClass.simpleName}\n${e.message}"
                }

                withContext(Dispatchers.Main) {
                    binding.txtOutput.text = text
                }
            }
        }
    }
}

