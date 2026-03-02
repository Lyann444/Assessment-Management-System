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
public class MyGradingSystem implements Serializable {

    @Id
    @Column(nullable = false, unique = true)
    private String grade;
    private double minMark;
    private double maxMark;
    private double gradePoint;

    public MyGradingSystem(String grade, double minMark, double maxMark, double gradePoint) {
        this.grade = grade;
        this.minMark = minMark;
        this.maxMark = maxMark;
        this.gradePoint = gradePoint;
    }

    public MyGradingSystem() {
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getMinMark() {
        return minMark;
    }

    public void setMinMark(double minMark) {
        this.minMark = minMark;
    }

    public double getMaxMark() {
        return maxMark;
    }

    public void setMaxMark(double maxMark) {
        this.maxMark = maxMark;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(double gradePoint) {
        this.gradePoint = gradePoint;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (grade != null ? grade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MyGradingSystem)) {
            return false;
        }
        MyGradingSystem other = (MyGradingSystem) object;
        if ((this.grade == null && other.grade != null) || (this.grade != null && !this.grade.equals(other.grade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.MyGradingSystem[ grade=" + grade + " ]";
    }
    
}
