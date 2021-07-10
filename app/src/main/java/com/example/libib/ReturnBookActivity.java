package com.example.libib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReturnBookActivity extends AppCompatActivity {

    int quantity;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);
        mAuth = FirebaseAuth.getInstance();
    }

    public void returnbook(View view) {
        String email = ((EditText)findViewById(R.id.userid)).getText().toString();
        String password = ((EditText)findViewById(R.id.passcode)).getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
                                        String fd = snapshot.child(arr[0]).child("IssuedBook").getValue().toString();
                                        String td = ((TextView) findViewById(R.id.bookid)).getText().toString();

                                        if (fd.equals(td)) {
                                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("data").
                                                    child("Students").
                                                    child(arr[0]);
                                            myRef.removeValue();

                                            DatabaseReference FireRef = FirebaseDatabase.
                                                    getInstance().
                                                    getReference("data").
                                                    child("Books").
                                                    child(
                                                            ((TextView) findViewById(R.id.bookid)).
                                                                    getText().
                                                                    toString()
                                                    )
                                                    .child("BookQuantity");

                                            FireRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot snapshot) {
                                                    quantity = Integer.parseInt(snapshot.getValue().toString());
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });

                                            quantity++;
                                            DatabaseReference Ref = FirebaseDatabase.getInstance().getReference("data").
                                                    child("Books").
                                                    child(
                                                            ((TextView) findViewById(R.id.bookid)).getText().toString()
                                                    ).
                                                    child("BookQuantity");
                                            String Quantity = ""+quantity;
                                            Ref.setValue(Quantity);
                                            Toast.makeText(ReturnBookActivity.this, "Book Returned Successfully", Toast.LENGTH_SHORT).show();

                                            ((TextView) findViewById(R.id.bookid)).setText("");
                                            ((TextView) findViewById(R.id.userid)).setText("");
                                            ((TextView) findViewById(R.id.passcode)).setText("");

                                        } else {
                                            Toast.makeText(ReturnBookActivity.this, "Invalid Book ID", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(ReturnBookActivity.this, "Book Not Issued", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { }
                            });
                        } else {
                            Toast.makeText(ReturnBookActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mAuth.signOut();
    }
}