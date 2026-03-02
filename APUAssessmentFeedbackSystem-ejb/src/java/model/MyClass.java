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
 *
 *
 * @author USER
 *
 */
@Entity

public class MyClass implements Serializable {

    @Id

    @Column(nullable = false, unique = true)

    private String classID;
    private String className;
    private String moduleID;
    private String field;
    
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getModuleID() {
        return moduleID;
    }

    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public MyClass(String classID, String className, String moduleID, String field) {
        this.classID = classID;
        this.className = className;
        this.moduleID = moduleID;
        this.field = field;
    }

    public MyClass() {
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (classID != null ? classID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MyClass)) {
            return false;
        }
        MyClass other = (MyClass) object;
        if ((this.classID == null && other.classID != null) || (this.classID != null && !this.classID.equals(other.classID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.MyClass[ classID=" + classID + " ]";
    }
}
