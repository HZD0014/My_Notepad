package com.lotte.android_notepad.ui.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.lotte.android_notepad.BaseActivity;
import com.lotte.android_notepad.R;
import com.lotte.android_notepad.Utils;
import com.lotte.android_notepad.db.MyNoteDBHelper;
import com.lotte.android_notepad.model.Note;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * 备忘详情页面
 */
public class NoteDetailActivity extends BaseActivity implements View.OnTouchListener {

    private static final int IMAGE_PICKER = 1001;
    private TextView tvTitle,tvAuthor,tvContent;//内容
    private TextView tvEdite,tvDelete,tvChange, tvReturn;//取消,保存
    //private TextView tvPlay, tvPause;//播放,暂停
    private ImageView ivContent;//图片内容
   // private VideoView vvContent;
    //private LinearLayout llVideoPlayer;//视频播放器布局

    private Note note;//备忘对象
    //private String id;
    private ArrayList<ImageItem> images;//选择完成返回的图片

    GestureDetector mGesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        note = (Note) getIntent().getSerializableExtra("note");

        initViews();
        setDataToView();
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvEdite = (TextView) findViewById(R.id.tvEdite);
        tvDelete = (TextView) findViewById(R.id.tvDelete);
        tvReturn = (TextView) findViewById(R.id.tvReturn);
        ivContent = (ImageView) findViewById(R.id.ivContent);

        tvEdite.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        tvReturn.setOnClickListener(this);
        //tvChange.setOnClickListener(this);
    }

    private void setDataToView() {
        tvTitle.setText(note.getTitle());
        tvAuthor.setText(note.getauthor());
        tvContent.setText(note.getContent());
        if (!TextUtils.isEmpty(note.getImagePath())) {
            ivContent.setVisibility(View.VISIBLE);
            Glide.with(this).load(new File(note.getImagePath())).into(ivContent);
        }
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvReturn:
                onBackPressed();
                break;
            case R.id.tvDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog alertDialog = builder.setTitle("提示").setMessage("是否删除该备忘?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteNote();
                                Toast.makeText(getApplicationContext(), "删除成功!", Toast.LENGTH_SHORT).show();
                                finish();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null).create();
                alertDialog.show();
                break;
            case R.id.tvEdite:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                AlertDialog alertDialog1 = builder1.setTitle("提示").setMessage("是否保存修改该备忘?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                String title = tvTitle.getText().toString().trim();
                String author = tvAuthor.getText().toString().trim();
                String content = tvContent.getText().toString().trim();
                if(author.length()==0){
                    author = "panzhenghao";
                    updateAuthor(author);
                }
                else if (title.length() <= 0 || content.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateDate(title,author,content,Utils.getTimeStr());
                Toast.makeText(getApplicationContext(), "保存成功!", Toast.LENGTH_SHORT).show();
                                finish();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null).create();
                alertDialog1.show();
        }
    }

    private void deleteNote() {
        writableDB.delete(MyNoteDBHelper.TABLE_NAME, MyNoteDBHelper.ID + "=" + note.getId(), null);
    }
    private void updateAuthor(String author){
        ContentValues cv = new ContentValues();
        cv.put(MyNoteDBHelper.AUTHOR, author);
        writableDB.update(MyNoteDBHelper.TABLE_NAME,cv,MyNoteDBHelper.ID + "=" + note.getId(),null);
    }
    private void updateDate(String title, String author, String content, String time) {
        ContentValues cv = new ContentValues();
        cv.put(MyNoteDBHelper.TITLE,title);
        cv.put(MyNoteDBHelper.AUTHOR, author);
        cv.put(MyNoteDBHelper.CONTENT, content);
        cv.put(MyNoteDBHelper.TIME, Utils.getTimeStr());
        writableDB.update(MyNoteDBHelper.TABLE_NAME,cv,MyNoteDBHelper.ID + "=" + note.getId(),null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mGesture == null) {
            mGesture = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return super.onScroll(e1, e2, distanceX, distanceY);
                }
            });
            mGesture.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    return false;
                }
            });
        }

        return mGesture.onTouchEvent(event);
   }
}
