package com.lotte.android_notepad.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.lotte.android_notepad.BaseActivity;
import com.lotte.android_notepad.R;
import com.lotte.android_notepad.Utils;
import com.lotte.android_notepad.adapter.NoteListAdapter;
import com.lotte.android_notepad.db.MyNoteDBHelper;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.File;
import java.util.ArrayList;

public class AddNoteActivity extends BaseActivity {

    private static final int IMAGE_PICKER = 1001;
    //private static final int VIDEO_PICKER = 1002;

    private EditText etTitle;
    private EditText etAuthor;//作者
    private EditText etContent;//内容
    private TextView tvImage, tvCancel, tvSave;//图片,取消,保存
    private ImageView ivContent;
    private VideoView vvContent;

    private ArrayList<ImageItem> images;//选择完成返回的图片
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getSharedPreferences("author",MODE_PRIVATE);
        setContentView(R.layout.activity_add_note);
        initViews();
        String author = pref.getString("author","扈智栋");
        etAuthor.setText(author);
    }

    private void initViews() {
        etTitle = (EditText) findViewById(R.id.etTitle);
        etAuthor = (EditText) findViewById(R.id.etAuthor);
        etContent = (EditText) findViewById(R.id.etContent);
        tvImage = (TextView) findViewById(R.id.tvImage);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvSave = (TextView) findViewById(R.id.tvSave);
        ivContent = (ImageView) findViewById(R.id.ivContent);
        tvImage.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvImage:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.tvCancel://取消该备忘
                onBackPressed();
                break;
            case R.id.tvSave:
                String title = etTitle.getText().toString().trim();
                String author = etAuthor.getText().toString().trim();
                String content = etContent.getText().toString().trim();
                if (title.length() <= 0 || content.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                insertData(title,author,content);
                Toast.makeText(getApplicationContext(), "保存成功!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                    if (data != null && requestCode == IMAGE_PICKER) {
                        //获取到选择完成的图片
                        images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        //显示图片
                        ivContent.setVisibility(View.VISIBLE);
                        Glide.with(this).load(new File(images.get(0).path)).into(ivContent);
                    } else {
                        Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
                    }
        }
    }

    //插入数据
    public void insertAuthor(String author){
        ContentValues cv = new ContentValues();
        cv.put(MyNoteDBHelper.AUTHOR,author);
        writableDB.insert(MyNoteDBHelper.TABLE_NAME, null, cv);
    }
    private void insertData(String title, String author, String content) {
        ContentValues cv = new ContentValues();
        cv.put(MyNoteDBHelper.TITLE,title);
        cv.put(MyNoteDBHelper.AUTHOR,author);
        cv.put(MyNoteDBHelper.CONTENT, content);
        cv.put(MyNoteDBHelper.TIME, Utils.getTimeStr());
        if (null != images && images.size() > 0) {
            //存储图片路径
            cv.put(MyNoteDBHelper.IMAGE_PATH, images.get(0).path);
        }
        writableDB.insert(MyNoteDBHelper.TABLE_NAME, null, cv);
    }
}
