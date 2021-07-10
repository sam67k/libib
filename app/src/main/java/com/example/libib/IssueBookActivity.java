package com.example.libib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;

public class IssueBookActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_book);
        mAuth = FirebaseAuth.getInstance();
    }

    public void issuebook(View view) {
        String ID = ((EditText)findViewById(R.id.bookid)).getText().toString();
        String email = ((EditText)findViewById(R.id.userid)).getText().toString();
        String password = ((EditText)findViewById(R.id.passcode)).getText().toString();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("data").child("Books");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(((TextView) findViewById(R.id.bookid)).getText().toString())) {
                    quantity = Integer.parseInt(snapshot.child(((TextView) findViewById(R.id.bookid)).getText().toString()).child("BookQuantity").getValue().toString());
                    if (quantity > 0) {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(IssueBookActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            String EMAIL = user.getEmail();
                                            String[] arr = EMAIL.split("@",2);
                                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("data").child("Students");
                                            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot snapshot) {
                                                    if (snapshot.hasChild(arr[0])) {
                                                        Toast.makeText(IssueBookActivity.this, "Book Issue Limit Reached", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("data").
                                                                child("Students").
                                                                child(arr[0]).
                                                                child("IssuedBook");
                                                        myRef.setValue(ID);
                                                        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference("data").
                                                                child("Books").
                                                                child(ID).
                                                                child("BookQuantity");
                                                        quantity--;
                                                        String Quantity = ""+quantity;
                                                        Ref.setValue(Quantity);
                                                        Toast.makeText(IssueBookActivity.this, "Book Issued Successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) { }
                                            });
                                        } else {
                                            Toast.makeText(IssueBookActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        mAuth.signOut();
                    } else {
                        Toast.makeText(IssueBookActivity.this, "Book Quantity Less than 1", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(IssueBookActivity.this, "Invalid Book ID", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}