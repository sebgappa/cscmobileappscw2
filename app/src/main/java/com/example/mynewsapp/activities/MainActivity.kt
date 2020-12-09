package com.example.mynewsapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.mynewsapp.R
import com.google.firebase.auth.FirebaseAuth

/**
 * On app launch resolve if user is authenticated, this determines whether to navigate
 * to news page or ask the user to sign in.
 * @author Sebastian Gappa
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if (user != null) {
                startActivity(Intent(this, MyNewsPageActivity::class.java))
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }, 1500) // Allows time to display the splash screen
    }
}