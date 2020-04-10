package com.lendeasy.lendeasy.ChatFunctionality;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.lendeasy.lendeasy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior.ESTIMATE;

public class ChatActivity extends AppCompatActivity {

    public static final String USER_ID = "UserId";
    ChatAdapter chatAdapter;
    FirebaseFirestore database;
    Map<String, Object> data;
    CollectionReference chatRoomsCollectionReference;
    CollectionReference usersCollectionReference;
    ImageButton imageButton;
    EditText editText;
    List<ChatModel> arrayList = new ArrayList<>();
    RecyclerView recyclerView;

    public static String getDataFromSharedPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        imageButton = findViewById(R.id.imageButton);
        editText = findViewById(R.id.editText);
        database = FirebaseFirestore.getInstance();
        chatRoomsCollectionReference = database.collection("chatRooms");
        usersCollectionReference = database.collection("users2");


        initRecyclerView();
        setTitle(getIntent().getStringExtra("recieverName"));

//      Query getuserIdDocumentReference =usersCollectionReference.whereEqualTo("userId",getDataFromSharedPref(USER_ID,getApplicationContext()));
//getuserIdDocumentReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
//    @Override
//    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//        if (queryDocumentSnapshots!=null){
//            for(DocumentSnapshot doc:queryDocumentSnapshots) {
//userIdDocumentReference=doc.getId();
//            }
//        }
//    }
//});


        Query getMessages = chatRoomsCollectionReference.document(setOneToOneChat(getDataFromSharedPref(USER_ID, getApplicationContext()), getIntent().getStringExtra("recieverId")))
                .collection("messages").orderBy("timeStamp");


        getMessages.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    arrayList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        DocumentSnapshot.ServerTimestampBehavior behavior = ESTIMATE;
                        arrayList.add(new ChatModel(doc.getString("message"), doc.getTimestamp("timeStamp", behavior), doc.getString("senderId"), doc.getString("recieverId")));

                    }
                    chatAdapter.updateRecylerView(arrayList);
                    recyclerView.scrollToPosition(arrayList.size() - 1);
                }
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(editText)) {
                    Toast.makeText(ChatActivity.this, "Enter a text", Toast.LENGTH_SHORT).show();
                } else {

                    data = new HashMap<>();
                    data.put("timeStamp", FieldValue.serverTimestamp());
                    data.put("senderId", getDataFromSharedPref(USER_ID, getApplicationContext()));
                    data.put("recieverId", getIntent().getStringExtra("recieverId"));
                    data.put("message", editText.getText().toString().trim());
                    editText.setText("");
                    chatRoomsCollectionReference.document(setOneToOneChat(getDataFromSharedPref(USER_ID, getApplicationContext()), getIntent().getStringExtra("recieverId"))).collection("messages").document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ChatActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("Error Sending Message", e.getMessage());
                        }
                    });
//chatRoomsCollectionReference.document(getIntent().getStringExtra("chatRoomDocumentId")).collection("Messages").add(data);


                }
            }
        });
    }

    public void initRecyclerView() {

        recyclerView = findViewById(R.id.recylerView);
        chatAdapter = new ChatAdapter(arrayList, getDataFromSharedPref(USER_ID, getApplicationContext()), getApplicationContext());
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
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
