<%-- 
    Document   : sidebar
    Created on : 19-Dec-2025, 09:41:18
    Author     : USER
--%>
 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
 
<%
    String userType = "";
 
    if (session != null) {
        String type = (String) session.getAttribute("loggedInUserType");
        if (type != null) {
            type = type.trim();
            if ("student".equalsIgnoreCase(type)) {
                userType = "student";
            } else if ("admin".equalsIgnoreCase(type)) {
                userType = "admin";
            } else if ("academic leader".equalsIgnoreCase(type)) {
                userType = "academicLeader";
            } else if ("lecturer".equalsIgnoreCase(type)) {
                userType = "lecturer";
            }
        }
    }
 
    String username = (String) session.getAttribute("loggedInUsername");
%>

<html>
<head>
<meta charset="UTF-8">
<title> Sidebar </title>
</head>
<div class="sidebar">
 
    <img src="${pageContext.request.contextPath}/images/<%= userType %>.png" alt="<%= userType %>">

    <p>Welcome back!</p>
    <p><%= username != null ? username : "User" %></p>

    <!-- logout button -->
    <button class="logout-btn" onclick="confirmLogout()">
        Logout
    </button>
</div>
    
<!-- logout confirmation -->
<!-- CONFIRMATION POPUP -->
<div class="logout-popup" id="logoutPopup">
    <div class="popup-box">
        <p>Are you sure you want to logout?</p>
        <form action="${pageContext.request.contextPath}/Logout" method="POST">
            <button type="submit" class="confirm-btn">Yes</button>
            <button type="button" class="cancel-btn" onclick="closeLogout()">Cancel</button>
        </form>
    </div>
</div>
</html>    

<script>
function confirmLogout() {
    document.getElementById("logoutPopup").classList.add("show");
}

function closeLogout() {
    document.getElementById("logoutPopup").classList.remove("show");
}
</script>