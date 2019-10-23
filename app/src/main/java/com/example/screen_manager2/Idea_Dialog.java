package com.example.screen_manager2;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class Idea_Dialog extends Dialog implements View.OnClickListener{
    EditText et1, et2, et3;
    Button btn1, btn2;

    private CustomDialogListener dialogListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.idea_dialog);

        et1 = findViewById(R.id.idea_title);
        et2 = findViewById(R.id.idea_description);
        et3 = findViewById(R.id.reference);

        btn1 = findViewById(R.id.positive);
        btn2 = findViewById(R.id.negative);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);


    }

    public Idea_Dialog(@NonNull Context context){
        super(context);
    }

    public void setCustomDialogListener(CustomDialogListener dialogListener){
        this.dialogListener = dialogListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.positive:
                String title = et1.getText().toString();
                String desc = et2.getText().toString();
                String link = et3.getText().toString();
                dialogListener.onPositiveClick(title, desc, link);
                dismiss();
                break;
            case R.id.negative:
                dismiss();
                break;
        }
    }

    public interface CustomDialogListener{
        void onPositiveClick(String title, String desc, String link);
        void onNegativeClick();
    }


}
