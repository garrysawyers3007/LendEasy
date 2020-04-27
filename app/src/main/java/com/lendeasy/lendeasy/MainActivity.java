package com.lendeasy.lendeasy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

import static com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior.ESTIMATE;


public class MainActivity extends AppCompatActivity {
    FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadFragment(new Lend());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.getMenu().findItem(R.id.lend).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.profile:
                        loadFragment(new Profile());
                        item.setChecked(true);
                        break;
                    case R.id.lend:
                        loadFragment(new Lend());
                        item.setChecked(true);
                        break;
                    case R.id.borrow:
                        loadFragment(new Borrow());
                        item.setChecked(true);
                        break;
                    case R.id.target:
                        loadFragment(new Target());
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });
        setUpNotifications();

    }

    private void setUpNotifications() {
        database = FirebaseFirestore.getInstance();
        DocumentReference borrowItem = database.collection("borrow")
                .document("6vV6grSQxkfi6LE0eJyb");

        borrowItem.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot.ServerTimestampBehavior behavior = ESTIMATE;
                Date borrowDate = task.getResult().getTimestamp("Timestamp", behavior).toDate();

                Calendar calendar = Calendar.getInstance();
                Timestamp timestamp = new Timestamp(new Date(System.currentTimeMillis()));
                Date today = timestamp.toDate();
                long differenceInDays = (today.getTime() - borrowDate.getTime()) / (1000 * 3600 * 24);
                if (differenceInDays <= 3) {
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), AlertReciever.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (alarmManager != null) {
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                        }
                    }
                }
            }
        });


    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


}
