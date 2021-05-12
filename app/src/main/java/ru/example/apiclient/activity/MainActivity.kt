package ru.example.apiclient.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import ru.example.apiclient.databinding.ActivityMainBinding
import ru.example.apiclient.fragments.CalcFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(binding.container.id, CalcFragment.newInstance())
        }
    }
}