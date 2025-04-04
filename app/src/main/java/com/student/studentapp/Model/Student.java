package com.student.studentapp.Model;

public class Student {
    private String name;
    private String std;
    private String section;
    private String school;
    private String imageUrl;

    public Student(String name, String std, String section, String school, String imageUrl) {
        this.name = name;
        this.std = std;
        this.section = section;
        this.school = school;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
