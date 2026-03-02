<%-- 
    Document   : register
    Created on : 19-Dec-2025, 08:36:06
    Author     : USER
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/authentication.css">
</head>
<body>
<!--for server side security validation (if really bypass js validation (for ic,email,phone and password) -->
<c:if test="${not empty requestScope.error}">
    <div class="global-popup show" id="globalPopup">
        <span id="popupMessage">
            <c:choose>
                <c:when test="${requestScope.error == 'username'}">
                    Username already exists. Please try another.
                </c:when>
                <c:when test="${requestScope.error == 'weakpassword'}">
                    Password does not meet requirements.
                </c:when>
                <c:when test="${requestScope.error == 'email'}">
                    Invalid email format.
                </c:when>
                <c:when test="${requestScope.error == 'phone'}">
                    Phone number must contain digits only.
                </c:when>
                <c:when test="${requestScope.error == 'ic'}">
                    IC number must contain digits only.
                </c:when>
            </c:choose>
        </span>
    </div>
</c:if>

<div class="login-card">

    <h1>Register</h1>

    <form action="${pageContext.request.contextPath}/Register" method="POST">

        <!-- Username -->
        <div class="form-group">
            <label>Username</label>
            <input type="text" name="username" id="username" required>
        </div>

        <!-- Password -->    
        <div class="form-group password-group">
            <label>Password</label>

            <div class="password-wrapper">
                <input type="password" name="password" id="regPassword" required>
                <span class="toggle-password" onclick="toggleRegisterPassword()">Show</span>
            </div>

            <!-- Password tool tip -->
            <div class="tooltip password-tooltip" id="passwordTooltip">
                <strong>Password must contain:</strong>
                <ul>
                    <li id="rule-len">At least 8 characters</li>
                    <li id="rule-upper">One uppercase letter</li>
                    <li id="rule-num">One number</li>
                    <li id="rule-sym">One symbol</li>
                </ul>
            </div>
        </div>

        <!-- TWO-COLUMN SECTION -->
        <div class="form-grid">

            <div class="form-group">
                <label>Gender</label>
                <select name="gender" required>
                    <option value="">Select</option>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                </select>
            </div>

            <div class="form-group">
                <label>Phone Number</label>
                <input type="text" name="phonenum" id="phone" required>
            </div>

            <div class="form-group">
                <label>IC Number</label>
                <input type="text" name="icnum" id="ic" required>
            </div>

            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" id="email" required>
                <small id="emailMsg"></small>
            </div>

        </div>

        <!--address -->
        <div class="form-group">
            <label>Address</label>
            <input type="text" name="address" required>
        </div>

        <button type="submit" class="login-btn">
            Register →
        </button>

    </form>

    <div class="bottom-text">
        Already have an account?
        <a href="${pageContext.request.contextPath}/general/login.jsp">Log in</a>
    </div>

</div>

<script>
//for error message pop up
function showPopup(message) {
    let popup = document.getElementById("globalPopup");

    // If popup does not exist (no server error), create it
    if (!popup) {
        popup = document.createElement("div");
        popup.id = "globalPopup";
        popup.className = "global-popup show";

        const span = document.createElement("span");
        span.id = "popupMessage";
        popup.appendChild(span);

        document.body.appendChild(popup);
    }

    document.getElementById("popupMessage").textContent = message;
    popup.classList.add("show");

    // auto-hide
    setTimeout(() => {
        popup.classList.remove("show");
    }, 4000);
}

//TOGGLE PASSWORD
function toggleRegisterPassword() {
    const field = document.getElementById("regPassword");
    const toggle = document.querySelector(".toggle-password");

    if (field.type === "password") {
        field.type = "text";
        toggle.textContent = "Hide";
    } else {
        field.type = "password";
        toggle.textContent = "Show";
    }
}

//showing whether the email is valid or not 
document.getElementById("email").addEventListener("input", function () {
    const msg = document.getElementById("emailMsg");
    if (this.validity.valid) {
        msg.textContent = "Valid email";
        msg.style.color = "green";
    } else {
        msg.textContent = "Invalid email format";
        msg.style.color = "red";
    }
});

//FOR PASSWORD LIVE CHECKING
const passwordField = document.getElementById("regPassword");
const tooltip = document.getElementById("passwordTooltip");

passwordField.addEventListener("focus", () => {
    tooltip.classList.add("show");
});

passwordField.addEventListener("blur", () => {
    tooltip.classList.remove("show");
});

passwordField.addEventListener("keyup", () => {
    const v = passwordField.value;

    updateRule("rule-len", v.length >= 8);
    updateRule("rule-upper", /[A-Z]/.test(v));
    updateRule("rule-num", /[0-9]/.test(v));
    updateRule("rule-sym", /[^A-Za-z0-9]/.test(v));
});

function updateRule(id, isValid) {
    const el = document.getElementById(id);
    el.classList.toggle("valid", isValid);
    el.classList.toggle("invalid", !isValid);
}

const registerForm = document.querySelector("form");

const usernameInput = document.getElementById("username");
const globalPopup = document.getElementById("globalPopup");

if (usernameInput && globalPopup) {
    usernameInput.addEventListener("input", () => {
        globalPopup.classList.remove("show");
    });
}

document.querySelectorAll("input, select").forEach(el => {
    el.addEventListener("input", () => {
        const popup = document.getElementById("globalPopup");
        if (popup) popup.classList.remove("show");
    });
});

setTimeout(() => {
    const popup = document.getElementById("globalPopup");
    if (popup) popup.classList.remove("show");
}, 4000);

document.getElementById("phone").addEventListener("input", function () {
    this.value = this.value.replace(/[^0-9]/g, "");
});

document.getElementById("ic").addEventListener("input", function () {
    this.value = this.value.replace(/[^0-9]/g, "");
});


//WHEN SUBMIT
registerForm.addEventListener("submit", function (e) {
    
    //to clear old pop up
    const popup = document.getElementById("globalPopup");
    if (popup) popup.classList.remove("show");
    
    
    const password = document.getElementById("regPassword").value;
    const phone = document.getElementById("phone").value;
    const ic = document.getElementById("ic").value;

    const strongPassword =
        password.length >= 8 &&
        /[A-Z]/.test(password) &&
        /[0-9]/.test(password) &&
        /[^A-Za-z0-9]/.test(password);

    if (!strongPassword) {
        e.preventDefault(); 

        const tooltip = document.getElementById("passwordTooltip");
        tooltip.classList.add("show");
        return;
    }
    
    if (!/^\d{10,12}$/.test(phone)) {
        e.preventDefault();
        showPopup("Phone number must be numeric and 10–12 digits.");
        return;
    }

    if (!/^\d{12}$/.test(ic)) {
        e.preventDefault();
        showPopup("IC number must be exactly 12 digits.");
        return;
    }
});

</script>

</body>
</html>
