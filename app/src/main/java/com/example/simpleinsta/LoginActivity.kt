package com.example.simpleinsta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseObject
import com.parse.ParseUser

private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {
    lateinit var et_usrname: EditText
    lateinit var et_pssword: EditText
    lateinit var button_login: Button
    lateinit var button_signup: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        /* Testing if connection was successful
        val firstObject = ParseObject("FirstClass")
        firstObject.put("message","Hey ! First message from android. Parse is now connected")
        firstObject.saveInBackground {
            if (it != null){
                it.localizedMessage?.let { message -> Log.e("MainActivity", message) }
            }else{
                Log.d("MainActivity","Object saved.")
            }
        }

         */
        // Check if user is already logged in
        if (ParseUser.getCurrentUser() != null){
            goto_MainActivity()
        }
        // If not, then
        et_usrname = findViewById(R.id.et_username)
        et_pssword = findViewById(R.id.et_password)
        button_login = findViewById(R.id.login_button)
        button_signup = findViewById(R.id.sign_button)

        button_login.setOnClickListener{
            var usrname = et_usrname.text.toString()
            var password = et_pssword.text.toString()
            login_user(usrname,password)

        }
        button_signup.setOnClickListener{
            var usrname = et_usrname.text.toString()
            var password = et_pssword.text.toString()
            signup_user(usrname,password)
        }
    }

    private fun login_user(usr:String, pswd:String){
        ParseUser.logInInBackground(usr, pswd, ({ user, e ->
            if (user != null) {
                // Hooray!  The user is logged in.
                Toast.makeText(this,"Login Successful!", Toast.LENGTH_SHORT).show()  // TODO not showing on app.
                Log.i(TAG, "Login Succesful!") // This actually works
                goto_MainActivity()

            } else {
                // Signup failed.  Look at the ParseException to see what happened.
                Toast.makeText(this,"Login Failed!", Toast.LENGTH_SHORT).show() // TODO not showing
                Log.i(TAG, "Login Failed!") // It works too.
                e.printStackTrace()

            }})
        )
    }
    private fun signup_user(usr:String, pswd:String){ // TODO maybe do another activity?
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(usr)
        user.setPassword(pswd)

        user.signUpInBackground { e ->
            if (e == null) {
                // Hooray! Let them use the app now.
                Toast.makeText(this,"Login Successful!", Toast.LENGTH_SHORT).show()  // TODO not showing on app.
                Log.i(TAG, "Login Succesful!") // This actually works
                goto_MainActivity()
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Toast.makeText(this,"Signup Failed!", Toast.LENGTH_SHORT).show() // TODO not showing
                Log.i(TAG, "Login Failed!") // It works too.
                e.printStackTrace()
            }
        }
    }
    private fun goto_MainActivity(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish() // This essentially terminates the current view, pressing back will now exit app.
    }


}