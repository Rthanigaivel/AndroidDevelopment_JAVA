package com.student.studentapp.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.student.studentapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddStudent extends AppCompatActivity {

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;

    private Uri imageUri;
    TextInputEditText edt_name, edt_schoolname, edt_dob, edt_bloodgroup, edt_fathername, edt_mothername, edt_parentcontno, edt_address1, edt_city,
            edt_state, edt_zip, edt_emergencyno;
    Spinner spn_class, spn_section;
    ImageView imgBack, imgProfile;
    RadioGroup rbngroup_gender;
    RadioButton rbn_male, rbn_female;
    Button btn_submit;
    private FirebaseFirestore db;
    private Calendar calendar;
    List<String> classdetails = new ArrayList<>();
    List<String> sectiondetails = new ArrayList<>();
    String imageUrl = null;
    String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_student);

        init();
        init_callback();
        requestPermissions();
    }

    public void init() {
        imgBack = findViewById(R.id.imgBack);
        imgProfile = findViewById(R.id.imgProfile);
        edt_name = findViewById(R.id.edt_name);
        edt_schoolname = findViewById(R.id.edt_schoolname);
        edt_zip = findViewById(R.id.edt_zip);
        edt_emergencyno = findViewById(R.id.edt_emergencyno);
        edt_dob = findViewById(R.id.edt_dob);
        edt_bloodgroup = findViewById(R.id.edt_bloodgroup);
        edt_fathername = findViewById(R.id.edt_fathername);
        edt_mothername = findViewById(R.id.edt_mothername);
        edt_parentcontno = findViewById(R.id.edt_parentcontno);
        edt_address1 = findViewById(R.id.edt_address1);
        edt_city = findViewById(R.id.edt_city);
        edt_state = findViewById(R.id.edt_state);
        spn_class = findViewById(R.id.spn_class);
        spn_section = findViewById(R.id.spn_section);
        btn_submit = findViewById(R.id.btn_submit);
        rbngroup_gender = findViewById(R.id.rbngroup_gender);
        rbn_male = findViewById(R.id.rbn_male);
        rbn_female = findViewById(R.id.rbn_female);
        calendar = Calendar.getInstance();

        db = FirebaseFirestore.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            }
        }

    }

    public void init_callback() {


        edt_dob.setOnClickListener(v -> showDatePickerDialog());

        classdetails.add(0, "Select");
        classdetails.add(1, "1");
        classdetails.add(2, "2");
        classdetails.add(3, "3");
        classdetails.add(4, "4");
        classdetails.add(5, "5");
        classdetails.add(6, "6");
        classdetails.add(7, "7");
        classdetails.add(8, "8");
        classdetails.add(9, "9");
        classdetails.add(10, "10");
        classdetails.add(11, "11");
        classdetails.add(12, "12");
        ArrayAdapter<String> classadapter = new ArrayAdapter<>(AddStudent.this, android.R.layout.simple_spinner_item, classdetails);
        classadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_class.setAdapter(classadapter);

        sectiondetails.add(0, "Select");
        sectiondetails.add(1, "A");
        sectiondetails.add(2, "B");
        sectiondetails.add(3, "C");
        sectiondetails.add(4, "D");
        sectiondetails.add(5, "E");
        ArrayAdapter<String> sectionadapter = new ArrayAdapter<>(AddStudent.this, android.R.layout.simple_spinner_item, sectiondetails);
        sectionadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_section.setAdapter(sectionadapter);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStudent.this, Dashboard.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_name.getText().toString().trim();
                String school = edt_schoolname.getText().toString().trim();
                String dob = edt_dob.getText().toString().trim();
                String bloodgroup = edt_bloodgroup.getText().toString().trim();
                String fathername = edt_fathername.getText().toString().trim();
                String mothername = edt_mothername.getText().toString().trim();
                String parentcontno = edt_parentcontno.getText().toString().trim();
                String address1 = edt_address1.getText().toString().trim();
                String city = edt_city.getText().toString().trim();
                String state = edt_state.getText().toString().trim();
                String zip = edt_zip.getText().toString().trim();
                String emergencyno = edt_emergencyno.getText().toString().trim();
                String stdclass = spn_class.getSelectedItem().toString().trim();
                String section = spn_section.getSelectedItem().toString().trim();
                int selectedId = rbngroup_gender.getCheckedRadioButtonId();

                String gender = (selectedId != -1) ? ((RadioButton) findViewById(selectedId)).getText().toString() : "male";

                String studentId = db.collection("studentdetails").document().getId();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(school) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(bloodgroup)
                        || TextUtils.isEmpty(fathername) || TextUtils.isEmpty(mothername) || TextUtils.isEmpty(parentcontno)
                        || TextUtils.isEmpty(address1) || TextUtils.isEmpty(city) || TextUtils.isEmpty(state)
                        || TextUtils.isEmpty(zip) || TextUtils.isEmpty(emergencyno) || TextUtils.isEmpty(stdclass)
                        || TextUtils.isEmpty(section)) {
                    Toast.makeText(AddStudent.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImageToFirebase(studentId, name, school, gender, dob, bloodgroup, fathername, mothername,
                            parentcontno, address1, city, state, zip, emergencyno, stdclass, section);
                }
            }
        });


    }

    private void saveUserToFirestore(String studentId, String name, String school, String gender, String dob, String bloodgroup,
                                     String fathername, String mothername, String parentcontno, String address1,
                                     String city, String state, String zip, String emergencyno, String stdclass, String section, String imageUrl) {

        Map<String, Object> studentData = new HashMap<>();
        studentData.put("name", name);
        studentData.put("school", school);
        studentData.put("gender", gender);
        studentData.put("dob", dob);
        studentData.put("bloodgroup", bloodgroup);
        studentData.put("fathername", fathername);
        studentData.put("mothername", mothername);
        studentData.put("parentcontno", parentcontno);
        studentData.put("address1", address1);
        studentData.put("city", city);
        studentData.put("state", state);
        studentData.put("zip", zip);
        studentData.put("emergencyno", emergencyno);
        studentData.put("stdclass", stdclass);
        studentData.put("section", section);
        studentData.put("imageUrl", imageUrl);  // Ensure imageUrl is included

        db.collection("studentdetails").document(studentId)
                .set(studentData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Student Details Added Successfully!", Toast.LENGTH_SHORT).show();
                    clear();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    public void clear() {
        edt_name.setText("");
        edt_schoolname.setText("");
        edt_dob.setText("");
        edt_bloodgroup.setText("");
        edt_fathername.setText("");
        edt_mothername.setText("");
        edt_parentcontno.setText("");
        edt_address1.setText("");
        edt_city.setText("");
        edt_state.setText("");
        edt_zip.setText("");
        edt_emergencyno.setText("");
        spn_class.setSelection(0);
        spn_section.setSelection(0);
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the selected date
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(calendar.getTime());

                    // Set formatted date to EditText
                    edt_dob.setText(formattedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image")
                .setItems(new String[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            pickImageFromGallery();
                        } else {
                            captureImageFromCamera();
                        }
                    }
                })
                .show();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        }
    }


    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void captureImageFromCamera() {
        try {
            File photoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "profile.jpg");

            if (!photoFile.exists()) {
                photoFile.createNewFile();
            }

            imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", photoFile);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void saveImageLocally(Uri imageUri, String studentId) {
        try {
            // Define the local directory
            File directory = new File(getExternalFilesDir(null), "student_images");
            if (!directory.exists()) {
                directory.mkdirs(); // Create directory if not exists
            }

            // Define the file path
            File file = new File(directory, studentId + "_" + System.currentTimeMillis() + ".jpg");

            // Copy the image from the URI to the local storage
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            // Get the absolute path of the saved image
            imagePath = file.getAbsolutePath();
            Toast.makeText(this, "Image saved at: " + imagePath, Toast.LENGTH_SHORT).show();


        } catch (IOException e) {
            Toast.makeText(this, "Failed to save image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY && data != null) {
                imageUri = data.getData();
                imgProfile.setImageURI(imageUri);
                saveImageLocally(imageUri, "studentId");
            } else if (requestCode == REQUEST_CAMERA) {
                if (imageUri != null) {
                    imgProfile.setImageURI(imageUri);
                    saveImageLocally(imageUri, "studentId");
                } else {
                    Toast.makeText(this, "Camera capture failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadImageToFirebase(String studentId, String name, String school, String gender, String dob, String bloodgroup,
                                       String fathername, String mothername, String parentcontno, String address1,
                                       String city, String state, String zip, String emergencyno, String stdclass, String section) {

        if (imagePath != null) {

            // Now, save data to Firestore with image URL
            saveUserToFirestore(studentId, name, school, gender, dob, bloodgroup, fathername, mothername,
                    parentcontno, address1, city, state, zip, emergencyno, stdclass, section, imagePath);


        } else {
            // If no image, proceed without image URL
            saveUserToFirestore(studentId, name, school, gender, dob, bloodgroup, fathername, mothername,
                    parentcontno, address1, city, state, zip, emergencyno, stdclass, section, null);
        }
    }


}