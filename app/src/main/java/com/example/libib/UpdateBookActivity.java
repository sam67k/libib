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

public class UpdateBookActivity extends AppCompatActivity {

    Book oldbook, newbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);
    }

    public void updatebook(View view) {

        oldbook = new Book(
                ((TextView) findViewById(R.id.oldid)).getText().toString(),
                ((TextView) findViewById(R.id.oldtitle)).getText().toString(),
                ((TextView) findViewById(R.id.oldquantity)).getText().toString());

        newbook = new Book(
                ((TextView) findViewById(R.id.newid)).getText().toString(),
                ((TextView) findViewById(R.id.newtitle)).getText().toString(),
                ((TextView) findViewById(R.id.newquantity)).getText().toString());

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("data").child("Books");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String quantity = snapshot.child(((TextView) findViewById(R.id.oldid)).getText().toString()).child("BookQuantity").getValue().toString();
                String title = snapshot.child(((TextView) findViewById(R.id.oldid)).getText().toString()).child("BookTitle").getValue().toString();
                if (snapshot.hasChild(((TextView)findViewById(R.id.oldid)).getText().toString()) &&
                        quantity.equals(oldbook.BookQuantity) &&
                        title.equals(oldbook.BookTitle)) {
                    if (snapshot.hasChild(((TextView) findViewById(R.id.newid)).getText().toString())) {
                        if (oldbook.BookID.equals(newbook.BookID) &&
                                oldbook.BookTitle.equals(newbook.BookTitle) &&
                                oldbook.BookQuantity.equals(newbook.BookQuantity)) {
                            Toast.makeText(UpdateBookActivity.this, "New Book ID Already Exits", Toast.LENGTH_LONG).show();
                        } else {
                            update();
                        }
                    } else {
                        update();
                    }
                } else {
                    Toast.makeText(UpdateBookActivity.this, "Invalid Old Book Details", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    private void update() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("data").
                child("Books").
                child(
                        ((TextView) findViewById(R.id.oldid)).getText().toString()
                );
        if(((TextView) findViewById(R.id.oldid)).getText().toString() != ((TextView) findViewById(R.id.newid)).getText().toString()) {
            myRef.removeValue();
            DatabaseReference tmpRef = database.getReference("data").
                    child("Books").
                    child(
                            ((TextView) findViewById(R.id.newid)).getText().toString()
                    );
            tmpRef.setValue(newbook);
        } else { myRef.setValue(newbook); }

        Toast.makeText(UpdateBookActivity.this, "Book Updated Successfully", Toast.LENGTH_LONG).show();

        ((TextView) findViewById(R.id.oldid)).setText("");
        ((TextView) findViewById(R.id.oldtitle)).setText("");
        ((TextView) findViewById(R.id.oldquantity)).setText("");

        ((TextView) findViewById(R.id.newid)).setText("");
        ((TextView) findViewById(R.id.newtitle)).setText("");
        ((TextView) findViewById(R.id.newquantity)).setText("");
    }

    class Book {

        public String BookID, BookTitle, BookQuantity;

        public Book() { }

        public Book(String id, String title, String quantity) {
            this.BookID = id;
            this.BookTitle = title;
            this.BookQuantity = quantity;
        }
    }
}
