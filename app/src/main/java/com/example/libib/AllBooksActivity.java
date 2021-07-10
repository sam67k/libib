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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllBooksActivity extends AppCompatActivity {

    List<String> BooksID = new ArrayList<String>();
    List<String> BooksTitle = new ArrayList<String>();
    List<String> BooksQuantity = new ArrayList<String>();

    ListAdapter la;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        list = (ListView)findViewById(R.id.bookslist);

//        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("data").child("Books");
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                int i = 1;
//                for (DataSnapshot Snapshot : snapshot.getChildren()) {
//                    Toast.makeText(AllBooksActivity.this, ""+snapshot, Toast.LENGTH_SHORT).show();
//                    String num = ""+i;
//                    BooksID.add(Snapshot.child(num).child("BookID").getValue().toString());
//                    BooksTitle.add(Snapshot.child(num).child("BookTitle").getValue().toString());
//                    BooksQuantity.add(Snapshot.child(num).child("BookQuantity").getValue().toString());
//
//                    if (BooksID.size() == 1) {
//                        la = new ListAdapter();
//                        list.setAdapter(la);
//                    } else if (BooksID.size() > 1) {
//                        la.notifyDataSetChanged();
//                    }
//
//                    i++;
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) { }
//        });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("data").child("Books").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot Snapshot : snapshot.getChildren()) {

                        String tester = Snapshot.getKey();

                        if (tester.equals("BookID")) {
                            BooksID.add(Snapshot.getValue(String.class));
                        } else if (tester.equals("BookTitle")) {
                            BooksTitle.add(Snapshot.getValue(String.class));
                        } else if (tester.equals("BookQuantity")) {
                            BooksQuantity.add(Snapshot.getValue(String.class));
                        }

                        if (BooksID.size() == 1 && BooksQuantity.size() > 1 && BooksTitle.size() > 1) {
                            la = new ListAdapter();
                            list.setAdapter(la);
                        } else if (BooksID.size() > 1) {
                            la.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);

        ListView list = (ListView)findViewById(R.id.bookslist);
        la = new ListAdapter();
        list.setAdapter(la);
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return BooksID.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.bookslayout, null);
            TextView id = (TextView)convertView.findViewById(R.id.bookid);
            TextView title = (TextView)convertView.findViewById(R.id.booktitle);
            TextView quantity = (TextView)convertView.findViewById(R.id.bookquantity);

            id.setText(BooksID.get(position));
            title.setText(BooksTitle.get(position));
            quantity.setText(BooksQuantity.get(position));

            return convertView;
        }
    }
}