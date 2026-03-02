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
public class FinalResultDTO implements Serializable {
    private String moduleName;
    private double totalMarks;
    private String finalGrade;
    private boolean isComplete; // To track if all assessments under same module are marked

    public FinalResultDTO(String moduleName, double totalMarks, String finalGrade, boolean isComplete) {
        this.moduleName = moduleName;
        this.totalMarks = totalMarks;
        this.finalGrade = finalGrade;
        this.isComplete = isComplete;
    }

    public FinalResultDTO() {
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(double totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }

    public boolean isIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }    
}
