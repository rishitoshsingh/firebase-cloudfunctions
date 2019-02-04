package rishi.alphae.sampleapp;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private UserAdapter viewAdapter;
    private ArrayList<User> mUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("User");
        RecyclerView.LayoutManager viewManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        viewAdapter = new UserAdapter(mUsers);

        RecyclerView userRecycler = findViewById(R.id.user_recycler);
        userRecycler.setHasFixedSize(true);
        userRecycler.setLayoutManager(viewManager);
        userRecycler.setAdapter(viewAdapter);
        userRecycler.setItemAnimator(new DefaultItemAnimator());

        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editUserName = (EditText) findViewById(R.id.edit_user_name);
                String userName = editUserName.getText().toString();
                EditText editUserAge = (EditText) findViewById(R.id.edit_user_age);
                String userAge = editUserAge.getText().toString();
                Integer age = Integer.parseInt(userAge);

                if (!Objects.equals(userName, "") && !Objects.equals(userAge, "")) {
                    String userId = database.push().getKey();
                    database.child(userId).setValue(new User(userName, age));
                    Snackbar.make(findViewById(R.id.root_view), "Users Updated", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                mUsers.add(user);
                viewAdapter.notifyDataSetChanged();
                findViewById(R.id.progress_bar).setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mUsers.remove(user);
                viewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}