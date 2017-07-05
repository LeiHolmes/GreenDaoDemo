package com.holmeslei.greendaodemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.holmeslei.greendaodemo.database.DaoSession;
import com.holmeslei.greendaodemo.database.NotePointDao;
import com.holmeslei.greendaodemo.model.NotePoint;
import com.holmeslei.greendaodemo.util.GreenDaoUtil;

import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private TextView tvText;
    private NotePointDao notePointDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        DaoSession daoSession = GreenDaoUtil.getDaoSession();
        notePointDao = daoSession.getNotePointDao();
        quary();
    }

    private void initView() {
        tvText = (TextView) findViewById(R.id.main_text);
    }

    private void initListener() {
        findViewById(R.id.main_insert).setOnClickListener(this);
        findViewById(R.id.main_delete).setOnClickListener(this);
        findViewById(R.id.main_delete_all).setOnClickListener(this);
        findViewById(R.id.main_update).setOnClickListener(this);
        findViewById(R.id.main_quary).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_insert:
                insert();
                break;
            case R.id.main_delete:
                delete();
                break;
            case R.id.main_delete_all:
                deleteAll();
                break;
            case R.id.main_update:
                update();
                break;
            case R.id.main_quary:
                quary();
                break;
        }
    }

    /**
     * 增
     * id传null回自动设置自增长
     */
    private void insert() {
        NotePoint notePoint1 = new NotePoint(null, 0, 55f, 100f, 123456f, 0, 122f, 1, 1);
        NotePoint notePoint2 = new NotePoint(null, 0, 56f, 99f, 123457f, 0, 132f, 1, 2);
        NotePoint notePoint3 = new NotePoint(null, 0, 57f, 98f, 123458f, 0, 152f, 2, 2);
        NotePoint notePoint4 = new NotePoint(null, 0, 58f, 97f, 123459f, 0, 202f, 2, 2);
        NotePoint notePoint5 = new NotePoint(null, 0, 59f, 96f, 123460f, 0, 102f, 3, 2);
        NotePoint notePoint6 = new NotePoint(null, 0, 60f, 95f, 123461f, 0, 11f, 3, 3);
        notePointDao.insert(notePoint1);
        notePointDao.insert(notePoint2);
        notePointDao.insert(notePoint3);
        notePointDao.insert(notePoint4);
        notePointDao.insert(notePoint5);
        notePointDao.insert(notePoint6);
        quary();
    }

    /**
     * 删
     * deleteBykey(Long key) ：根据主键删除一条记录。
     * delete(User entity) ：根据实体类删除一条记录，一般结合查询方法，查询出一条记录之后删除。
     * deleteAll()： 删除所有记录。
     */
    private void delete() {
        List<NotePoint> deleteList = notePointDao.queryBuilder().where(NotePointDao.Properties.PageIndex.eq(2)).build().list();
        if (deleteList != null) {
            for (NotePoint notePoint : deleteList) {
                notePointDao.deleteByKey(notePoint.getId());
            }
        } else {
            Log.e("greendao_test", "delete:deleteList为空");
        }
        quary();
    }

    private void deleteAll() {
        notePointDao.deleteAll();
        quary();
    }

    /**
     * 改
     */
    private void update() {
        List<NotePoint> updateList = notePointDao.queryBuilder().where(NotePointDao.Properties.PageIndex.eq(3)).build().list();
        if (updateList != null) {
            for (NotePoint notePoint : updateList) {
                notePoint.setPageIndex(2);
                notePointDao.update(notePoint);
            }
        } else {
            Log.e("greendao_test", "update:updateList为空");
        }
        quary();
    }

    /**
     * 查
     * loadAll()：查询所有记录
     * load(Long key)：根据主键查询一条记录
     * queryBuilder().list()：返回：List
     * queryBuilder().where(UserDao.Properties.Name.eq("")).l
     * queryRaw(String where,String selectionArg)：返回：List
     */
    private void quary() {
        List<NotePoint> quaryList = notePointDao.queryBuilder().list();
        if (quaryList != null)
            tvText.setText(quaryList.toString());
        else {
            Log.e("greendao_test", "quary:quaryList为空");
        }
    }

}
