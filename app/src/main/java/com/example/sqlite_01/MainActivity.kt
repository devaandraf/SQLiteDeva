package com.example.sqlite_01

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sqlite_01.`object`.EmpModelClass
import com.example.sqlite_01.helper.MyAdapter
import com.example.sqlite_01.model.DatabaseHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewRecord()
    }

    fun saveRecord(view: View){
        val id = u_id.text.toString()
        val name = u_name.text.toString()
        val email = u_email.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if(id.trim()!="" && name.trim()!="" && email.trim()!=""){
            val status = databaseHandler.addEmployee(EmpModelClass(Integer.parseInt(id),name, email))
            if(status > -1){
                Toast.makeText(applicationContext,"Data Berhasil Disimpan",Toast.LENGTH_LONG).show()
                u_id.text.clear()
                u_name.text.clear()
                u_email.text.clear()
                viewRecord()
            }
        }else{
            Toast.makeText(applicationContext,"ID dan Username Harus Diisi !",Toast.LENGTH_LONG).show()
        }

    }
    // fungsi untuk membaca data dari database dan menampilkannya dari listview
    fun viewRecord(){
        // membuat instanisasi databasehandler
        val databaseHandler: DatabaseHandler= DatabaseHandler(this)

        // memamnggil fungsi viewemployee dari databsehandler untuk mengambil data
        val emp: List<EmpModelClass> = databaseHandler.viewEmployee()
        val empArrayId = Array<String>(emp.size){"0"}
        val empArrayName = Array<String>(emp.size){"null"}
        val empArrayEmail = Array<String>(emp.size){"null"}
        var index = 0

        // setiap data yang didapatkan dari database akan dimasukkan ke array
        for(e in emp){
            empArrayId[index] = e.userId.toString()
            empArrayName[index] = e.userName
            empArrayEmail[index] = e.userEmail
            index++
        }

        // membuat customadapter untuk view UI
        val myListAdapter = MyAdapter(this,empArrayId,empArrayName,empArrayEmail)
        listView.adapter = myListAdapter

        listView.setOnItemClickListener { parent, view, position, id ->
            u_id.setText(empArrayId[position])
            u_name.setText(empArrayName[position])
            u_email.setText(empArrayEmail[position])
        }
    }

    // fungsi untuk memperbarui data sesuai id user
    fun updateRecord(view: View){
            val updateId = u_id.text.toString()
            val updateName = u_name.text.toString()
            val updateEmail = u_email.text.toString()

            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
            if(updateId.trim()!="" && updateName.trim()!="" && updateEmail.trim()!=""){

                val status = databaseHandler.updateEmployee(EmpModelClass(Integer.parseInt(updateId),updateName, updateEmail))
                if(status > -1){
                    Toast.makeText(applicationContext,"Data Berhasil Di Update",Toast.LENGTH_LONG).show()
                    u_id.setText("")
                    u_name.setText("")
                    u_email.setText("")
                    viewRecord()
                }
            }else{
                Toast.makeText(applicationContext,"ID dan Username Harus Diisi !",Toast.LENGTH_LONG).show()
            }

    }

    // fungsi untuk menghapus data berdasarkan id
    fun deleteRecord(view: View){
            val deleteId = u_id.text.toString()

            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
            if(deleteId.trim()!=""){

                val status = databaseHandler.deleteEmployee(EmpModelClass(Integer.parseInt(deleteId),"",""))
                if(status > -1){
                    Toast.makeText(applicationContext,"Data Berhasil Dihapus",Toast.LENGTH_LONG).show()
                    u_id.setText("")
                    u_name.setText("")
                    u_email.setText("")
                    viewRecord()
                }
            }else{
                Toast.makeText(applicationContext,"ID Harus Diisi !",Toast.LENGTH_LONG).show()
            }
    }

    fun deleteAllRecord(view: View) {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setTitle("Hapus Semua Data")
        dialogBuilder.setMessage("Anda Yakin ?")
        dialogBuilder.setPositiveButton("Hapus", DialogInterface.OnClickListener { _, _ ->

            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
            databaseHandler.deleteAllEmployee()
            Toast.makeText(applicationContext,"Data Berhasil Dihapus",Toast.LENGTH_LONG).show()
            viewRecord()

        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
            // tidak melakukan apa2 :)
        })
        val b = dialogBuilder.create()
        b.show()
    }
}
