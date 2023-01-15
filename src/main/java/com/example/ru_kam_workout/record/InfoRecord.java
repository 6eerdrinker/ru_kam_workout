package com.example.ru_kam_workout.record;

import java.time.LocalDate;

public class InfoRecord {
    private final String studentName;

    private final String projectName;

    private final LocalDate projectDate;

    private final String description;

    public InfoRecord(String studentName, String projectName, LocalDate projectDate, String description) {
        this.studentName = studentName;
        this.projectName = projectName;
        this.projectDate = projectDate;
        this.description = description;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getProjectName() {
        return projectName;
    }

    public LocalDate getProjectDate() {
        return projectDate;
    }

    public String getDescription() {
        return description;
    }
}
