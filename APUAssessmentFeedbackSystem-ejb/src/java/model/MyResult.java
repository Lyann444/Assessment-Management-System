/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author USER
 */
@Entity
public class MyResult implements Serializable {

    @Id
    @Column(nullable = false, unique = true)
    private String marksID;
    private double marks;
    private String feedback;
    private String studentName;
    private String assessmentID;

    public MyResult(String marksID, double marks, String feedback, String studentName, String assessmentID) {
        this.marksID = marksID;
        this.marks = marks;
        this.feedback = feedback;
        this.studentName = studentName;
        this.assessmentID = assessmentID;
    }

    public MyResult() {
    }

    public String getMarksID() {
        return marksID;
    }

    public void setMarksID(String marksID) {
        this.marksID = marksID;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(String assessmentID) {
        this.assessmentID = assessmentID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (marksID != null ? marksID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MyResult)) {
            return false;
        }
        MyResult other = (MyResult) object;
        if ((this.marksID == null && other.marksID != null) || (this.marksID != null && !this.marksID.equals(other.marksID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.MyResult[ marksID=" + marksID + " ]";
    }

}
 