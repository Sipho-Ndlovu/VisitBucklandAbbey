package com.example.visitbucklandabbeyapp;

public class Student {
    private int studentID;
    private String first_Name;
    private String second_Name;

    public Student() {}
    public Student(int studentID, String first_Name, String second_Name) {
        this.studentID = studentID;
        this.first_Name = first_Name;
        this.second_Name = second_Name;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getFirst_Name() {
        return first_Name;
    }

    public void setFirst_Name(String first_Name) {
        this.first_Name = first_Name;
    }

    public String getSecond_Name() {
        return second_Name;
    }

    public void setSecond_Name(String second_Name) {
        this.second_Name = second_Name;
    }
}
