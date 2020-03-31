package com.lendeasy.lendeasy;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    private static final int STORAGE_PERMISSION_CODE =2 ;
    private static final int CAMERA_PERMISSION_CODE = 3;
    private static final int MY_CAMERA_REQUEST_CODE = 4;
    private ArrayList<String> list;
    private RecyclerView recyclerView;
    private HistoryAdapter lentAdapter;
    private ImageView exit;
    private GoogleSignInClient googleSignInClient;
    private Uri imageuri;
    final int REQ_IMAGE_CAPTURE = 1;
    Uri resulturi;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        exit = view.findViewById(R.id.logout);
        final TextView target, score, balance, name;

        Button borrow = view.findViewById(R.id.borrow);

        borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission(new String[]{Manifest.permission.CAMERA}[0], MY_CAMERA_REQUEST_CODE);
            }
        });

        target = view.findViewById(R.id.target);
        score = view.findViewById(R.id.time);
        balance = view.findViewById(R.id.balance);
        name = view.findViewById(R.id.name);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                googleSignInClient.signOut();
                mAuth.signOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                getActivity().finish();
                Toast.makeText(getContext(), "Signed Out",
                        Toast.LENGTH_SHORT).show();
            }
        });

        String userid = mAuth.getCurrentUser().getUid();


        db.collection("users").document(userid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot != null) {

                            Log.d("docsnap", documentSnapshot + "");
                            Log.d("docdouble", documentSnapshot.getDouble("Target") + "");

                            target.setText(documentSnapshot.getDouble("Target") + "");
                            score.setText(documentSnapshot.getDouble("Score") + "");
                            balance.setText(documentSnapshot.getDouble("Balance") + "");
                            name.setText(documentSnapshot.getString("Name"));

                        }
                    }
                });

        list = new ArrayList<>();
        list.add("Hello");
        recyclerView = view.findViewById(R.id.history);
        lentAdapter = new HistoryAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(lentAdapter);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_IMAGE_CAPTURE)
            if(resultCode==RESULT_OK ){
                Log.d("TAg",imageuri.toString());
                CropImage.activity(imageuri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(),this);
            }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){

                resulturi = result.getUri();
                Log.d("Img",resulturi.toString());

                Intent intent=new Intent(getActivity(),BorrowActivity.class);
                intent.putExtra("Image",resulturi);
                startActivity(intent);
//                image.setImageURI(resulturi);
//                BitmapDrawable bitmapDrawable=(BitmapDrawable)image.getDrawable();
//                bitmap=bitmapDrawable.getBitmap();

            }
            else
                Log.d("Img","jbjbhjv");
        }

    }

    // Function to check and request permission
    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                getContext(),
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            getActivity(),
                            new String[] { permission },
                            requestCode);
        }
        else {
            takepicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                takepicture();
            }
            else {
                Toast.makeText(getContext(),
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(),
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(getContext(),
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void takepicture(){
        PackageManager pm = getContext().getPackageManager();

        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            ContentValues values = new ContentValues();

            values.put(MediaStore.Images.Media.TITLE, "New pic");
            values.put(MediaStore.Images.Media.DESCRIPTION, "Image to text");

            imageuri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);

            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                //checking if phone has camera start the activity for getting picture
                startActivityForResult(takePictureIntent, REQ_IMAGE_CAPTURE);
            }
        }
    }

}
