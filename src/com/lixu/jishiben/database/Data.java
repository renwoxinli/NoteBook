package com.lixu.jishiben.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "datas")
public class Data {

	public final static String USER_ID = "user_id";
	public final static String TITLE = "title";
	public final static String TEXT = "text";
	public final static String SYSTEM_TIME = "system_time";

	public Data() {

	}

	public Data(String text, String title, String time_text) {
		this.text = text;
		this.title = title;
		this.time_text = time_text;
	}

	@DatabaseField(generatedId = true, columnName = USER_ID)
	public int user_id;

	@DatabaseField(columnName = TITLE)
	public String title;

	@DatabaseField(columnName = TEXT)
	public String text;

	@DatabaseField(columnName = SYSTEM_TIME)
	public String time_text;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTime_text() {
		return time_text;
	}

	public void setTime_text(String string) {
		this.time_text = string;
	}

}