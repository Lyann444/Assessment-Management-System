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
    @NamedQuery(
            name = "MyUser.searchUsername",
            query = "SELECT u FROM MyUser u WHERE u.username = :input"),

    @NamedQuery(
            name = "MyUser.findLecturersByAcademicLeader",
            query = "SELECT u FROM MyUser u WHERE u.userType = 'Lecturer' AND u.assignedAcademicLeader = :al"
    )
})
public class MyUser implements Serializable {

    @Id
    @Column(nullable = false, unique = true)
    private String username;
    private String password;
    private String gender;
    private String phoneNumber;
    private String icNumber;
    private String email;
    private String address;
    private String userType;
    private String field;
    private String assignedAcademicLeader;
    private String status;

    public MyUser() {
    }

    public MyUser(String username, String password, String gender, String phoneNumber, String icNumber, String email, String address, String userType, String field, String assignedAcademicLeader, String status) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.icNumber = icNumber;
        this.email = email;
        this.address = address;
        this.userType = userType;
        this.field = field;
        this.assignedAcademicLeader = assignedAcademicLeader;
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setAssignedAcademicLeader(String assignedAcademicLeader) {
        this.assignedAcademicLeader = assignedAcademicLeader;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getUserType() {
        return userType;
    }

    public String getField() {
        return field;
    }

    public String getAssignedAcademicLeader() {
        return assignedAcademicLeader;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MyUser)) {
            return false;
        }
        MyUser other = (MyUser) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.MyUser[ username=" + username + " ]";
    }

}
