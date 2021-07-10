package com.example.libib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class IssuedBooksActivity extends AppCompatActivity {

    List<String> BooksID = new ArrayList<String>();
    List<String> StudentName = new ArrayList<String>();

    IssuedBooksListAdapter ibl;

    ListView issuedlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        issuedlist = (ListView)findViewById(R.id.issuedbookslist);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("data").child("Students");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    BooksID.add(postSnapshot.child("IssuedBook").getValue().toString());
                    StudentName.add(postSnapshot.getKey());

                    if (BooksID.size() == 1) {
                        ibl = new IssuedBooksListAdapter();
                        issuedlist.setAdapter(ibl);
                    } else if (BooksID.size() > 1) {
                        ibl.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_books);

        issuedlist = (ListView)findViewById(R.id.issuedbookslist);
        ibl = new IssuedBooksListAdapter();
        issuedlist.setAdapter(ibl);

    }

    class IssuedBooksListAdapter extends BaseAdapter {

        @Override
        public int getCount() { return BooksID.size(); }

        @Override
        public Object getItem(int position) { return null; }

        @Override
        public long getItemId(int position) { return 0; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.issuedbookslayout, null);
            TextView id = (TextView)convertView.findViewById(R.id.bookid);
            TextView student = (TextView)convertView.findViewById(R.id.studentname);

            id.setText(BooksID.get(position));
            student.setText(StudentName.get(position));

            return convertView;
        }

    }
}