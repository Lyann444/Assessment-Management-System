/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author USER
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "MyAssessment.findByDesign",
            query = "SELECT a FROM MyAssessment a WHERE a.design = :design"
    )
})
public class MyAssessment implements Serializable {

    @Id
    private String assessmentID;

    @ManyToOne
    @JoinColumn(name = "designID")
    private MyModuleAssessmentDesign design;
    @Column(nullable = false)
    private String assessmentType;
    @Column(nullable = false)
    private double weightage;

    public MyAssessment() {
    }

    public MyAssessment(String assessmentID, MyModuleAssessmentDesign design, String assessmentType, double weightage) {
        this.assessmentID = assessmentID;
        this.design = design;
        this.assessmentType = assessmentType;
        this.weightage = weightage;
    }

    public String getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(String assessmentID) {
        this.assessmentID = assessmentID;
    }

    public MyModuleAssessmentDesign getDesign() {
        return design;
    }

    public void setDesign(MyModuleAssessmentDesign design) {
        this.design = design;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public double getWeightage() {
        return weightage;
    }

    public void setWeightage(double weightage) {
        this.weightage = weightage;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (assessmentID != null ? assessmentID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MyAssessment)) {
            return false;
        }
        MyAssessment other = (MyAssessment) object;
        if ((this.assessmentID == null && other.assessmentID != null) || (this.assessmentID != null && !this.assessmentID.equals(other.assessmentID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.MyAssessment[ assessmentID=" + assessmentID + " ]";
    }

}
