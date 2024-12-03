package com.example.herat_beat.databases

import HomeRecord
import TimeRecord
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.herat_beat.HeartBeat

class DatabaseHelper(): SQLiteOpenHelper(HeartBeat.context, "my_db", null, 1) {
    //采用应用级context,简答处理数据的处理。
companion object{
    val aaa ="CREATE TABLE LastInsertId (id INTEGER)"

    val bbb="CREATE TABLE ALastInsert (id INTEGER)"
    const val CREATE_HOME_RECORD_TABLE = ("CREATE TABLE IF NOT EXISTS HomeRecord ("
            + "id INTEGER PRIMARY KEY,"
            + "startTime TEXT,"
            + "date TEXT,"
            + "frequency INTEGER,"
            + "dianjitime TEXT,"
            + "endTime TEXT"
            + ")")
    val CREATE_TABLE_QUERY = "CREATE TABLE items (id INTEGER PRIMARY KEY,   string1 TEXT,\n" +
            "  string2 TEXT,\n" +
            "  string3 TEXT,\n" +
            "  string4 TEXT)"
    val INSERT_DEFAULT_DATA_QUERY = "INSERT INTO items (id,string1, string2, string3, string4) VALUES (0,'00:00:00', '00:00', '00:00', '0')"
    val INSERT_QUERY = "INSERT INTO LastInsertId (id) VALUES (0)"
    val INSERT_2 = "INSERT INTO ALastInsert (id) VALUES (0)"
    val insertQuery = "INSERT INTO HomeRecord (id, startTime, date, frequency, dianjitime, endTime) VALUES (0, '00:00', '00', 0, '0', '00:00')"

}
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_QUERY)
        db.execSQL(aaa)
        db.execSQL(bbb)
        db.execSQL(CREATE_HOME_RECORD_TABLE)
        db.execSQL(INSERT_DEFAULT_DATA_QUERY)
        db.execSQL(INSERT_QUERY)
        db.execSQL(INSERT_2)
        db.execSQL(insertQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 数据库升级逻辑
    }
    fun deleteItem(idToDelete:Int){
        val db = writableDatabase
        val deleteQuery = "DELETE FROM items WHERE id = $idToDelete;"
        db.execSQL(deleteQuery)
        db.close()
    }

    fun deleteAll()
    {
        val db = writableDatabase
        val deleteQuery = "DELETE FROM items;"
        db.execSQL(deleteQuery)
        db.close()
    }
    fun updateItem(id: Int, newValue: String) {
        val values = ContentValues()

        values.put("string4",newValue)

        val db = writableDatabase
        db.update("items", values, "id = ?", arrayOf(id.toString()))
        db.close()
    }
    fun insertHome(item:HomeRecord){
        val query = "SELECT id FROM ALastInsert"
        val db=writableDatabase
        val cursorr = db.rawQuery(query, null)
        var lastId = 0
        if (cursorr.moveToFirst()) {
            lastId = cursorr.getInt(0)
            Log.d("itemmm","$lastId")
        }
        val values = ContentValues()
        values.put("id", lastId+1)
        values.put("startTime", item.startTime)
        values.put("date", item.date)
        values.put("frequency", item.frequency)
        values.put("dianjitime", item.dianjitime)
        values.put("endTime",item.endTime)

        val updateQuery = "UPDATE ALastInsert SET id = ${lastId + 1};"

        db.execSQL(updateQuery)
        db.insert("HomeRecord", null, values)
        db.close()
        cursorr.close()
    }
    fun deleteAllDataFromTable() {
        val db = writableDatabase
        val tableName = "ALastInsert"
        val deleteQuery = "DELETE FROM $tableName"
        db.execSQL(deleteQuery)
        db.close()
    }
     fun updateHome(id:Int,count:Int,dianji:String)
     {
         val values = ContentValues()
         values.put("dianjitime",dianji)
         values.put("frequency",count)
         val db = writableDatabase
         db.update("HomeRecord", values, "id = ?", arrayOf(id.toString()))
         db.close()
     }
    fun getAllHomebydate(date:String):List<HomeRecord>{
        val items = mutableListOf<HomeRecord>()
        val db=readableDatabase
        val cursor = db.rawQuery("SELECT * FROM HomeRecord where date=\"$date\"", null)
        if (cursor.moveToFirst())
        {
            do {
                val iii=cursor.getColumnIndex("id")
                val id = cursor.getInt(iii)
                val bbb=cursor.getColumnIndex("startTime")
                val string1 = cursor.getString(bbb)
                val ccc=cursor.getColumnIndex("date")
                val string2 = cursor.getString(ccc)
                val xxx=cursor.getColumnIndex("frequency")
                val string3 = cursor.getInt(xxx)
                val zzz=cursor.getColumnIndex("dianjitime")
                val string4 = cursor.getString(zzz)
                val www=cursor.getColumnIndex("endTime")
                val string5=cursor.getString(www)
                val item = HomeRecord( id,string1, string2, string3, string4,string5)
                items.add(item)
            }    while (cursor.moveToNext())
        }
        else{
            cursor.close()
            db.close()
            return arrayListOf(HomeRecord(1,"0","$date",0,"d","d"))
        }
        cursor.close()
        db.close()
        return items
    }
    fun getAllHome():List<HomeRecord>{
        val items = mutableListOf<HomeRecord>()
        val db=readableDatabase
        val cursor = db.rawQuery("SELECT * FROM HomeRecord ORDER BY id ASC", null)
        if (cursor.moveToFirst()){
            do  {
                val iii=cursor.getColumnIndex("id")
                val id = cursor.getInt(iii)
                val bbb=cursor.getColumnIndex("startTime")
                val string1 = cursor.getString(bbb)
                val ccc=cursor.getColumnIndex("date")
                val string2 = cursor.getString(ccc)
                val xxx=cursor.getColumnIndex("frequency")
                val string3 = cursor.getInt(xxx)
                val zzz=cursor.getColumnIndex("dianjitime")
                val string4 = cursor.getString(zzz)
                val www=cursor.getColumnIndex("endTime")
                val string5=cursor.getString(www)
                val item = HomeRecord( id,string1, string2, string3, string4,string5)
                items.add(item)
            }   while (cursor.moveToNext())
        }else{
            cursor.close()
            db.close()
            return arrayListOf(HomeRecord(1,"0","9",0,"d","d"))
        }

        cursor.close()
        db.close()
        return items
    }
    fun getFirst(id: Int):HomeRecord{  //读取最新  的ID
        val db=readableDatabase
        val cursor = db.rawQuery("SELECT * FROM HomeRecord where id=$id", null)
        val item=HomeRecord(1,"00:00","0",0,"0","0")
        if (cursor.moveToFirst())
        {
            do{
                val iii=cursor.getColumnIndex("id")
                val id = cursor.getInt(iii)
                val bbb=cursor.getColumnIndex("startTime")
                val string1 = cursor.getString(bbb)
                val ccc=cursor.getColumnIndex("date")
                val string2 = cursor.getString(ccc)
                val xxx=cursor.getColumnIndex("frequency")
                val string3 = cursor.getInt(xxx)
                val zzz=cursor.getColumnIndex("dianjitime")
                val string4 = cursor.getString(zzz)
                val www=cursor.getColumnIndex("endTime")
                val string5=cursor.getString(www)
                val item = HomeRecord(id,string1, string2, string3, string4,string5)
            } while (cursor.moveToNext())
        }else{
            cursor.close()
            db.close()
            return item
        }

        return item
    }
    fun printTableData():Int {
        val db = readableDatabase
        val query = "SELECT * FROM ALastInsert"
        val cursor = db.rawQuery(query, null)
        var dog=1
        // 检查游标是否有效
        if (cursor != null) {
            // 移动游标到第一行
            if (cursor.moveToFirst()) {
                do {
                    // 遍历每一行，并打印每一列的数据
                    for (i in 0 until cursor.columnCount) {
                        val columnName = cursor.getColumnName(i)
                        val columnValue = cursor.getString(i)
                        println("$columnName: $columnValue")
                        dog=columnValue.toInt()
                    }
                    println("--------------------")
                } while (cursor.moveToNext())
            }

            // 关闭游标
            cursor.close()

        }

        // 关闭数据库连接
        db.close()
        return dog
    }
    fun printTableData1(){
        val db = readableDatabase
        val query = "SELECT id FROM HomeRecord"
        val cursor = db.rawQuery(query, null)
        // 检查游标是否有效

        if (cursor != null) {
            // 移动游标到第一行
            println("jingruyoubiao")
            if (cursor.moveToFirst()) {
                do {
                    // 遍历每一行，并打印每一列的数据
                    for (i in 0 until cursor.columnCount) {
                        val columnName = cursor.getColumnName(i)
                        val columnValue = cursor.getString(i)
                        println("$columnName++: $columnValue")
                    }
                    println("--------------------")
                } while (cursor.moveToNext())
            }

            // 关闭游标
            cursor.close()

        }

        // 关闭数据库连接
        db.close()

    }
    fun insertItem(item: TimeRecord) {
        val query = "SELECT id FROM LastInsertId"
        val db=readableDatabase
        val cursorr = db.rawQuery(query, null)
        var lastId = 0
        if (cursorr.moveToFirst()) {
            lastId = cursorr.getInt(0)
            Log.d("itemmm","$lastId")
        }

        val values = ContentValues()
        values.put("id", lastId+1)
        values.put("string1", item.startTime)
        values.put("string2", item.Howlong)
        values.put("string3", item.Jiange)
        values.put("string4", item.hurt)
        val updateQuery = "UPDATE LastInsertId SET id = ${lastId + 1};"
        db.execSQL(updateQuery)
        db.insert("items", null, values)
        db.close()
        cursorr.close()

    }
    fun getAllItemsbyDate(date: String):List<TimeRecord>
    {
        val items = mutableListOf<TimeRecord>()
        val db=readableDatabase
        val cursor = db.rawQuery("SELECT * FROM items", null)
        if (cursor.moveToFirst()){
            do {
                val iii=cursor.getColumnIndex("id")
                val id = cursor.getInt(iii)
                val bbb=cursor.getColumnIndex("string1")
                val string1 = cursor.getString(bbb)
                val ccc=cursor.getColumnIndex("string2")
                val string2 = cursor.getString(ccc)
                val xxx=cursor.getColumnIndex("string3")
                val string3 = cursor.getString(xxx)
                val zzz=cursor.getColumnIndex("string4")
                val string4 = cursor.getString(zzz)
                val item = TimeRecord( id,string1, string2, string3, string4)
                items.add(item)
            }while (cursor.moveToNext())
        }else{
            cursor.close()
            db.close()
            return arrayListOf(TimeRecord(1,"00:00","00:00","30'00''","点击选择"))
        }


        cursor.close()
        db.close()

        return items
    }


    fun getAllItems(): List<TimeRecord> {
        val items = mutableListOf<TimeRecord>()
        val db=readableDatabase
        val cursor = db.rawQuery("SELECT *FROM items ORDER BY id ASC", null)
        if (cursor.moveToFirst()){
            do {
                val iii=cursor.getColumnIndex("id")
                val id = cursor.getInt(iii)
                val bbb=cursor.getColumnIndex("string1")
                val string1 = cursor.getString(bbb)
                val ccc=cursor.getColumnIndex("string2")
                val string2 = cursor.getString(ccc)
                val xxx=cursor.getColumnIndex("string3")
                val string3 = cursor.getString(xxx)
                val zzz=cursor.getColumnIndex("string4")
                val string4 = cursor.getString(zzz)
                val item = TimeRecord( id,string1, string2, string3, string4)
                items.add(item)
            }while (cursor.moveToNext())
        }else{
            cursor.close()
            db.close()
            return arrayListOf(TimeRecord(1,"00:00","00:00","30'00''","点击选择"))
        }


        cursor.close()
        db.close()

        return items
    }
}