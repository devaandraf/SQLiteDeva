package com.example.sqlite_01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sqlite_01.`object`.LoginModelClass
import com.example.sqlite_01.model.DatabaseHandler
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {
            val txt_username = username!!.text.toString()
            val txt_password = password!!.text.toString()
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (txt_username != "" && txt_password != ""){
                if (!databaseHandler!!.checkUser(txt_username.trim())){

                    var user = LoginModelClass(username = txt_username.trim(),
                        password = txt_password.trim())


                    databaseHandler!!.addUser(user)

                    Toast.makeText(applicationContext,"Data Berhasil Dibuat", Toast.LENGTH_LONG).show()

                    val inte = Intent(this, Login::class.java)
                    startActivity(inte)

                } else{
                    Toast.makeText(applicationContext,"Username Terpakai !", Toast.LENGTH_LONG).show()
                }
            } else{
                Toast.makeText(applicationContext,"ID dan Username Harus Diisi !", Toast.LENGTH_LONG).show()
            }
        }
    }
}
