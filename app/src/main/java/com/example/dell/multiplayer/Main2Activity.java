package com.example.dell.multiplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Main2Activity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference activeGames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        activeGames=firebaseDatabase.getReference().child("ActiveGames");

    }
    public void signOut(View view) {
        mAuth.signOut();
        finish();
    }
    public void createGame(View view)
    {
        HashMap<String, Object> result = new HashMap<>();

        FirebaseUser user=mAuth.getCurrentUser();
        result.put("author",user.getEmail());
        result.put("joined",0);
        result.put("player1",user.getEmail());
        result.put("player2","");
        result.put("turn",user.getEmail());
        result.put("selected","");
        result.put("winner",0);
        activeGames.child(user.getUid()).setValue(result);
        Intent i=new Intent(this,Main4Activity.class);
        i.putExtra("gameId",user.getUid());
        i.putExtra("name",user.getEmail());
        startActivity(i);


    }
    public void join(View view)
    {
        FirebaseUser user=mAuth.getCurrentUser();
        Intent i=new Intent(this,Main3Activity.class);
        i.putExtra("user",user.getEmail());
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}
