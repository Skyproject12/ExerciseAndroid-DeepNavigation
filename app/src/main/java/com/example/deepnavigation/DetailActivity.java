package com.example.deepnavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

  TextView textTitle;
  TextView textMessage;
  public static  final String EXTRA_TITLE= "extra_title";
  public static  final String EXTRA_MESSAGE= "extra_message";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    textTitle= findViewById(R.id.text_title);
    textMessage= findViewById(R.id.text_message);

    String title= getIntent().getStringExtra(EXTRA_TITLE);
    String message= getIntent().getStringExtra(EXTRA_MESSAGE);

    textTitle.setText(title);
    textMessage.setText(message);

  }
}