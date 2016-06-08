package com.example.kangjonghyuk.calendar_0603;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kangjonghyuk on 2016. 6. 6..
 */
public class MemoPage extends Activity implements View.OnClickListener{

    myDB my;
    SQLiteDatabase sql;

    Button btnAdd, btnCancel, btnChange, btnRemove;
    TextView tvDate;
    EditText editMemo;
    int m_year, m_month;
    String m_date, str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memolayout);

        my = new myDB(this);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        btnChange = (Button)findViewById(R.id.btnChange);
        btnChange.setOnClickListener(this);
        btnRemove = (Button)findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(this);
        tvDate = (TextView)findViewById(R.id.tvDate);
        editMemo = (EditText)findViewById(R.id.editMemo);

        Intent intent = getIntent();
        m_year = intent.getIntExtra("YEAR",0);
        m_month = intent.getIntExtra("MONTH",1);
        m_date = intent.getStringExtra("DATE");

        tvDate.setText(m_year+"."+m_month+"."+m_date);

        str = String.valueOf(m_year)+String.valueOf(m_month)+m_date;

        sql = my.getReadableDatabase();
        Cursor cursor;
        cursor = sql.rawQuery("SELECT * FROM MEMO WHERE date="+str+";",null);
        String str1 = "";
        while(cursor.moveToNext()){
            str1 += cursor.getString(1);
        }

        if(str1.length()==0){
            btnAdd.setVisibility(View.VISIBLE);
            btnChange.setVisibility(View.INVISIBLE);
            btnRemove.setVisibility(View.INVISIBLE);
        } else {
            btnAdd.setVisibility(View.INVISIBLE);
            btnChange.setVisibility(View.VISIBLE);
            btnRemove.setVisibility(View.VISIBLE);
        }
        editMemo.setText(str1);
        cursor.close();
        sql.close();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnCancel :
                finish();
                break;

            case R.id.btnAdd :
                sql = my.getWritableDatabase();
                sql.execSQL("INSERT INTO memo VALUES("+str+",'"
                    +editMemo.getText().toString()+"');");
                sql.close();
                Toast.makeText(getApplicationContext(),"저장되었습니다", Toast.LENGTH_SHORT).show();

                finish();
                break;

            case R.id.btnChange :
                sql = my.getWritableDatabase();
                sql.execSQL("UPDATE memo SET story="+"'"+editMemo.getText().toString()+"'"+" WHERE date="+str+";");
                sql.close();
                Toast.makeText(getApplicationContext(),"수정되었습니다",Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.btnRemove :
                sql = my.getWritableDatabase();
                sql.execSQL("DELETE FROM memo where date="+str+";");
                sql.close();
                Toast.makeText(getApplicationContext(),"삭제되었습니다",Toast.LENGTH_SHORT).show();
                finish();
                break;

        }
    }

}
