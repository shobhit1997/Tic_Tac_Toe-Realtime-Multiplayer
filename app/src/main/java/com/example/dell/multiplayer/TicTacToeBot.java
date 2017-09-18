package com.example.dell.multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by DELL on 9/17/2017.
 */

public class TicTacToeBot extends AppCompatActivity {

    TextView ar[][]=new TextView[3][3];
    int ar1[][];

    String turn;
    int symbol;
    String gameId;
    String player,opponent;
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
        opponent=i.getStringExtra("opponent");
        turn=player;
        Log.i("player",player);
        Log.i("symbol",symbol+"");
        Log.i("gameId",gameId);


    }
    public void play(View view)
    {
        view.setEnabled(false);
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
                    Toast.makeText(getApplication(), "Winner is :" + (winner == 2 ? opponent : player), Toast.LENGTH_LONG).show();
                }
                Intent i=new Intent(this,Main2Activity.class);
                startActivity(i);
                finish();

            }
            else
            {
                turn=opponent;
                playOponent();
            }
        }

    }
    public void playOponent()
    {
        if(turn.equals(opponent))
        {
            BestMove move=new BestMove();
            int t[]=move.findBestMove(ar1);
            int r=t[0];
            int c=t[1];
            ar1[r][c] =2;
            Log.i("R:",r+"");
            Log.i("C:",c+"");
            int backId=android.R.drawable.btn_radio;
            ar[r][c].setBackgroundResource(backId);
            ar[r][c].setEnabled(false);
            int winner=win();
            if(winner!=0)
            {
                layout.setEnabled(false);
                if(winner==3)
                {
                    Toast.makeText(getApplication(),"Match Drawn",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplication(), "Winner is :" + (winner == 2 ? opponent : player), Toast.LENGTH_LONG).show();
                }
                Intent i=new Intent(this,Main2Activity.class);
                startActivity(i);
                finish();

            }
            else {
                turn=player;
            }

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

