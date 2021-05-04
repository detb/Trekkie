package com.github.detb.trekkie.db;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.github.detb.trekkie.data.model.Hike;
import com.github.detb.trekkie.data.model.HikeFirebase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class HikeFirebaseDao {
    private MutableLiveData<Hike> hike;
    MutableLiveData<List<Hike>> hikes;
    private FirebaseUser user;
    private static HikeFirebaseDao instance;
    private FirebaseDatabase database;

    public HikeFirebaseDao() {
        database = FirebaseDatabase.getInstance();

        hike = new MutableLiveData<>();

        hike.observeForever(new Observer<Hike>() {
            @Override
            public void onChanged(Hike hike) {
                DatabaseReference myRef = database.getReference("Hikes");// Read from the database
                myRef.setValue(hike.getHikeAsHikeFirebase());
            }
        });
    }

    public static HikeFirebaseDao getInstance(){
        if (instance == null)
            instance = new HikeFirebaseDao();
        return instance;
    }

    public MutableLiveData<Hike> getHike() {
        DatabaseReference myRef = database.getReference("Hikes");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                hike.setValue(new Hike(dataSnapshot.getValue(HikeFirebase.class)));
                Log.d("TAG", "Value is: " + hike.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        return hike;
    }

    public MutableLiveData<List<Hike>> getHikes() {
        DatabaseReference myRef = database.getReference("Hikes");// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                hike.setValue(new Hike(dataSnapshot.getValue(HikeFirebase.class)));
                Log.d("TAG", "Value is: " + hike.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        return hikes;
    }

    public void updateHike(Hike hike)
    {
        this.hike.setValue(hike);
    }


    public void getHikeFromFirebase(String id){
        DatabaseReference myRef = database.getReference("Hikes");// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                hike.setValue(new Hike(dataSnapshot.getValue(HikeFirebase.class)));
                Log.d("TAG", "Value is: " + hike.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
        getHikeFromFirebase(user.getUid());
    }

    public FirebaseUser getUser(){
        return user;
    }

    public void updateDatabase() {
        DatabaseReference myRef = database.getReference("Hikes");// Read from the database
        myRef.setValue(hike.getValue().getHikeAsHikeFirebase());
    }

    public void setHike(Hike hike) {
        DatabaseReference myRef = database.getReference("Hikes");
        myRef.setValue(hike.getHikeAsHikeFirebase());
    }

    public void setText() {
        DatabaseReference myRef = database.getReference("test");
        myRef.setValue("HelloWorld");
    }
}
