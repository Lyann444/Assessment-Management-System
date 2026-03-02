<%-- 
    Document   : login
    Created on : 19-Dec-2025, 08:30:34
    Author     : USER
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/authentication.css">
</head>
<body>
<!-- successful registered -->
<c:if test="${requestScope.success == 'registered'}">
    <div class="global-popup success show" id="loginSuccess">
        Registration successful. Please log in.
    </div>
</c:if>

<!-- failed login -->
<c:if test="${requestScope.error == 'invalid'}">
    <div class="global-popup show" id="loginError">
        Invalid username or password.
    </div>
</c:if>
<c:if test="${requestScope.error == 'role'}">
    <div class="global-popup show" id="loginError">
        Invalid user role.
    </div>
</c:if>

<!-- blocked user -->
<c:if test="${requestScope.error == 'blocked'}">
    <div class="global-popup error show" id="loginBlocked">
        User account has been blocked. 
        Please contact admin via <strong>support@apu.edu.my</strong>.
    </div>
</c:if>
<div class="login-card">

    <h1>Log in</h1>

    <form action="${pageContext.request.contextPath}/Login" method="POST">

        <div class="form-group">
            <label>Username</label>
            <input type="text" name="username" required>
        </div>

        <div class="form-group">
            <label>Password</label>

            <div class="password-wrapper">
                <input type="password" name="password" id="password" required>
                <span class="toggle-password" onclick="togglePassword()">
                    Show
                </span>
            </div>
        </div>

        <button type="submit" class="login-btn">
            Log in →
        </button>

    </form>

    <div class="bottom-text">
        Don’t have an account?
        <a href="${pageContext.request.contextPath}/general/register.jsp">Sign up now!</a>
    </div>

</div>

<script>
function togglePassword() {
    const passwordField = document.getElementById("password");
    const toggleText = document.querySelector(".toggle-password");

    if (passwordField.type === "password") {
        passwordField.type = "text";
        toggleText.textContent = "Hide";
    } else {
        passwordField.type = "password";
        toggleText.textContent = "Show";
    }
}

setTimeout(() => {
    const success = document.getElementById("loginSuccess");
    if (success) success.classList.remove("show");
}, 4000);

setTimeout(() => {
    const err = document.getElementById("loginError");
    if (err) err.classList.remove("show");
}, 4000);
setTimeout(() => {
    const blocked = document.getElementById("loginBlocked");
    if (blocked) blocked.classList.remove("show");
}, 4000);
</script>
</body>
</html>
