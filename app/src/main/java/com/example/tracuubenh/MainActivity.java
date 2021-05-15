package com.example.tracuubenh;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter; //bỏ thư viện cursor adapter v4 rồi

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    SearchView search;

    static DatabaseHelper myDbHelper;
    static boolean databaseOpened = false; // db ban đầu chưa mở - > false


    SimpleCursorAdapter suggestionAdapter;

    ArrayList<History> historyList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter historyAdapter;

    RelativeLayout emptyHistory;
    Cursor cursorHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        search = (SearchView) findViewById(R.id.search_view);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setIconified(false);

            }
        });

        myDbHelper = new DatabaseHelper(this);
        if(myDbHelper.checkDatabase())
        {
            openDatabase();
        }
        else
        {
            LoadDatabaseAsync task = new LoadDatabaseAsync(MainActivity.this);
            task.execute();
        }


        //setup simple cur sò adapter
        final String[] from = new String[] {"tenbenh"};
        final int[] to = new int[]{R.id.suggestion_text};

        suggestionAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.suggestion_row, null, from, to,0)
        {
            @Override
            public void changeCursor(Cursor cursor)
            {
                super.swapCursor(cursor);
            }
        };

        search.setSuggestionsAdapter(suggestionAdapter);
        search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                CursorAdapter ca = search.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(position);
                String clicked_word = cursor.getString(cursor.getColumnIndex("tenbenh"));
                search.setQuery(clicked_word,false);
                search.clearFocus();
                search.setFocusable(false);

                Intent intent = new Intent(MainActivity.this, DinhNghiaBenh.class);
                Bundle bundle = new Bundle();
                bundle.putString("tenbenh", clicked_word);
                intent.putExtras(bundle);
                startActivity(intent);


                return true;
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String text = search.getQuery().toString();
                Cursor c  = myDbHelper.getMeaning(text);

                if(c.getCount()==0)
                {
                    search.setQuery("",false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
                    builder.setTitle("không tìm thấy bệnh");
                    builder.setMessage("Vui lòng tra cứu lại");

                    String positiveText = getString(android.R.string.ok);
                    builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    String negativeText = getString(android.R.string.cancel);
                    builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            search.clearFocus();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else
                {
                    search.clearFocus();
                    search.setFocusable(false);

                    Intent intent = new Intent(MainActivity.this, DinhNghiaBenh.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("tenbenh",text);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                search.setIconifiedByDefault(false); //give sug list margins
                Cursor cursorSuggestion = myDbHelper.getSuggestion(s);
                suggestionAdapter.changeCursor(cursorSuggestion);
                return false;
            }
        });

        emptyHistory = (RelativeLayout) findViewById(R.id.empty_history);
        //recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_history);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
//        fetch_history();

    }
    protected static void openDatabase()
    {
        try {
            myDbHelper.openDatabase();
            databaseOpened = true;
        }catch (SQLiteException e)
        {
            e.printStackTrace();
        }
    }

    private void fetch_history()
    {
        historyList = new ArrayList<>();
        historyAdapter = new RecyclerViewAdapterHistory(this, historyList);
        recyclerView.setAdapter(historyAdapter);
        History h;

        if(databaseOpened)
        {
            cursorHistory = myDbHelper.getHistory();
            do{
                h = new History(cursorHistory.getString(cursorHistory.getColumnIndex("word")));
                historyList.add(h);
            }while (cursorHistory.moveToNext());
        }
        historyAdapter.notifyDataSetChanged();
        if(historyAdapter.getItemCount()==0)
        {
            emptyHistory.setVisibility(View.VISIBLE);
        }else
        {
            emptyHistory.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id= item.getItemId();
        if(id ==R.id.action_settings)
        {
            Intent intent = new Intent(MainActivity.this, CaiDat.class);
            startActivity(intent);// nên để tên class tiếng anh, đổi ngôn ngữ = title
            return true;
        }
        if(id==R.id.action_exit)
        {
            System.exit(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//        fetch_history();
//    }
}