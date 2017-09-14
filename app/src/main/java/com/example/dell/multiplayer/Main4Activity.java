package com.example.dell.multiplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main4Activity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference activeGames;
    String gameId;
    TextView joining;
    Button start;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        joining=(TextView)findViewById(R.id.joining);
        start=(Button)findViewById(R.id.begin);
        firebaseDatabase=FirebaseDatabase.getInstance();
        activeGames=firebaseDatabase.getReference().child("ActiveGames");
        Intent i=getIntent();
        gameId=i.getStringExtra("gameId");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                     name = String.valueOf( messageSnapshot.child("author").getValue());
                    int joined = Integer.parseInt(String.valueOf(messageSnapshot.child("joined").getValue()));
                    Log.i("Author :",name);
                    Log.i("Joined :",joined+"");
                    if(joined==1&&gameId.equals(messageSnapshot.getKey())) {

                        joining.setText("Joined");

                        start.setEnabled(true);




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


    }
    public void begin(View view)
    {

        Intent it=new Intent(getApplicationContext(),TicTacToe.class);
        Toast.makeText(getApplicationContext(),"Your Turn",Toast.LENGTH_SHORT).show();
        it.putExtra("player",name);
        it.putExtra("symbol",1);
        it.putExtra("gameId",gameId);
        gameId="";
        startActivity(it);


    }
}
