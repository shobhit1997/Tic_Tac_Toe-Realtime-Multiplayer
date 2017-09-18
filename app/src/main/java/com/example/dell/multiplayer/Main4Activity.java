package com.example.dell.multiplayer;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
    CountDownTimer timer;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        joining=(TextView)findViewById(R.id.joining);

        firebaseDatabase=FirebaseDatabase.getInstance();
        activeGames=firebaseDatabase.getReference().child("ActiveGames");
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setMax(20000);
        Intent i=getIntent();
        gameId=i.getStringExtra("gameId");
        name=i.getStringExtra("name");
        timer=new CountDownTimer(20000+100,10) {
            @Override
            public void onTick(long l) {

                progressBar.setProgress((int)l);

            }

            @Override
            public void onFinish() {

                joining.setText("Joined");
                activeGames.child(gameId).child("joined").setValue(1);
                activeGames.child(gameId).child("player2").setValue("Tic Tac Toe Bot");
                Intent it=new Intent(getApplicationContext(),TicTacToeBot.class);
                Toast.makeText(getApplicationContext(),"Hello I am TicTacToe Bot",Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Your Turn",Toast.LENGTH_SHORT).show();
                it.putExtra("player",name);
                it.putExtra("symbol",1);
                it.putExtra("gameId",gameId);
                it.putExtra("opponent",gameId);
                gameId="";
                startActivity(it);
                finish();

            }
        }.start();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    int joined = Integer.parseInt(String.valueOf(messageSnapshot.child("joined").getValue()));
                    String joiner = String.valueOf( messageSnapshot.child("player2").getValue());

                    Log.i("Joined :",joined+"");
                    if(joined==1&&gameId.equals(messageSnapshot.getKey())&&joiner.length()!=0) {

                        timer.cancel();

                        joining.setText("Joined");
                        name = String.valueOf( messageSnapshot.child("author").getValue());
                        Log.i("Author 1:",name);
                        Intent it=new Intent(getApplicationContext(),TicTacToe.class);
                        Toast.makeText(getApplicationContext(),"Your Turn",Toast.LENGTH_SHORT).show();
                        it.putExtra("player",name);
                        it.putExtra("symbol",1);
                        it.putExtra("gameId",gameId);
                        gameId="";
                        startActivity(it);
                        finish();



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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activeGames.child(gameId).child("joined").setValue(1);
    }
}
