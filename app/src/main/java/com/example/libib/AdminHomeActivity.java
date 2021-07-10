package com.example.libib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void allbooksactivity(View view) {
        startActivity(new Intent(this, AllBooksActivity.class));
    }

    public void addbookactivity(View view) {
        startActivity(new Intent(this, AddBookActivity.class));
    }

    public void updatebookactivity(View view) {
        startActivity(new Intent(this, UpdateBookActivity.class));
    }

    public void removebookactivity(View view) {
        startActivity(new Intent(this, RemoveBookActivity.class));
    }

    public void issuebookactivity(View view) {
        startActivity(new Intent(this, IssueBookActivity.class));
    }

    public void returnbookactivity(View view) {
        startActivity(new Intent(this, ReturnBookActivity.class));
    }

    public void seeissuedbooksactivity(View view) {
        startActivity(new Intent(this, IssuedBooksActivity.class));
    }

    public void logoutactivity(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }
}