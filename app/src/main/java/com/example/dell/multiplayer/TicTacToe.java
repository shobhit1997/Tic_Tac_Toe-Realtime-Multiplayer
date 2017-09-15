package com.example.dell.multiplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class TicTacToe extends AppCompatActivity {

    TextView ar[][]=new TextView[3][3];
    int ar1[][];
    FirebaseDatabase firebaseDatabase;
    DatabaseReference activeGames;
    String turn;
    int symbol;
    String gameId;
    String player;
    String player1,player2;
    boolean wait=false;
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);
        ar[0][0]=(TextView)findViewById(R.id.textView00);
        ar[0][1]=(TextView)findViewById(R.id.textView01);
        ar[0][2]=(TextView)findViewById(R.id.textView02);
        ar[1][0]=(TextView)findViewById(R.id.textView10);
        ar[1][1]=(TextView)findViewById(R.id.textView11);
        ar[1][2]=(TextView)findViewById(R.id.textView12);
        ar[2][0]=(TextView)findViewById(R.id.textView20);
        ar[2][1]=(TextView)findViewById(R.id.textView21);
        ar[2][2]=(TextView)findViewById(R.id.textView22);
        layout=(LinearLayout) findViewById(R.id.grid);
        layout.setEnabled(true);
        ar1=new int[3][3];
        Intent i=getIntent();
        player=i.getStringExtra("player");
        symbol=i.getIntExtra("symbol",1);//0-cross,1-circle
        gameId=i.getStringExtra("gameId");
        Log.i("player",player);
        Log.i("symbol",symbol+"");
        Log.i("gameId",gameId);
        firebaseDatabase= FirebaseDatabase.getInstance();
        activeGames=firebaseDatabase.getReference().child("ActiveGames");


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    int joined = Integer.parseInt(String.valueOf(messageSnapshot.child("joined").getValue()));

                    Log.i("Joined :",joined+"");
                    if(joined==1&&gameId.equals(messageSnapshot.getKey())) {
                        int winner = Integer.parseInt(String.valueOf(messageSnapshot.child("winner").getValue()));
                        player1=String.valueOf( messageSnapshot.child("player1").getValue());
                        Log.i("player1",player1);
                        player2=String.valueOf( messageSnapshot.child("player2").getValue());
                        Log.i("player2",player2);

                            turn = String.valueOf(messageSnapshot.child("turn").getValue());
                            Log.i("Turn", turn);
                            if (player.equals(turn)) {
                                String selected = String.valueOf(messageSnapshot.child("selected").getValue());
                                Log.i("Selected", selected);
                                if (selected.trim().length() > 0) {
                                    int r = selected.charAt(0) - 48;
                                    int c = selected.charAt(1) - 48;
                                    if (ar1[r][c] == 0) {
                                        ar1[r][c] = (symbol == 1 ? 2 : 1);
                                        ar[r][c].setEnabled(false);
                                        int backId = (symbol == 1 ? android.R.drawable.btn_radio : android.R.drawable.ic_delete);
                                        ar[r][c].setBackgroundResource(backId);
                                        if(winner!=0)
                                        {
                                            layout.setEnabled(false);
                                            if(winner==3)
                                            {
                                                Toast.makeText(getApplication(),"Match Drawn",Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                Toast.makeText(getApplication(), "Winner is :" + (winner == 2 ? player2 : player1), Toast.LENGTH_LONG).show();
                                            }
                                            Intent i=new Intent(getApplicationContext() ,Main2Activity.class);
                                            startActivity(i);
                                            finish();


                                        }
                                        else
                                        {
                                            Toast.makeText(getApplication(),"Your Turn",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }


                    }
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                    Log.i("Error",databaseError.getMessage());
                // ...
            }
        };
        activeGames.addValueEventListener(postListener);

    }
    public void play(View view)
    {
        Log.i("turn",turn);
        Log.i("player",player);
        if(turn.equals(player)) {
            String tag = view.getTag().toString();
            Log.i("Tag",tag);
            int r = tag.charAt(0) - 48;
            int c = tag.charAt(1) - 48;
            ar1[r][c] =symbol;
            int backId=(symbol==2?android.R.drawable.btn_radio:android.R.drawable.ic_delete);
            ar[r][c].setBackgroundResource(backId);
            int winner=win();
            if(winner!=0)
            {
                layout.setEnabled(false);
                if(winner==3)
                {
                    Toast.makeText(getApplication(),"Match Drawn",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplication(), "Winner is :" + (winner == 2 ? player2 : player1), Toast.LENGTH_LONG).show();
                }
                Intent i=new Intent(this,Main2Activity.class);
                startActivity(i);
                finish();

            }



            HashMap<String, Object> result = new HashMap<>();

            result.put("winner",winner);
            result.put("author",player1);
            result.put("joined",1);
            result.put("player1",player1);
            result.put("player2",player2);
            result.put("turn",player.equals(player1)?player2:player1);
            result.put("selected",r+""+c);
            activeGames.child(gameId).setValue(result);

            /*activeGames.child(gameId).child("selected").setValue();
            activeGames.child(gameId).child("turn").setValue();*/

        }

    }
    public int win()
    {
        for(int i=0;i<3;i++)
        {
            if(ar1[i][0]==ar1[i][1]&&ar1[i][1]==ar1[i][2]&&ar1[i][0]!=0)
            {
                return ar1[i][0];
            }
            else if(ar1[0][i]==ar1[1][i]&&ar1[1][i]==ar1[2][i]&&ar1[0][i]!=0)
            {
                return ar1[0][i];
            }

        }
        if(ar1[0][0]==ar1[1][1]&&ar1[1][1]==ar1[2][2]&&ar1[0][0]!=0)
        {
            return ar1[0][0];
        }
        if(ar1[0][2]==ar1[1][1]&&ar1[1][1]==ar1[2][0]&&ar1[0][2]!=0)
        {
            return ar1[0][2];
        }
        for (int x=0;x<3;x++)
        {
            for(int y=0;y<3;y++)
            {
                if(ar1[x][y]==0)
                {
                    return 0;
                }
            }
        }
        return 3;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this,Main2Activity.class);
        startActivity(i);
        finish();
    }
}
