package com.lendeasy.lendeasy;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    List<ChatModel> arrayList = new ArrayList<>();
    FirebaseFirestore database;
    Map<String, Object> data;
    CollectionReference collectionReference;
    ImageButton button;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initRecyclerView();
        button = findViewById(R.id.imageButton);
        editText = findViewById(R.id.editText);
        database = FirebaseFirestore.getInstance();
        collectionReference = database.collection("chat");
        Query getMessages = collectionReference.orderBy("timeStamp");

        getMessages.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                chatAdapter.updateRecylerView(queryDocumentSnapshots.toObjects(ChatModel.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(editText)) {
                    Toast.makeText(ChatActivity.this, "Enter a text", Toast.LENGTH_SHORT).show();
                } else {

                    data = new HashMap<>();
                    data.put("message", editText.getText().toString());
                    data.put("timeStamp", FieldValue.serverTimestamp());
                    collectionReference.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(ChatActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                            editText.setText("");
                        }
                    });
                }
            }
        });
    }


    public void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerview);
        chatAdapter = new ChatAdapter(arrayList, getApplicationContext());
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new RecyclerView.LayoutManager() {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return null;
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
