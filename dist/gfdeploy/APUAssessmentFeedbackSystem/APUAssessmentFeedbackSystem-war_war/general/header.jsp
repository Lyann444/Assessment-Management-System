<%-- 
    Document   : header
    Created on : 19-Dec-2025, 09:39:25
    Author     : USER
--%>
 
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
 
<%
    String dashboardTitle = "Dashboard"; // default
 
    if (session != null) {
        String userType = (String) session.getAttribute("loggedInUserType");
        if (userType != null) {
            userType = userType.trim();
            if ("student".equalsIgnoreCase(userType)) {
                dashboardTitle = "Student Dashboard";
            } else if ("admin".equalsIgnoreCase(userType)) {
                dashboardTitle = "Admin Dashboard";
            } else if ("academic leader".equalsIgnoreCase(userType)) {
                dashboardTitle = "Academic Leader Dashboard";
            } else if ("lecturer".equalsIgnoreCase(userType)) {
                dashboardTitle = "Lecturer Dashboard";
            } else {
                dashboardTitle = "Dashboard";
            }
        }
    }
%>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= dashboardTitle %></title>
</head>
<body>
<div class="header">
<h2><%= dashboardTitle %></h2>
</div>
</body>
</html>
 