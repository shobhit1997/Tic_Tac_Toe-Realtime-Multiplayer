package com.example.dell.multiplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Main3Activity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference activeGames;
    ArrayList<String> games;

    ArrayList<String> gameId;
    ArrayAdapter<String> adapter;
    String joiner;
    String turn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        firebaseDatabase=FirebaseDatabase.getInstance();
        activeGames=firebaseDatabase.getReference().child("ActiveGames");
        Intent i=getIntent();
        joiner=i.getStringExtra("user");
        ListView lv=(ListView)findViewById(R.id.list);
        games=new ArrayList<>();
        gameId=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,games);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                gameId.clear();
                games.clear();
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    int joined = Integer.parseInt(String.valueOf(messageSnapshot.child("joined").getValue()));

                    Log.i("Joined :",joined+"");
                    if(joined==0) {
                        String name = String.valueOf( messageSnapshot.child("author").getValue());
                        gameId.add(messageSnapshot.getKey());
                        games.add(name);
                        turn=String.valueOf( messageSnapshot.child("turn").getValue());
                        adapter.notifyDataSetChanged();
                    }
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };
        activeGames.addValueEventListener(postListener);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                activeGames.child(gameId.get(i)).child("joined").setValue(1);
                activeGames.child(gameId.get(i)).child("player2").setValue(joiner);
                Log.i("Game :", games.get(i));
                Log.i("GameId :", gameId.get(i));
                Intent it=new Intent(getApplicationContext(),TicTacToe.class);
                it.putExtra("player",joiner);
                it.putExtra("symbol",2);
                it.putExtra("gameId",gameId.get(i));
                startActivity(it);

            }
        });

    }


}
