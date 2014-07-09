package com.easy.app.chatmessagerlock;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class SQLite extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "betterBrowser";	//資料庫名稱
	private static final int DATABASE_VERSION = 1;	//資料庫版本
	public static final String MSG = "MSG";
	public static final String SERVICENAME = "SNAME";
	public static final String DATE = "DATE";
 
	private SQLiteDatabase db;
 
	public SQLite(Context context) {	//建構子
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = this.getWritableDatabase();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		String DATABASE_CREATE_TABLE =
		    "create table betterBrowser ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
		        + "msg TEXT ,"
		        + "serviceName TEXT"
		    + ");";
		//建立config資料表，詳情請參考SQL語法
		db.execSQL(DATABASE_CREATE_TABLE);
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//oldVersion=舊的資料庫版本；newVersion=新的資	料庫版本
		db.execSQL("DROP TABLE IF EXISTS betterBrowser");	//刪除舊有的資料表
		onCreate(db);
	}
	public int getCount() throws Exception{
		Cursor cursor = null;
		int count = 0;
		try{
			cursor = db.rawQuery("SELECT * FROM betterBrowser", null);
			count = cursor.getCount();
		}finally{
			cursor.close();
		}
		return count;
	}
	public Cursor getIdDone(String msg){
		return db.rawQuery("SELECT * FROM betterBrowser where msg like '%"+msg+"%'", null);
	}
	public Cursor getLatestId(){
		return db.rawQuery("SELECT id FROM betterBrowser order by id DESC limit 1", null);
	}
	public Cursor getAll() {
	    return db.rawQuery("SELECT * FROM betterBrowser order by id DESC", null);
	}
	public Cursor getServicesData(String serviecName)throws Exception{
		 return db.rawQuery("SELECT * FROM betterBrowser where serviceName ='"+ serviecName+"' order by id DESC limit 9999",null);
	}
	public int deleteAllData(){
		
		try {
			Log.d("SQLITE","deleteAllData:"+String.valueOf(getCount()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 return db.delete("betterBrowser", null, null);
	}
	public int deleteData(String condition){
		return db.delete("betterBrowser", condition, null);
	}
	public Cursor queryDataByCount(int count)throws Exception{
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT * FROM betterBrowser  order by id DESC limit " + count , null);
		//Log.v("SQLITE", "cursor.getCount()"+cursor.getCount());
		return cursor;
	}
	// 取得一筆紀錄
	public Cursor get(long rowId) throws SQLException {
		Cursor cursor = db.query(true,
		"table_name",				//資料表名稱
		new String[] {"_ID", "name", "value"},	//欄位名稱
		"_ID=" + rowId,				//WHERE
		null, // WHERE 的參數
		null, // GROUP BY
		null, // HAVING
		null, // ORDOR BY
		null  // 限制回傳的rows數量
		);
 
		// 注意：不寫會出錯
		if (cursor != null) {
			cursor.moveToFirst();	//將指標移到第一筆資料
		}
		return cursor;
	}
	//新增一筆記錄，成功回傳rowID，失敗回傳-1
	public long create(String msg, String serviceName) throws Exception{
		ContentValues args = new ContentValues();
		args.put("msg", msg);
		args.put("serviceName", serviceName);
		
		return db.insert("betterBrowser", null, args);
    }
	//刪除記錄，回傳成功刪除筆數
	public int delete(long rowId) {
		return db.delete("table_name",	//資料表名稱
		"_ID=" + rowId,			//WHERE
		null				//WHERE的參數
		);
	}
	//修改記錄，回傳成功修改筆數
	public int update(long rowId, String value) {
		ContentValues args = new ContentValues();
		args.put("value", value);
 
		return db.update("table_name",	//資料表名稱
		args,				//VALUE
		"_ID=" + rowId,			//WHERE
		null				//WHERE的參數
		);
	}
}
