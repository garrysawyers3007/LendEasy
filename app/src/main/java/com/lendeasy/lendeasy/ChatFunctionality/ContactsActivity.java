package com.lendeasy.lendeasy.ChatFunctionality;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.lendeasy.lendeasy.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    public static final String USER_ID = "UserId";
    public static final String USER_NAME = "UserName";
    Button logOutButton;
    FirebaseAuth auth;
    FirebaseFirestore database;
    CollectionReference usersCollectionReference;
    ContactAdapter contactAdapter;
    TextView userNameTextView;
    CollectionReference chatRoomsCollectionReference;
    List<Contact> arrayList = new ArrayList<>();

    public static String getDataFromSharedPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public static void storeDataIntoSharedPref(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(ContactsActivity.this, LoginActivity2.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);


        logOutButton = findViewById(R.id.logOutButton);
        userNameTextView = findViewById(R.id.userNameTV);
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        usersCollectionReference = database.collection("users2");
        chatRoomsCollectionReference = database.collection("chatRooms");

        userNameTextView.setText(getDataFromSharedPref(USER_NAME, getApplicationContext()));
        initRecyclerView();

        Query getContacts = usersCollectionReference.orderBy("userId");
        getContacts.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                arrayList.clear();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    if (!doc.getString("userId").equals(getDataFromSharedPref(USER_ID, getApplicationContext()))) {
                        arrayList.add(new Contact(doc.getString("name"), doc.getString("eMail"), doc.getString("userId")));
                    }
                }
                contactAdapter.updateRecylerView(arrayList);
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query getCurrentUser = usersCollectionReference.whereEqualTo("userId", getDataFromSharedPref(USER_ID, getApplicationContext()));
                getCurrentUser.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                usersCollectionReference.document(document.getId()).delete();
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
                auth.signOut();
            }
        });


        contactAdapter.setCustomClickListener(new ContactAdapter.CustomClickListener() {
            @Override
            public void onClick(Contact contact) {
                Intent intent = new Intent(ContactsActivity.this, ChatActivity.class);
                intent.putExtra("recieverName", contact.getName());
                intent.putExtra("recieverId", contact.getUserId());
                startActivity(intent);
            }
        });
    }

    public void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recylerView);
        contactAdapter = new ContactAdapter(arrayList);
        recyclerView.setAdapter(contactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private String setOneToOneChat(String uid1, String uid2) {
//Check if user1’s id is less than user2's
        if (uid1.compareTo(uid2) > 0) {
            return uid1 + uid2;
        } else {
            return uid2 + uid1;
        }

    }


}
