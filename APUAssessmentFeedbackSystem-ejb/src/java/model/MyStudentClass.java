/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
 
/**
*
* @author User
*/

@Entity
public class MyStudentClass implements Serializable {
    
    @Id
    @Column(nullable = false, unique = true)
    private String studentClassID; //StudentName_ClassID
    private String studentName;
    private String classID;

    public String getStudentClassID() {
        return studentClassID;
    }
 
    public void setStudentClassID(String id) {
        this.studentClassID = id;
    }
 
    public String getStudentName() {
        return studentName;
    }
 
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
 
    public String getClassID() {
        return classID;
    }
 
    public void setClassID(String classID) {
        this.classID = classID;
    }
 
    public MyStudentClass(String studentClassID, String studentName, String classID) {
        this.studentClassID = studentClassID;
        this.studentName = studentName;
        this.classID = classID;
    }
 
    public MyStudentClass() {
    }    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (studentClassID != null ? studentClassID.hashCode() : 0);
        return hash;
    }
 
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MyStudentClass)) {
            return false;
        }
        MyStudentClass other = (MyStudentClass) object;
        if ((this.studentClassID == null && other.studentClassID != null) || (this.studentClassID != null && !this.studentClassID.equals(other.studentClassID))) {
            return false;
        }
        return true;
    }
 
    @Override
    public String toString() {
        return "model.MyStudentClass[ studentClassID=" + studentClassID + " ]";
    }
}

 