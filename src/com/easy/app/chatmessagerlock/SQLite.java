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
	private static final String DATABASE_NAME = "betterBrowser";	//��Ʈw�W��
	private static final int DATABASE_VERSION = 1;	//��Ʈw����
	public static final String MSG = "MSG";
	public static final String SERVICENAME = "SNAME";
	public static final String DATE = "DATE";
 
	private SQLiteDatabase db;
 
	public SQLite(Context context) {	//�غc�l
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
		//�إ�config��ƪ�A�Ա��аѦ�SQL�y�k
		db.execSQL(DATABASE_CREATE_TABLE);
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//oldVersion=�ª���Ʈw�����FnewVersion=�s����	�Ʈw����
		db.execSQL("DROP TABLE IF EXISTS betterBrowser");	//�R���¦�����ƪ�
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
	// ���o�@������
	public Cursor get(long rowId) throws SQLException {
		Cursor cursor = db.query(true,
		"table_name",				//��ƪ�W��
		new String[] {"_ID", "name", "value"},	//���W��
		"_ID=" + rowId,				//WHERE
		null, // WHERE ���Ѽ�
		null, // GROUP BY
		null, // HAVING
		null, // ORDOR BY
		null  // ����^�Ǫ�rows�ƶq
		);
 
		// �`�N�G���g�|�X��
		if (cursor != null) {
			cursor.moveToFirst();	//�N���в���Ĥ@�����
		}
		return cursor;
	}
	//�s�W�@���O���A���\�^��rowID�A���Ѧ^��-1
	public long create(String msg, String serviceName) throws Exception{
		ContentValues args = new ContentValues();
		args.put("msg", msg);
		args.put("serviceName", serviceName);
		
		return db.insert("betterBrowser", null, args);
    }
	//�R���O���A�^�Ǧ��\�R������
	public int delete(long rowId) {
		return db.delete("table_name",	//��ƪ�W��
		"_ID=" + rowId,			//WHERE
		null				//WHERE���Ѽ�
		);
	}
	//�ק�O���A�^�Ǧ��\�קﵧ��
	public int update(long rowId, String value) {
		ContentValues args = new ContentValues();
		args.put("value", value);
 
		return db.update("table_name",	//��ƪ�W��
		args,				//VALUE
		"_ID=" + rowId,			//WHERE
		null				//WHERE���Ѽ�
		);
	}
}
