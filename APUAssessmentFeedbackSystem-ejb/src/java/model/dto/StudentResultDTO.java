/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dto;

import java.io.Serializable;

/**
 *
 * @author User
 */
public class StudentResultDTO implements Serializable {
    private String moduleName;
    private String assessmentType;
    private double marks;
    private double weightage; 
    private String feedback;

    // Default Constructor
    public StudentResultDTO() {
    }

    // Full Constructor
    public StudentResultDTO(String moduleName, String assessmentType, double marks, double weightage, String feedback) {
        this.moduleName = moduleName;
        this.assessmentType = assessmentType;
        this.marks = marks;
        this.weightage = weightage;
        this.feedback = feedback;
    }

    // Getters and Setters
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public double getWeightage() {
        return weightage;
    }

    public void setWeightage(double weightage) {
        this.weightage = weightage;
    }


    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
