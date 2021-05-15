package com.example.tracuubenh;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

public class CaiDat extends AppCompatActivity {
    DatabaseHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cài đặt"); // constraint layout lỗi ko fix dc xài tạm cor

        toolbar.setNavigationIcon(getResources().getDrawable(17301593)); // sự sai trái của icon,nhét image view vào nó mới ra biểu tượng, chắc do dạng icon
//        TextView clearHistory = (TextView) findViewById(R.id.clear_history);
//        clearHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myDbHelper = new DatabaseHelper(CaiDat.this);
//                myDbHelper.openDatabase();
//                showAlertDialog();
//            }
//        });
 //   }

//    private void showAlertDialog()
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(CaiDat.this, R.style.MyDialogTheme);
//        builder.setTitle("Lịch sử tra cứu sẽ bị xoá ?");
//        builder.setMessage("Tất cả lịch sử sẽ bị xoá!!");
//
//        String positiveText = "Đồng ý";
//        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                myDbHelper.deleteHistory();
//            }
//        });
//
//        String negativeText = "Thôi không xoá";
//        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}