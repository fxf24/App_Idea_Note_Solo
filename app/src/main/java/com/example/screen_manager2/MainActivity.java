package com.example.screen_manager2;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity {
    ListView idea_list;
    ArrayList<Idea_Data> list_data;
    List_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        final String today = sd.format(new Date(System.currentTimeMillis()));

        idea_list = findViewById(R.id.idea_list);
        list_data = new ArrayList<>();
        adapter = new List_Adapter(this, list_data);
        idea_list.setAdapter(adapter);

//        GetListThread glt = new GetListThread();
//        glt.start();

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
                        list_data.add(new Idea_Data(title,desc,link,today));
                        adapter.notifyDataSetChanged();
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
        if (id == R.id.action_settings) {
            Toast.makeText(this,"setting" ,Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                builder = builder.url("http://192.168.122.72:8080/NetworkProjectServer/upload.jsp");

                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                String today = sd.format(new Date(System.currentTimeMillis()));
                FormBody.Builder builder2 = new FormBody.Builder();
                builder2.add("idea_title", title);
                builder2.add("idea_desc", memo);
                builder2.add("idea_link", link);
                builder2.add("idea_date", today);

                FormBody body = builder2.build();
                builder = builder.post(body);

                Request request = builder.build();
                Call call=client.newCall(request);
                NetworkCallback nc = new NetworkCallback();
                call.enqueue(nc);
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
            builder = builder.url("http://192.168.122.72:8080/NetworkProjectServer/get_list.jsp");
            Request request = builder.build();
            Call call=client.newCall(request);
            NetworkDataCallback nc = new NetworkDataCallback();
            call.enqueue(nc);
        }
    }
    class NetworkCallback implements Callback{

        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("test", "fail");
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try{
                String result = response.body().string();

                if(result.trim().equals("OK")){

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class NetworkDataCallback implements Callback{

        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                String result = response.body().string();

                list_data.clear();

                JSONArray root = new JSONArray(result);

                for(int i =0; i<root.length(); i++){
                    JSONObject obj = root.getJSONObject(i);

                    list_data.add(new Idea_Data(obj.getString("idea_title"),obj.getString("idea_desc"),obj.getString("idea_link"),obj.getString("idea_date")));
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
