package com.example.studentdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    EditText editRollno, editName, editMarks;
    Button btnAdd, btnDelete, btnModify, btnView, btnViewAll, btnShowInfo;
    SQLiteDatabase sqLiteDatabase;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();

    }

    private void initComponent() {

        editRollno = findViewById(R.id.editRollno);
        editName = findViewById(R.id.editName);
        editMarks = findViewById(R.id.editMarks);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnModify = findViewById(R.id.btnModify);
        btnView = findViewById(R.id.btnView);
        btnViewAll = findViewById(R.id.btnViewall);
        btnShowInfo = findViewById(R.id.btnShow);

        operationOnComponent();

    }

    private void operationOnComponent() {

        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnViewAll.setOnClickListener(this);
        btnShowInfo.setOnClickListener(this);

        sqLiteDatabase = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:


                if(editRollno.getText().toString().trim().length()==0||
                        editName.getText().toString().trim().length()==0||
                        editMarks.getText().toString().trim().length()==0)
                {
                    Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    showMessage("Error", "Please enter all values");
                    return;
                }

                sqLiteDatabase.execSQL("INSERT INTO student VALUES('"+editRollno.getText()+"','"+editName.getText()+
                        "','"+editMarks.getText()+"');");
                showMessage("Success", "Record added");
                clearText();

                break;

            case R.id.btnDelete:

                if(editRollno.getText().toString().trim().length()==0)
                {
                    showMessage("Error", "Please enter Rollno");
                    return;
                }
                Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM student WHERE rollno='"+editRollno.getText()+"'", null);
                if(c.moveToFirst())
                {
                    sqLiteDatabase.execSQL("DELETE FROM student WHERE rollno='"+editRollno.getText()+"'");
                    showMessage("Success", "Record Deleted");
                }
                else
                {
                    showMessage("Error", "Invalid Rollno");
                }
                clearText();

                break;

            case R.id.btnModify:

                if(editRollno.getText().toString().trim().length()==0)
                {
                    showMessage("Error", "Please enter Rollno");
                    return;
                }
                c=sqLiteDatabase.rawQuery("SELECT * FROM student WHERE rollno='"+editRollno.getText()+"'", null);
                if(c.moveToFirst())
                {
                    sqLiteDatabase.execSQL("UPDATE student SET name='"+editName.getText()+"',marks='"+editMarks.getText()+
                            "' WHERE rollno='"+editRollno.getText()+"'");

                    showMessage("Success", "Record Modified");
                }
                else
                {
                    showMessage("Error", "Invalid Rollno");
                }
                clearText();

                break;

            case R.id.btnView:


                if(editRollno.getText().toString().trim().length()==0)
                {
                    showMessage("Error", "Please enter Rollno");
                    return;
                }
                c=sqLiteDatabase.rawQuery("SELECT * FROM student WHERE rollno='"+editRollno.getText()+"'", null);
                if(c.moveToFirst())
                {
                    editName.setText(c.getString(1));
                    editMarks.setText(c.getString(2));
                }
                else
                {
                    showMessage("Error", "Invalid Rollno");
                    clearText();
                }

                break;

            case R.id.btnViewall:

                c=sqLiteDatabase.rawQuery("SELECT * FROM student", null);
                if(c.getCount()==0)
                {
                    showMessage("Error", "No records found");
                    return;
                }
                StringBuffer buffer=new StringBuffer();
                while(c.moveToNext())
                {
                    buffer.append("Rollno: "+c.getString(0)+"\n");
                    buffer.append("Name: "+c.getString(1)+"\n");
                    buffer.append("Marks: "+c.getString(2)+"\n\n");
                }
                showMessage("Student Details", buffer.toString());

                break;

            case R.id.btnShow:
                showMessage("Student Record Application", "Developed By Chandan Prasad");
                break;
        }
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
        editRollno.setText("");
        editName.setText("");
        editMarks.setText("");
        editRollno.requestFocus();
    }
}

