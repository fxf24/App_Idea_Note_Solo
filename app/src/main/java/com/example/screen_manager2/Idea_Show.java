package com.example.screen_manager2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class Idea_Show extends Dialog implements View.OnClickListener {
    TextView tv1, tv2, tv3;
    Button btn1;
    String title = "", desc = "", link = "";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.idea_show);

        tv1 = findViewById(R.id.title);
        tv2 = findViewById(R.id.description);
        tv3 = findViewById(R.id.references);

        tv1.setText(title);
        tv2.setText(desc);
        tv3.setText(link);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + tv3.getText())));
            }
        });

        btn1 = findViewById(R.id.confirm);

        btn1.setOnClickListener(this);
    }
    public Idea_Show(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm:
                dismiss();
                break;
        }
    }

    public void setDialog(String title, String desc, String link){
        this.title = title;
        this.desc = desc;
        this.link = link;
    }
}
