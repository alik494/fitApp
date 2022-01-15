package com.sumin.notes;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Activity_faq extends AppCompatActivity {
    private Button btnCopy;
    private EditText edtCopy;

    ClipboardManager clipboardManager;
    ClipData clipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        btnCopy=findViewById(R.id.buttonCopy);
        edtCopy=findViewById(R.id.edtCopy);

        clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "@string/edtAddresBit";
                clipData = ClipData.newPlainText("text",text);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(Activity_faq.this, "Text Copied", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onClickCopy(View view) {


    }
}
