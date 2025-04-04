package com.student.studentapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.student.studentapp.Adapter.StudentAdapter;
import com.student.studentapp.Model.Student;
import com.student.studentapp.R;

import java.util.ArrayList;
import java.util.List;

public class ViewScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<Student> studentList;
    private ImageView imgBack;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_screen);

        init();
        init_callback();

        fetchStudentDataFromFirestore();
    }

    public void init() {
        recyclerView = findViewById(R.id.recyclerView);
        imgBack = findViewById(R.id.imgBack);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentList = new ArrayList<>();
        adapter = new StudentAdapter(this, studentList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
    }

    public void init_callback() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewScreen.this, Dashboard.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });


    }

    private void fetchStudentDataFromFirestore() {
        if (studentList != null) {
            db.collection("studentdetails")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Clear the existing list
                            studentList.clear();

                            // Loop through the documents and add them to the list
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Retrieve the fields from the document
                                String name = document.getString("name");
                                String std = document.getString("stdclass");
                                String section = document.getString("section");
                                String school = document.getString("school");
                                String imageUrl = document.getString("imageUrl");

                                // Add the student data to the list
                                studentList.add(new Student(name, std, section, school, imageUrl));  // Use a default image for now
                            }

                            // Initialize the adapter if not already initialized
                            if (adapter == null) {
                                adapter = new StudentAdapter(ViewScreen.this, studentList);
                                recyclerView.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            // Handle errors
                        }
                    });
        }
    }
}