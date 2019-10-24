package com.example.screen_manager2;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements List_Adapter.ListitemPosition {
    ListView idea_list;
    ArrayList<Idea_Data> list_data;
    List_Adapter adapter;
    int i = 1;
    String today;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        today = sd.format(new Date(System.currentTimeMillis()));

        idea_list = findViewById(R.id.idea_list);
        list_data = new ArrayList<>();
        adapter = new List_Adapter(this, list_data);
        idea_list.setAdapter(adapter);

        idea_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("test", "item long clickec : " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("삭제")
                    .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteThread deleteThread = new DeleteThread(list_data.get(position).idx);
                                deleteThread.start();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();

                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Idea_Dialog id = new Idea_Dialog(MainActivity.this);
                id.setCustomDialogListener(new Idea_Dialog.CustomDialogListener() {
                    @Override
                    public void onPositiveClick(String title, String desc, String link) {
                        UploadThread ut = new UploadThread(title, desc, link);
                        ut.start();
                    }
                    @Override
                    public void onNegativeClick() {
                        Log.d("test", "cancle");
                    }
                });
                id.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        GetListThread glt = new GetListThread();
        glt.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.trigger) {
            if(i == 1){
                for(int j = 0; j<list_data.size(); j++){
                    Idea_Data cg= list_data.get(j);
                    cg.visibility = View.VISIBLE;
                    list_data.set(j, cg);
                }
                adapter.notifyDataSetChanged();
                item.setTitle("편집취소");
                i = 0;
            }
            else{
                for(int j = 0; j<list_data.size(); j++){
                    Idea_Data cg= list_data.get(j);
                    cg.visibility = View.GONE;
                    list_data.set(j, cg);
                }
                adapter.notifyDataSetChanged();
                item.setTitle("편집");
                i = 1;
            }
            Log.d("test","change" );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showClickPosition(final int position) {
        Idea_Dialog idea_dialog = new Idea_Dialog(MainActivity.this);
        idea_dialog.setDialog(list_data.get(position).getTitle(),list_data.get(position).getMemo(),list_data.get(position).getLink());
        idea_dialog.setCustomDialogListener(new Idea_Dialog.CustomDialogListener() {
            @Override
            public void onPositiveClick(String title, String desc, String link) {
                UpdateThread updateThread = new UpdateThread(title, desc, link, String.valueOf(list_data.get(position).idx));
                updateThread.start();

                ActionMenuItemView menuItem =findViewById(R.id.trigger);
                menuItem.setTitle("편집");
                i=1;
            }
            @Override
            public void onNegativeClick() {
                Log.d("test", "cancel");
            }
        });
        idea_dialog.show();
    }

    class UploadThread extends Thread{
        String title, memo, link;
        public UploadThread(String title, String memo, String link){
            this.title = title;
            this.memo = memo;
            this.link = link;
        }
        @Override
        public void run() {
            super.run();

            try{
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder = builder.url("http://192.168.122.33:8080/NetworkProjectServer/upload.jsp");

                FormBody.Builder builder2 = new FormBody.Builder();
                builder2.add("idea_title", title);
                builder2.add("idea_desc", memo);
                builder2.add("idea_link", link);
                builder2.add("idea_date", today);

                FormBody body = builder2.build();
                builder = builder.post(body);

                Request request = builder.build();
                Call call=client.newCall(request);
                call.execute();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onResume();
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class UpdateThread extends Thread{
        String title, memo, link, idx;
        public UpdateThread(String title, String memo, String link, String idx){
            this.title = title;
            this.memo = memo;
            this.link = link;
            this.idx = idx;
        }
        @Override
        public void run() {
            super.run();

            try{
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder = builder.url("http://192.168.122.33:8080/NetworkProjectServer/update_list.jsp");

                FormBody.Builder builder2 = new FormBody.Builder();
                builder2.add("idea_title", title);
                builder2.add("idea_desc", memo);
                builder2.add("idea_link", link);
                builder2.add("idea_date", today);
                builder2.add("idea_idx", idx);

                FormBody body = builder2.build();
                builder = builder.post(body);

                Request request = builder.build();
                Call call=client.newCall(request);
                call.execute();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onResume();
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class GetListThread extends Thread{
        @Override
        public void run() {
            super.run();
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder = builder.url("http://192.168.122.33:8080/NetworkProjectServer/get_list.jsp");
            Request request = builder.build();
            Call call=client.newCall(request);
            NetworkDataCallback nc = new NetworkDataCallback();
            call.enqueue(nc);
        }
    }

    class DeleteThread extends Thread{
        int idx;
        public DeleteThread(int idx){
            this.idx = idx;
        }
        @Override
        public void run() {
            super.run();
            try{
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder = builder.url("http://192.168.122.33:8080/NetworkProjectServer/delete_list.jsp");

                FormBody.Builder builder2 = new FormBody.Builder();
                builder2.add("idea_idx", String.valueOf(idx));

                FormBody body = builder2.build();
                builder = builder.post(body);

                Request request = builder.build();
                Call call=client.newCall(request);
                call.execute();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onResume();
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    class NetworkDataCallback implements Callback{

        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("test", e.toString());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                String result = response.body().string();

                list_data.clear();

                JSONArray root = new JSONArray(result);

                for(int i =0; i<root.length(); i++){
                    JSONObject obj = root.getJSONObject(i);

                    Idea_Data id = new Idea_Data(obj.getString("idea_title"),obj.getString("idea_desc"),obj.getString("idea_link"),obj.getString("idea_date"));
                    id.idx = obj.getInt("idea_idx");
                    id.visibility = View.GONE;
                    list_data.add(id);

                    Log.d("test", obj.getString("idea_title"));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
