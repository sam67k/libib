package com.example.libib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RemoveBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_book);
    }

    public void removebook(View view) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("data").child("Books");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(((TextView) findViewById(R.id.id)).getText().toString())) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("data").
                            child("Books").
                            child(
                                    ((TextView) findViewById(R.id.id)).getText().toString()
                            );
                    myRef.removeValue();
                    Toast.makeText(RemoveBookActivity.this, "Book Removed Successfully", Toast.LENGTH_LONG).show();
                    ((TextView) findViewById(R.id.id)).setText("");
                } else {
                    Toast.makeText(RemoveBookActivity.this, "Invalid Book ID", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}