package com.example.sqlite_01.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.sqlite_01.`object`.EmpModelClass
import com.example.sqlite_01.`object`.LoginModelClass

class DatabaseHandler(context: Context):
    SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase"
        private val TABLE_CONTACTS = "EmployeeTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
        private val USER_TABLE = "userTable"
        private val U_ID = "userId"
        private val U_USERNAME = "username"
        private val U_PASSWORD = "password"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // membuat tabel beserta definisi kolomnya
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT" + ");")
        db?.execSQL(CREATE_CONTACTS_TABLE)
        val CREATE_USER_TABLE = ("CREATE TABLE " + USER_TABLE +
                "(" + U_ID + " INTEGER PRIMARY KEY," + U_USERNAME + " TEXT,"  + U_PASSWORD + " TEXT " + ");")
        db?.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS + "," + USER_TABLE)
        onCreate(db)
    }

    //fungsi verifikasi username & password dapat login
    fun checkUser(username: String, password: String): Boolean{
        val columns = arrayOf(U_ID)

        val db = this.readableDatabase

        val selection = "$U_USERNAME = ? AND $U_PASSWORD =?"

        val selectionArgs = arrayOf(username, password)

        val cursor = db.query(
            USER_TABLE,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null)

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        if (cursorCount > 0)
            return true

        return false
    }

    //fungsi verifikasi username sudah terpakai atau tidak
    fun checkUser(username: String): Boolean{
        val columns = arrayOf(U_ID)

        val db = this.readableDatabase

        val selection = "$U_USERNAME = ? "

        val selectionArgs = arrayOf(username)

        val cursor = db.query(
            USER_TABLE,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null)

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        if (cursorCount > 0)
            return true

        return false
    }

    //fungsi untuk register
    fun addUser(login: LoginModelClass){
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(U_USERNAME, login.username)
        contentValues.put(U_PASSWORD, login.password)

        db.insert(USER_TABLE, null, contentValues)
        db.close()
    }

    // fungsi untuk menambahkan data
    fun addEmployee(emp: EmpModelClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName)
        contentValues.put(KEY_EMAIL,emp.userEmail )
        // menambahkan data pada tabel
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close()
        return success
    }

    // fungsi untuk menampilkan data dari tabel ke UI
    fun viewEmployee():List<EmpModelClass>{
        val empList:ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userId: Int
        var userName: String
        var userEmail: String
        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("id"))
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))
                val emp= EmpModelClass(userId = userId, userName = userName, userEmail = userEmail)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }
    // fungsi untuk memperbarui data pegawai
    fun updateEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName)
        contentValues.put(KEY_EMAIL,emp.userEmail )

        // memperbarui data
        val success = db.update(TABLE_CONTACTS, contentValues,"id="+emp.userId,null)

        // menutup koneksi ke database
        db.close()
        return success
    }
    // fungsi untuk menghapus data
    fun deleteEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        // employee id dari data yang akan dihapus
        contentValues.put(KEY_ID, emp.userId)
        // query untuk menghapus ata
        val success = db.delete(TABLE_CONTACTS,"id="+emp.userId,null)

        // menutup koneksi ke database
        db.close()
        return success
    }

    fun deleteAllEmployee():Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        // query untuk menghapus semua data
        val success = db.delete(TABLE_CONTACTS,null,null)

        // menutup koneksi ke database
        db.close()
        return success
    }
}