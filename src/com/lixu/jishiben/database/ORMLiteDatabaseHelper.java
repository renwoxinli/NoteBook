package com.lixu.jishiben.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class ORMLiteDatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static ORMLiteDatabaseHelper mDatabaseHelper = null;
	private Dao<Data, Integer> mUserDao = null;

	private final static String DataBase_NAME = "text.db";
	private final static int DataBase_VERSION = 1;

	public ORMLiteDatabaseHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion) {
		super(context, DataBase_NAME, factory, DataBase_VERSION);
	}

	public static ORMLiteDatabaseHelper getInstance(Context context) {
		if (mDatabaseHelper == null) {
			mDatabaseHelper = new ORMLiteDatabaseHelper(context, DataBase_NAME, null, DataBase_VERSION);
		}

		return mDatabaseHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource connectionSource) {

		try {
			TableUtils.createTableIfNotExists(connectionSource, Data.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource arg1, int arg2, int arg3) {

	}

	/**
	 * 每一个数据库中的表，要有一个获得Dao的方法。 可以使用一种更通用的模板方法如：
	 * 
	 * public Dao<Class, Integer> getORMLiteDao(Class cls) throws SQLException {
	 * if (dao == null) { dao = getDao(cls); }
	 * 
	 * return dao; }
	 */
	public Dao<Data, Integer> getUserDao() {
		if (mUserDao == null) {
			try {
				mUserDao = getDao(Data.class);
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}

		return mUserDao;
	}

	@Override
	public void close() {
		super.close();
		mUserDao = null;
	}
}