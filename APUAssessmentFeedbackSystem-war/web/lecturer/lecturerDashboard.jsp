<%-- 
    Document   : LecturerDasboard
    Created on : 19-Dec-2025, 09:29:37
    Author     : USER
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/lecturer.css">


<html>
<body>

<jsp:include page="/general/header.jsp" />

<div class="wrapper">

    <jsp:include page="/general/sidebar.jsp" />
    <jsp:include page="/general/editProfile.jsp" />
    <div class="content">
        <div class="tabs-row">
            <!-- Tabs -->
            <div class="tabs-wrapper">
                <div class="tabs">
                    <div class="tab" onclick="loadPage('${pageContext.request.contextPath}/LecturerDesignAssessment'); setActiveTab(this);">
                        Design Assessment
                    </div>
                    <div class="tab" onclick="loadPage('${pageContext.request.contextPath}/LecturerManageAssessment'); setActiveTab(this);">
                        Manage Assessment
                    </div>
                    <div class="tab" onclick="loadPage('${pageContext.request.contextPath}/LecturerKeyInMarks'); setActiveTab(this);">
                        Key In Marks
                    </div>                    
                </div>
            </div>

            <!-- Edit Profile Button -->
            <div class="edit-profile-btn" onclick="openEditProfile()">
                <img src="${pageContext.request.contextPath}/images/profileIcon.png" alt="Edit Profile">
            </div>
        </div>
        
        <!-- Dynamic Content -->
        <div class="dynamic-content" id="dynamicContent">

            <!-- DEFAULT WELCOME VIEW -->
            <div class="welcome-panel">
                <div class="slide-in">
                    <h2>Hi ${loggedInUsername}!</h2>
                    <p>Welcome to Lecturer Dashboard</p>
                </div>
            </div>

        </div>
    </div>
</div>

<jsp:include page="/general/footer.jsp" />

<script src="${pageContext.request.contextPath}/js/lecturerAssessmentDesign.js"></script>
<script src="${pageContext.request.contextPath}/js/lecturerManageAssessment.js"></script>
<script src="${pageContext.request.contextPath}/js/lecturerKeyInMarks.js"></script>

</body>
</html>

<script>
const contextPath = "${pageContext.request.contextPath}";
function setActiveTab(tab) {
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    tab.classList.add('active');
}

function loadPage(page) {
    fetch(page)
        .then(response => response.text())
        .then(data => {
            document.getElementById("dynamicContent").innerHTML = data;

            if (page.includes("LecturerDesignAssessment")) {
                initLecturerAssessmentDesign();
            }
            
            if (page.includes("LecturerKeyInMarks")) {
                console.log("Key In Marks page loaded");
            }
        });
}
function filterStatus(status) {
    loadPage(
        contextPath + "/LecturerManageAssessment?status=" + status
    );
}
document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);

    if (params.get("tab") === "designAssessment") {
        loadPage(contextPath + "/LecturerDesignAssessment");
        document.querySelectorAll(".tab")[0].classList.add("active");
    }

    if (params.get("tab") === "manageAssessment") {
        loadPage(contextPath + "/LecturerManageAssessment");
        document.querySelectorAll(".tab")[1].classList.add("active");
    }
    
    if (params.get("tab") === "keyInMarks") {
        loadPage(contextPath + "/LecturerKeyInMarks");
        document.querySelectorAll(".tab")[2].classList.add("active");
    }

    if (params.get("success") === "submitted") {
        showGlobalPopup(
            "Assessment design submitted and pending approval",
            "success"
        );
    }

    if (params.get("error")) {
        const error = params.get("error");

        if (error === "duplicate") {
            showGlobalPopup("Assessment types cannot be duplicated.", "error");
        } else if (error === "weightage") {
            showGlobalPopup("Total weightage must be exactly 100%.", "error");
        } else if (error === "count") {
            showGlobalPopup("Assessment count must be between 1 and 3.", "error");
        } else if (error === "empty") {
            showGlobalPopup("Please select all assessment types.", "warning");
        } else {
            showGlobalPopup("Invalid assessment submission.", "error");
        }
    }

    if (params.get("success") === "resubmitted") {
        showGlobalPopup(
            "Assessment design resubmitted for approval",
            "success"
        );
    }

    if (params.get("success") === "saved") {
        showGlobalPopup(
            "Marks saved successfully",
            "success"
        );
    }
    
    // --- Profile Update Alerts ---
    if (params.get("profileUpdated") === "true") {
        showGlobalPopup("Profile updated successfully!", "success");

        // clean ONLY profile params
        setTimeout(() => {
            window.history.replaceState({}, document.title, window.location.pathname);
        }, 200);

    } else if (params.get("profileUpdated") === "false") {
        const error = params.get("error");

        if (error === "weakpassword") {
            showGlobalPopup(
                "Password must be at least 8 characters, include uppercase, number and symbol.",
                "error"
            );
        } else if (error === "phone") {
            showGlobalPopup("Phone number must be 10–12 digits.", "error");
        } else if (error === "ic") {
            showGlobalPopup("IC number must be exactly 12 digits.", "error");
        } else if (error === "email") {
            showGlobalPopup("Invalid email format.", "error");
        } else if (error === "REQUIRED_FIELDS_MISSING") {
            showGlobalPopup("All fields are required.", "error");
        } else {
            showGlobalPopup("Failed to update profile. Please try again.", "error");
        }

        // clean ONLY profile params
        setTimeout(() => {
            window.history.replaceState({}, document.title, window.location.pathname);
        }, 200);
    }
    
    if (params.get("success") || params.get("error") || params.get("tab") || params.get ("profileUpdated")) {
        window.history.replaceState({}, document.title, window.location.pathname);
    }
});

function showGlobalPopup(message, type = "warning") {
    let popup = document.querySelector(".global-popup");

    if (!popup) {
        popup = document.createElement("div");
        popup.className = "global-popup";
        document.body.appendChild(popup);
    }

    popup.className = "global-popup";
    if (type) popup.classList.add(type);
    popup.textContent = message;
    popup.classList.add("show");

    setTimeout(() => popup.classList.remove("show"), 4000);
}
</script>