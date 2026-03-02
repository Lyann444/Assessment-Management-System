<%-- 
    Document   : editProfile
    Created on : Dec 28, 2025, 3:18:06 AM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="../css/dashboard.css">

<div id="editProfileModal" class="modal">
    <div class="editProfileModalContent">
        <div class="editProfileModalHeader">
            <h2>Edit Profile</h2>
            <span class="editProfile-close-btn" onclick="closeEditProfile()">&times;</span>
        </div>

        <form id="editProfileForm"
              action="<%= request.getContextPath() %>/EditProfile"
              method="POST">

            <div class="editProfile-form-group">
                <label>Username:</label>
                <input type="text" name="username" value="${loggedInUser.username}" readonly title="Username cannot be edited" placeholder="Username cannot be edited">
            </div>

            <div class="editProfile-form-group">
                <label>Password:</label>
                <input type="password" id="password" name="password" value="${loggedInUser.password}" title="Edit password" required>
            </div>

            <div class="editProfile-form-group editProfile-password-checkbox-group">
                <label class="checkbox-label">
                    <input type="checkbox" onclick="togglePassword()"> Show Password
                </label>
            </div>

            <div class="editProfile-form-group">
                <label>Gender:</label>
                <select name="gender" required>
                    <option value="" disabled>-- Select Gender --</option>
                    <option value="Male" ${loggedInUser.gender == 'Male' ? 'selected' : ''}>Male</option>
                    <option value="Female" ${loggedInUser.gender == 'Female' ? 'selected' : ''}>Female</option>
                </select>
            </div>

            <div class="editProfile-form-group">
                <label>Phone Number:</label>
                <input type="text" name="phonenum" value="${loggedInUser.phoneNumber}" required>
            </div>

            <div class="editProfile-form-group">
                <label>IC Number:</label>
                <input type="text" name="icnum" value="${loggedInUser.icNumber}" required>
            </div>

            <div class="editProfile-form-group">
                <label>Email:</label>
                <input type="text" name="email" value="${loggedInUser.email}" required>
            </div>

            <div class="editProfile-form-group">
                <label>Address:</label>
                <textarea name="address" rows="3" required>${loggedInUser.address}</textarea>
            </div>

            <div class="editProfile-form-actions">
                <button type="submit" class="editProfile-btn-save">Save Changes</button>
            </div>

        </form>
    </div>
</div>

<script>
    function openEditProfile() {
        document.getElementById('editProfileModal').classList.add('show');
    }

    function closeEditProfile() {
        document.getElementById('editProfileModal').classList.remove('show');
    }

    window.onclick = function(event) {
        const modal = document.getElementById('editProfileModal');
        if (event.target === modal) closeEditProfile();
    }

    function togglePassword() {
        const passwordField = document.getElementById('password');
        passwordField.type = passwordField.type === 'password' ? 'text' : 'password';
    }
</script>

