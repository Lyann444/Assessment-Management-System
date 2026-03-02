/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author USER
 */
@Entity
@NamedQueries({
    //AL
    @NamedQuery(
            name = "MyModule.findLatestModuleID",
            query = "SELECT m.moduleID FROM MyModule m ORDER BY m.moduleID DESC"
    ),

    @NamedQuery(
            name = "MyModule.findByModuleName",
            query = "SELECT m FROM MyModule m WHERE m.moduleName = :name"
    ),

    @NamedQuery(
            name = "MyModule.findByAcademicLeader",
            query = "SELECT m FROM MyModule m WHERE m.academicLeaderAssigning = :al"
    ),

    @NamedQuery(
            name = "MyModule.findByAcademicLeaderActive",
            query = "SELECT m FROM MyModule m WHERE m.academicLeaderAssigning = :al AND m.status = 'active'"
    ),

    //LECTURER
    @NamedQuery(
            name = "MyModule.findByLecturer",
            query = "SELECT m FROM MyModule m " + "WHERE m.assignedLecturerUsername = :lecturer " + "AND m.status = 'Active'"
    ),
    
    @NamedQuery(
            name = "MyModule.findAvailableForDesign",
            query = "SELECT m FROM MyModule m "
            + "WHERE m.assignedLecturerUsername = :lecturer "
            + "AND m.status = 'Active' "
            + "AND m.moduleName NOT IN ("
            + "   SELECT d.moduleName FROM MyModuleAssessmentDesign d "
            + "   WHERE d.lecturerUsername = :lecturer"
            + ")"
    )
})
public class MyModule implements Serializable {

    @Id
    @Column(nullable = false, unique = true)
    private String moduleID;
    private String moduleName;
    private String moduleDescription;
    private String field;
    private String status;

    private String assignedLecturerUsername;
    private String academicLeaderAssigning;

    public MyModule() {
    }

    public MyModule(String moduleID, String moduleName, String moduleDescription, String field, String assignedLecturerUsername, String academicLeaderAssigning) {
        this.moduleID = moduleID;
        this.moduleName = moduleName;
        this.moduleDescription = moduleDescription;
        this.field = field;
        this.assignedLecturerUsername = assignedLecturerUsername;
        this.academicLeaderAssigning = academicLeaderAssigning;
        this.status = "Active";
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

    public String getModuleDescription() {
        return moduleDescription;
    }

    public void setModuleDescription(String moduleDescription) {
        this.moduleDescription = moduleDescription;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedLecturerUsername() {
        return assignedLecturerUsername;
    }

    public void setAssignedLecturerUsername(String assignedLecturerUsername) {
        this.assignedLecturerUsername = assignedLecturerUsername;
    }

    public String getAcademicLeaderAssigning() {
        return academicLeaderAssigning;
    }

    public void setAcademicLeaderAssigning(String academicLeaderAssigning) {
        this.academicLeaderAssigning = academicLeaderAssigning;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (moduleID != null ? moduleID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MyModule)) {
            return false;
        }
        MyModule other = (MyModule) object;
        if ((this.moduleID == null && other.moduleID != null) || (this.moduleID != null && !this.moduleID.equals(other.moduleID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.MyModule[ moduleID=" + moduleID + " ]";
    }

}
