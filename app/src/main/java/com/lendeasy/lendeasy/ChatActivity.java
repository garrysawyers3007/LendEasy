package com.lendeasy.lendeasy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

import static com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior.ESTIMATE;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    List<ChatModel> arrayList;
    FirebaseFirestore database;
    Map<String, Object> data;
    CollectionReference collectionReference;
    Button button;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.recylerView);
        database = FirebaseFirestore.getInstance();
        collectionReference = database.collection("chat");
        Query getMessages = collectionReference.orderBy("timeStamp");



        getMessages.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {

                    arrayList=new ArrayList<>();
                    for(DocumentSnapshot doc:queryDocumentSnapshots){
                        DocumentSnapshot.ServerTimestampBehavior behavior = ESTIMATE;
                        arrayList.add(new ChatModel(doc.getString("message"),doc.getTimestamp("timeStamp",behavior)));
                    }

                    chatAdapter = new ChatAdapter(arrayList, getApplicationContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                    recyclerView.setAdapter(chatAdapter);
                    recyclerView.scrollToPosition(arrayList.size()-1);
                    //chatAdapter.updateRecylerView(queryDocumentSnapshots.toObjects(ChatModel.class));
                }
            }
        });

        initRecyclerView();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(editText)) {
                    Toast.makeText(ChatActivity.this, "Enter a text", Toast.LENGTH_SHORT).show();
                } else {

                    data = new HashMap<>();
                    data.put("message", editText.getText().toString());
                    data.put("timeStamp", FieldValue.serverTimestamp());
                    editText.setText("");
                    collectionReference.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(ChatActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    public void initRecyclerView() {

    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }
}
