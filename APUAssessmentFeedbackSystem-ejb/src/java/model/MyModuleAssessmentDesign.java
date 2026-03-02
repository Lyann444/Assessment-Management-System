/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author USER
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "MyModuleAssessmentDesign.findByAL",
            query = "SELECT d FROM MyModuleAssessmentDesign d "
            + "WHERE d.moduleName IN ("
            + "SELECT m.moduleName FROM MyModule m "
            + "WHERE m.academicLeaderAssigning = :alUsername)"
    ),

    @NamedQuery(
            name = "MyModuleAssessmentDesign.findByStatusAndAL",
            query = "SELECT d FROM MyModuleAssessmentDesign d "
            + "WHERE d.status = :status AND d.moduleName IN ("
            + "SELECT m.moduleName FROM MyModule m "
            + "WHERE m.academicLeaderAssigning = :alUsername)"
    )
})
public class MyModuleAssessmentDesign implements Serializable {

    @Id
    private String designID;
    private String moduleID;
    @Column(nullable = false)
    private String moduleName;
    @Column(nullable = false)
    private String lecturerUsername;
    private String status; // either PENDING/APPROVED/REJECTED
    private String feedback;
    private Timestamp submittedAt;
    @OneToMany(mappedBy = "design", cascade = CascadeType.ALL)
    private List<MyAssessment> assessments;

    public MyModuleAssessmentDesign() {
    }

    public MyModuleAssessmentDesign(String designID, String moduleID, String moduleName, String lecturerUsername, String status, String feedback, Timestamp submittedAt) {
        this.designID = designID;
        this.moduleID = moduleID;
        this.moduleName = moduleName;
        this.lecturerUsername = lecturerUsername;
        this.status = status;
        this.feedback = feedback;
        this.submittedAt = submittedAt;
    }

    public String getDesignID() {
        return designID;
    }

    public void setDesignID(String designID) {
        this.designID = designID;
    }

    public String getModuleID() {
        return moduleID;
    }

    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getLecturerUsername() {
        return lecturerUsername;
    }

    public void setLecturerUsername(String lecturerUsername) {
        this.lecturerUsername = lecturerUsername;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }

    public List<MyAssessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<MyAssessment> assessments) {
        this.assessments = assessments;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (designID != null ? designID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MyModuleAssessmentDesign)) {
            return false;
        }
        MyModuleAssessmentDesign other = (MyModuleAssessmentDesign) object;
        if ((this.designID == null && other.designID != null) || (this.designID != null && !this.designID.equals(other.designID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.MyModuleAssessmentDesign[ designID=" + designID + " ]";
    }

}
