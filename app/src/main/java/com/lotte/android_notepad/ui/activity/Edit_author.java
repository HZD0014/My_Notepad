package com.lotte.android_notepad.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lotte.android_notepad.BaseActivity;
import com.lotte.android_notepad.R;

public class Edit_author extends BaseActivity {
    private EditText editText;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_author);
        initViews();
    }
    private void initViews(){
        editText = (EditText) findViewById(R.id.author);
        button = (Button) findViewById(R.id.save_author);
        button.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.save_author:
                String author = editText.getText().toString().trim();
                SharedPreferences.Editor editor = getSharedPreferences("author",MODE_PRIVATE).edit();
                editor.putString("author",author);
                editor.apply();
                Toast.makeText(getApplicationContext(), "修改成功!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}