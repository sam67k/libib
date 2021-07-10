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

public class AddBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
    }

    public void addbook(View view) {

        Book book = new Book(
                ((TextView) findViewById(R.id.id)).getText().toString(),
                ((TextView) findViewById(R.id.title)).getText().toString(),
                ((TextView) findViewById(R.id.quantity)).getText().toString());

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("data").child("Books");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(((TextView) findViewById(R.id.id)).getText().toString())) {
                    Toast.makeText(AddBookActivity.this, "Book Already Exists!", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("data")
                            .child("Books")
                            .child(
                                    ((TextView) findViewById(R.id.id)).getText().toString()
                                    );
                    myRef.setValue(book);

                    Toast.makeText(AddBookActivity.this, "Book Added Successfully", Toast.LENGTH_LONG).show();

                    ((TextView) findViewById(R.id.id)).setText("");
                    ((TextView) findViewById(R.id.title)).setText("");
                    ((TextView) findViewById(R.id.quantity)).setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }


    class Book {

        public String BookID, BookTitle, BookQuantity;

        public Book() {}

        public Book(String id, String title, String quantity) {
            this.BookID = id;
            this.BookTitle = title;
            this.BookQuantity = quantity;
        }

    }
}