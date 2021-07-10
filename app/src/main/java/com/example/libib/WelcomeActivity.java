package com.example.libib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mAuth = FirebaseAuth.getInstance();
    }

    public void signin(View view) {
        EditText EMAIL, PASSCODE;

        EMAIL = (EditText)findViewById(R.id.userid);
        PASSCODE = (EditText)findViewById(R.id.passcode);

        String email = EMAIL.getText().toString();
        String password = PASSCODE.getText().toString();

        if (email.matches("") || password.matches("")) {
            Toast.makeText(this, "Email or Passcode not Entered", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                String EMAIL = user.getEmail();
                                String[] arr = EMAIL.split("@",2);
                                if (arr[0].matches("admin")) {
                                    startActivity(new Intent(WelcomeActivity.this, AdminHomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(WelcomeActivity.this, "Not a Admin User", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();

                                    ((EditText)findViewById(R.id.userid)).setText("");
                                    ((EditText)findViewById(R.id.passcode)).setText("");
                                }
                            } else {
                                Toast.makeText(WelcomeActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}