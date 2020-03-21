package com.example.sqlite_01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sqlite_01.model.DatabaseHandler
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginProses.setOnClickListener {
            val txt_user = username!!.text.toString()
            val txt_pass = password!!.text.toString()
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            if (txt_user.trim() != "" && txt_pass.trim() != ""){

                if (databaseHandler!!.checkUser(txt_user.trim { it <= ' ' })) {

                    if (databaseHandler!!.checkUser(txt_user.trim { it <= ' ' },
                            txt_pass.trim { it <= ' ' })) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("USERNAME", txt_user.trim { it <= ' ' })
                        username.setText(null)
                        password.setText(null)
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "Password salah", Toast.LENGTH_LONG).show()
                    }

                } else{
                    Toast.makeText(applicationContext, "Username salah", Toast.LENGTH_LONG).show()
                }

            } else{
                Toast.makeText(applicationContext,"ID dan Username Harus Diisi !", Toast.LENGTH_LONG).show()
            }


        }

        btnRegister.setOnClickListener {
            val inte = Intent(this, Register::class.java)
            startActivity(inte)
        }
    }
}
