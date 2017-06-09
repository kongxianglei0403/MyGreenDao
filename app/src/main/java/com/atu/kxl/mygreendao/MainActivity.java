package com.atu.kxl.mygreendao;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.atu.kxl.mygreendao.greendao.Note;
import com.atu.kxl.mygreendao.greendao.NoteDao;

import java.util.Date;
import java.util.List;
import java.util.Random;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

public class MainActivity extends ListActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private EditText text;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String textColumn = NoteDao.Properties.Text.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        cursor = getDb().query(getNoteDao().getTablename(), getNoteDao().getAllColumns(),
                null, null, null, null, orderBy);
        String[] from = {textColumn, NoteDao.Properties.Comment.columnName};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from,
                to);
        setListAdapter(adapter);

        text = (EditText) findViewById(R.id.text);
    }

    private SQLiteDatabase getDb() {
        return ((BaseApplication)this.getApplicationContext()).getDb();
    }

    public void onMyButtonClick(View view){
        switch (view.getId()){
            case R.id.add:
                addNote(view);
                break;
            case R.id.search:
                search(view);
                break;
            default:
                Snackbar.make(view,"what's wrong?",Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    private void search(View view) {
        String searchTxt = text.getText().toString().trim();
        if (null == searchTxt || ("".equals(searchTxt))){
            Snackbar.make(view,"Please enter a note to query",Snackbar.LENGTH_SHORT).show();
        }else{
            Query query = getNoteDao().queryBuilder().where(NoteDao.Properties.Text.eq(searchTxt))
                    .orderAsc(NoteDao.Properties.Date)
                    .build();
            List list = query.list();
            Snackbar.make(view,"there have" + list.size() + "records",Snackbar.LENGTH_SHORT).show();

        }
        //在querybuilder类中内置两个flag值用于方便输出执行sql语句和传递参数的值
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

    }

    private void addNote(View view) {
        String content = text.getText().toString().trim();
        text.setText("");
        Random random = new Random();
        int i = random.nextInt(50);
        String comment = "Added on " + i;
        if (content == null || content.equals("")){
            Snackbar.make(view,"Please enter a note to add",Snackbar.LENGTH_SHORT).show();
        }else {
            Note note = new Note(null,content,comment,new Date());
            getNoteDao().insert(note);
            Log.d(TAG, "Inserted new note, ID: " + note.getId());
            cursor.requery();
        }
    }

    public NoteDao getNoteDao() {
        return ((BaseApplication)this.getApplicationContext()).getSession().getNoteDao();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        getNoteDao().deleteByKey(id);
        Snackbar.make(v,"Delete note , ID" + id,Snackbar.LENGTH_SHORT).show();
        Log.d(TAG, "Deleted note, ID: " + id);
        cursor.requery();
    }
}
