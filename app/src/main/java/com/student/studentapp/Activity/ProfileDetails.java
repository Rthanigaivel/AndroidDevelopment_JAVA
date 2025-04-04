package com.student.studentapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.student.studentapp.R;

public class ProfileDetails extends AppCompatActivity {

    ImageView imgBack, profile_image;
    TextView tv_student_name, tv_student_details, txt_gender, txt_dob, txt_blood_group, txt_fathername, txt_mothername, txt_contactno, txt_emergencyno, txt_address1, txt_city, txt_state, txt_pincode;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile_details);
        init();
        fetchStudentDataFromFirestore();
    }

    public void init() {
        tv_student_name = findViewById(R.id.tv_student_name);
        tv_student_details = findViewById(R.id.tv_student_details);
        txt_gender = findViewById(R.id.txt_gender);
        txt_dob = findViewById(R.id.txt_dob);
        txt_blood_group = findViewById(R.id.txt_blood_group);
        txt_fathername = findViewById(R.id.txt_fathername);
        txt_mothername = findViewById(R.id.txt_mothername);
        txt_contactno = findViewById(R.id.txt_contactno);
        txt_emergencyno = findViewById(R.id.txt_emergencyno);
        txt_address1 = findViewById(R.id.txt_address1);
        txt_city = findViewById(R.id.txt_city);
        txt_state = findViewById(R.id.txt_state);
        txt_pincode = findViewById(R.id.txt_zip);
        imgBack = findViewById(R.id.btn_back);
        profile_image = findViewById(R.id.profile_image);
        db = FirebaseFirestore.getInstance();

    }

    private void fetchStudentDataFromFirestore() {

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileDetails.this, ViewScreen.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        db.collection("studentdetails")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Clear the existing list

                        // Loop through the documents and add them to the list
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Retrieve the fields from the document
                            String name = document.getString("name");
                            String school = document.getString("school");
                            String stdclass = document.getString("stdclass");
                            String section = document.getString("section");
                            String gender = document.getString("gender");
                            String dob = document.getString("dob");
                            String blood_group = document.getString("bloodgroup");
                            String fathername = document.getString("fathername");
                            String mothername = document.getString("mothername");
                            String parentcontno = document.getString("parentcontno");
                            String emergencyno = document.getString("emergencyno");
                            String address1 = document.getString("address1");
                            String city = document.getString("city");
                            String state = document.getString("state");
                            String zip = document.getString("zip");
                            String imageUrl = document.getString("imageUrl");

                            tv_student_name.setText(name);
                            tv_student_details.setText(stdclass + " Standard '" + section + "' Section\n" + school);
                            txt_gender.setText(gender);
                            txt_dob.setText(dob);
                            txt_blood_group.setText(blood_group);
                            txt_fathername.setText(fathername);
                            txt_mothername.setText(mothername);
                            txt_contactno.setText(parentcontno);
                            txt_emergencyno.setText(emergencyno);
                            txt_address1.setText(address1);
                            txt_city.setText(city);
                            txt_state.setText(state);
                            txt_pincode.setText(zip);

                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(ProfileDetails.this)
                                        .load(imageUrl)
                                        .placeholder(R.drawable.group1379)
                                        .into(profile_image);
                            }

                        }


                    } else {
                        // Handle errors
                    }
                });
    }
}