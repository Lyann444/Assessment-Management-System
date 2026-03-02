<%-- 
    Document   : adminDashboard
    Created on : Dec 20, 2025, 10:17:05 PM
    Author     : User
--%>


<%@ page contentType="text/html;charset=UTF-8" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>

<jsp:include page="/general/header.jsp" />

<div class="wrapper">

    <jsp:include page="/general/sidebar.jsp" />
    <jsp:include page="/general/editProfile.jsp" />

    <div class="content">
        <!-- Tabs Row -->
        <div class="tabs-row">
            <div class="tabs-wrapper">
                <div class="tabs">
                    <div class="tab" data-page="${pageContext.request.contextPath}/AdminManageUsers" data-tab="manageUser"> Manage User </div>
                    <div class="tab" data-page="${pageContext.request.contextPath}/AdminManageLecturer" data-tab="manageLecturer"> Manage Lecturer </div>                    
                    <div class="tab" data-page="${pageContext.request.contextPath}/AdminManageGrading" data-tab="manageGrading">Manage Grading</div>
                    <div class="tab" data-page="${pageContext.request.contextPath}/AdminManageClass" data-tab="manageClass">Manage Class</div>
                    <div class="tab" data-page="${pageContext.request.contextPath}/AdminReports" data-tab="adminReports">Reports</div>
                </div>
            </div>

                 <!-- Edit Profile Button -->
            <c:if test="${loggedInUsername ne 'superadmin'}">
                <div class="edit-profile-btn" onclick="openEditProfile()">
                    <img src="${pageContext.request.contextPath}/images/profileIcon.png" alt="Edit Profile">
                </div>
            </c:if>
        </div>

        <!-- Dynamic Content -->
        <div class="dynamic-content" id="dynamicContent">

            <!-- DEFAULT WELCOME VIEW -->
            <div class="welcome-panel" id="welcomePanel">
                <div class="slide-in">
                    <h2>Hi ${loggedInUsername}!</h2>
                    <p>Welcome to Admin Dashboard</p>
                </div>
            </div>
            
        </div>
    </div>
</div>

<jsp:include page="/general/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
//adminDashboard.jsp
function showTab(tab, title) {
    // Change active tab
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    tab.classList.add('active');    
}

function setActiveTab(tab) {
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    tab.classList.add('active');
}

function loadPage(page) {
    fetch(page)
        .then(response => response.text())
        .then(data => {
            const container = document.getElementById("dynamicContent");
            container.innerHTML = "";      // clear welcome content
            container.innerHTML = data;    // load tab content

            // If we just loaded the reports page, initialize the charts
            if (page.includes("AdminReports")) {
                // Use requestAnimationFrame to wait for the browser to render the HTML
                requestAnimationFrame(() => {
                    // Double check if the element exists now
                    if (document.getElementById('chartUsersByType')) {
                        initCharts();
                    } else {
                        // If it's still not there, try a tiny delay as a fallback
                        setTimeout(initCharts, 50);
                    }
                });
            }
            
            // Make the loaded tables sortable
            container.querySelectorAll("table[data-sortable='true']").forEach(table => {
                makeTableSortable(table);
            });            
        })
    .catch(err => console.error("Fetch error:", err));
}

document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll('.tabs .tab').forEach(tab => {
        tab.addEventListener('click', function() {
            const page = tab.getAttribute('data-page');
            if (page) loadPage(page);
            
            setActiveTab(tab);
        });
    });
    
    //restore tab from url
    const urlParams = new URLSearchParams(window.location.search);
    const tab = urlParams.get("tab");
    
    // Hide welcome if tab exists
    if (tab) {
        const welcome = document.getElementById("welcomePanel");
        if (welcome) welcome.style.display = "none";
    }

    if (tab === "manageUser") {
        loadPage("${pageContext.request.contextPath}/AdminManageUsers");
        document.querySelectorAll(".tab")[0].classList.add("active");
    }

    if (tab === "manageLecturer") {
        loadPage("${pageContext.request.contextPath}/AdminManageLecturer");
        document.querySelectorAll(".tab")[1].classList.add("active");
    }

    if (tab === "manageGrading") {
        loadPage("${pageContext.request.contextPath}/AdminManageGrading");
        document.querySelectorAll(".tab")[2].classList.add("active");
    }

    if (tab === "manageClass") {
        loadPage("${pageContext.request.contextPath}/AdminManageClass");
        document.querySelectorAll(".tab")[3].classList.add("active");
    }

    if (tab === "adminReports") {
        loadPage("${pageContext.request.contextPath}/AdminReports");
        document.querySelectorAll(".tab")[4].classList.add("active");
    }

    // User alerts
    // get targetUser from session
    const targetUser = '<%= session.getAttribute("targetUser") != null ? session.getAttribute("targetUser") : "" %>';

    if (urlParams.get("addedUser") === "true") {
        alert(`User "${targetUser}" has been added successfully!`);
    } else if (urlParams.get("addedUser") === "false") {
        const error = urlParams.get("error");
    if (error === "username") {
        alert(`Username "${targetUser}" already exists.`);
        } else if (error === "weakpassword") {
            alert("Password must be at least 8 characters, include uppercase, number and symbol.");
        } else if (error === "phone") {
            alert("Phone number must be numeric and 10–12 digits.");
        } else if (error === "ic") {
            alert("IC number must be exactly 12 digits.");
        } else if (error === "email") {
            alert("Invalid email format.");
        } else {
            alert(`Failed to add user "${targetUser}".`);
        }
    } else if (urlParams.get("editedUser") === "true") {
        alert(`User "${targetUser}" has been edited successfully!`);
    } else if (urlParams.get("editedUser") === "false") {
        const error = urlParams.get("error");
        if (error === "phone") {
            alert("Phone number must be numeric and 10–12 digits.");
        } else if (error === "ic") {
            alert("IC number must be exactly 12 digits.");
        } else if (error === "email") {
            alert("Invalid email format.");
        } else if (error === "invalid") {
            alert("All fields must be filled correctly.");
        } else if (error === "notfound") {
            alert(`User "${targetUser}" not found.`);
        } else {
            alert(`Failed to edit user "${targetUser}".`);
        }
    } else if (urlParams.get("blockUser") === "true") {
        alert(`User "${targetUser}" has been blocked successfully!`);
    } else if (urlParams.get("blockUser") === "false") {
        alert(`Failed to blocked user "${targetUser}". Please try again.`);
    }

    // Grade alerts
    const targetGrade = '<%= session.getAttribute("targetGrade") != null ? session.getAttribute("targetGrade") : "" %>';
    
    if (urlParams.get("addedGrade") === "true") {
        alert(`Grade "${targetGrade}" has been added successfully!`);
    } else if (urlParams.get("addedGrade") === "false") {
        alert(`Failed to add grade "${targetGrade}". Please try again.`);
    } else if (urlParams.get("editedGrade") === "true") {
        alert(`Grade "${targetGrade}" has been updated successfully!`);
    } else if (urlParams.get("editedGrade") === "false") {
        alert(`Failed to update grade "${targetGrade}". Please try again.`);
    } else if (urlParams.get("deletedGrade") === "true") {
        alert(`Grade "${targetGrade}" has been deleted successfully!`);
    } else if (urlParams.get("deletedGrade") === "false") {
        alert(`Failed to delete grade "${targetGrade}". Please try again.`);
    } else if (urlParams.get("errorGrade") === "true") {
        alert(`An error occurred while processing grade "${targetGrade}".`);
    }
 
    // Lecturer Assignment Alerts
    const targetLecturer = '<%= session.getAttribute("targetLecturer") != null ? session.getAttribute("targetLecturer") : "" %>';
    const targetAcademicLeader = '<%= session.getAttribute("targetAcademicLeader") != null ? session.getAttribute("targetAcademicLeader") : "" %>';
    
    if (urlParams.get("assignedLecturer") === "true") {
        alert(`Lecturer "${targetLecturer}" has been successfully assigned to Academic Leader "${targetAcademicLeader}"!`);
    } else if (urlParams.get("assignedLecturer") === "false") {
        alert(`Failed to assign Lecturer "${targetLecturer}" to Academic Leader "${targetAcademicLeader}". Please ensure all selections are valid.`);
    } else if (urlParams.get("editedLecturer") === "true") {
        alert(`Lecturer "${targetLecturer}" assignment has been updated successfully!`);
    } else if (urlParams.get("editedLecturer") === "false") {
        alert(`Failed to update assignment for Lecturer "${targetLecturer}".`);
    } else if (urlParams.get("deletedLecturer") === "true") {
        alert(`Lecturer "${targetLecturer}" assignment has been removed successfully!`);
    } else if (urlParams.get("deletedLecturer") === "false") {
        alert(`Failed to remove assignment for Lecturer "${targetLecturer}".`);
    }

    // Manage Class Alerts
    const targetClassName = '<%= session.getAttribute("targetClassName") != null ? session.getAttribute("targetClassName") : "" %>';
    const targetModuleID = '<%= session.getAttribute("targetModuleID") != null ? session.getAttribute("targetModuleID") : "" %>';

    if (urlParams.get("addedClass") === "true") {
        alert(`Class "${targetClassName}" for Module "${targetModuleID}" has been created and students registered successfully!`);
    } else if (urlParams.get("addedClass") === "false") {
        alert(`Failed to create Class "${targetClassName}". Please try again.`);
    } else if (urlParams.get("deletedClass") === "true") {
        alert(`Class "${targetClassName}" and all its student registrations have been removed.`);
    } else if (urlParams.get("deletedClass") === "false") {
        alert(`Failed to remove class information.`);
    } else if (urlParams.get("duplicateClass") === "true") {
        alert(`Error: A class with the same class name already exists!`);
    }
    
    // --- Profile Update Alerts ---
    if (urlParams.get("profileUpdated") === "true") {
        showGlobalPopup("Profile updated successfully!", "success");

        // clean ONLY profile params
        setTimeout(() => {
            window.history.replaceState({}, document.title, window.location.pathname);
        }, 200);

    } else if (urlParams.get("profileUpdated") === "false") {
        const error = urlParams.get("error");

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


    
    <%
        // Remove session after using it
        if (session.getAttribute("targetUser") != null) {
            session.removeAttribute("targetUser");
        }
        
        if (session.getAttribute("targetGrade") != null) {
            session.removeAttribute("targetGrade");
        }
        
        if (session.getAttribute("targetLecturer") != null) {
            session.removeAttribute("targetLecturer");
        }
        
        if (session.getAttribute("targetAcademicLeader") != null) {
            session.removeAttribute("targetAcademicLeader");
        }
        
        if (session.getAttribute("targetClassName") != null) { 
            session.removeAttribute("targetClassName"); }
        
        if (session.getAttribute("targetModuleID") != null) { 
            session.removeAttribute("targetModuleID"); }
    %>
            
    setTimeout(() => {
        const params = new URLSearchParams(window.location.search);

        if (
            params.get("addedUser") ||
            params.get("editedUser") ||
            params.get("blockUser") ||
            params.get("addedGrade") ||
            params.get("editedGrade") ||
            params.get("deletedGrade") ||
            params.get("profileUpdated") ||
            params.get("tab")
        ) {
            window.history.replaceState({},document.title,window.location.pathname);
        }
    }, 200);

});



// adminManageUser.jsp
// Function to make a table sortable
function makeTableSortable(table) {
    const headers = table.querySelectorAll("th.sortable");
    headers.forEach((header, index) => {
        header.classList.add("sortable"); // for CSS hover arrow
        header.style.cursor = "pointer";  // hand cursor

        header.addEventListener("click", () => {
            const tbody = table.querySelector("tbody");
            const rows = Array.from(tbody.querySelectorAll("tr"));
            const isNumeric = rows.every(row => !isNaN(row.cells[index].innerText.trim()));

            // Determine new sort order
            let asc = !header.classList.contains("asc");
            
            // Remove sort classes from all headers
            headers.forEach(h => h.classList.remove("asc", "desc"));
            
            // Add current sort class
            header.classList.add(asc ? "asc" : "desc");

            // Sort rows
            rows.sort((a, b) => {
                let aText = a.cells[index].innerText.trim();
                let bText = b.cells[index].innerText.trim();

                if (isNumeric) {
                    aText = parseFloat(aText);
                    bText = parseFloat(bText);
                }

                if (aText < bText) return asc ? -1 : 1;
                if (aText > bText) return asc ? 1 : -1;
                return 0;
            });

            // Append sorted rows
            rows.forEach(row => tbody.appendChild(row));
        });
    });
}

// Search/filter function
function searchUserTable() {
    const input = document.getElementById("searchUser");
    const filter = input.value.toLowerCase();
    const table = document.getElementById("usersTable");
    const rows = table.getElementsByTagName("tr");

    for (let i = 1; i < rows.length; i++) { // skip header row
        let rowText = rows[i].innerText.toLowerCase();
        rows[i].style.display = rowText.includes(filter) ? "" : "none";
    }
}

// Add new user function
// Open Add User Modal
function openAddUserModal() {
    document.getElementById("addUserModal").style.display = "block";
}

// Close Add User Modal
function closeAddUserModal() {
    document.getElementById("addUserModal").style.display = "none";
}

// Close modal if clicking outside the modal content
window.onclick = function(event) {
    const modal = document.getElementById("addUserModal");
    if (event.target === modal) {
        modal.style.display = "none";
    }
}

// Edit user function
function enableEdit(button) {
    const row = button.closest("tr");
    const form = button.closest("form");
    const cells = row.querySelectorAll("td");

    for (let i = 1; i <= 8; i++) {
        const text = cells[i].innerText.trim()
        cells[i].innerHTML = "";
        const select = document.createElement("select");
        select.classList.add("edit-field");

        // Gender
        if (i === 1) {
            const select = document.createElement("select");
            ["Male", "Female"].forEach(opt => {
                const o = document.createElement("option");
                o.value = opt;
                o.text = opt;
                if (opt === text) o.selected = true;
                select.appendChild(o);
            });
            cells[i].appendChild(select);
        }

        // User Type
        else if (i === 6) {
            const select = document.createElement("select");
            ["Admin", "Academic Leader", "Lecturer", "Student"].forEach(opt => {
                const o = document.createElement("option");
                o.value = opt;
                o.text = opt;
                if (opt === text) o.selected = true;
                select.appendChild(o);
            });
            cells[i].appendChild(select);
        }
        // Field (datalist)
        else if (i === 7) {
            const input = document.createElement("input");
            input.setAttribute("list", "fieldOptionsEdit");
            input.value = text;
            input.classList.add("edit-field");
            cells[i].appendChild(input);
        }
        // Status
        else if (i === 8) {
            const select = document.createElement("select");
            ["Active", "Blocked"].forEach(opt => {
                const o = document.createElement("option");
                o.value = opt;
                o.text = opt;
                if (opt === text) o.selected = true;
                select.appendChild(o);
            });
            cells[i].appendChild(select);
        }
        // Text inputs
        else {
            const input = document.createElement("input");
            input.type = "text";
            input.value = text;
            input.classList.add("edit-field");
            cells[i].appendChild(input);
        }
    }
        
    // Toggle buttons
    button.style.display = "none"; // hide Edit
    form.querySelector("button[type='submit']").style.display = "inline"; // show Save
}

function prepareEdit(form) {
    const row = form.closest("tr");
    const cells = row.querySelectorAll("td");

    // Collect values
    const gender      = cells[1].firstChild.value.trim();
    const phoneNumber = cells[2].firstChild.value.trim();
    const icNumber    = cells[3].firstChild.value.trim();
    const email       = cells[4].firstChild.value.trim();
    const address     = cells[5].firstChild.value.trim();
    const userType    = cells[6].firstChild.value.trim();
    const field       = cells[7].firstChild.value.trim();
    const status      = cells[8].firstChild.value.trim();

    // Check for empty fields
    if (!gender || !phoneNumber || !icNumber || !email || !address || !userType || !field || !status) {
        alert("All fields must be filled out before saving!");
        return false; // prevent form submission
    }

    // Copy values to hidden inputs
    form.gender.value      = gender;
    form.phoneNumber.value = phoneNumber;
    form.icNumber.value    = icNumber;
    form.email.value       = email;
    form.address.value     = address;
    form.userType.value    = userType;
    form.field.value       = field;
    form.status.value      = status;

    return true; // allow submit
}

// Delete user function
// Confirm before deleting a user
function confirmDelete(form) {
    return confirm(`Are you sure you want to block this user`);
}

// adminManageGrading.jsp
// Modal functions
function openAddGradeModal() { 
    document.getElementById("addGradeModal").style.display = "block"; 
}

function closeAddGradeModal() { 
    document.getElementById("addGradeModal").style.display = "none"; 
}

window.addEventListener('click', function(event) {
    const modal = document.getElementById("addGradeModal");
    if (event.target === modal) modal.style.display = "none";
});

// Edit Grade
function enableEditGrade(button) {
    const row = button.closest("tr");
    const cells = row.querySelectorAll("td");

    // Get current values
    const minMarkText = cells[1].dataset.min;
    const maxMarkText = cells[1].dataset.max;
    const gradePointText = cells[2].dataset.point;

    // Clear existing content
    cells[1].innerHTML = "";
    cells[2].innerHTML = "";

    // Create min mark input
    const minInput = document.createElement("input");
    minInput.type = "number";
    minInput.name = "minMark";
    minInput.step = "0.1";
    minInput.min = "0";
    minInput.value = minMarkText;
    minInput.style.width = "60px";

    // Create max mark input
    const maxInput = document.createElement("input");
    maxInput.type = "number";
    maxInput.name = "maxMark";
    maxInput.step = "0.1";
    maxInput.min = "0";
    maxInput.value = maxMarkText;
    maxInput.style.width = "60px";

    // Append inputs with a dash
    cells[1].appendChild(minInput);
    cells[1].appendChild(document.createTextNode(" - "));
    cells[1].appendChild(maxInput);

    // Create grade point input
    const gradeInput = document.createElement("input");
    gradeInput.type = "number";
    gradeInput.name = "gradePoint";
    gradeInput.step = "0.1";
    gradeInput.min = "0";
    gradeInput.value = gradePointText;
    gradeInput.style.width = "60px";

    cells[2].appendChild(gradeInput);

    // Show Save button, hide Edit
    button.style.display = "none";
    row.querySelector("button[type='submit']").style.display = "inline";
}

function prepareEditGrade(form) {
    const row = form.closest("tr");
    const inputs = row.querySelectorAll("input");

    const minMark = inputs[0].value.trim();
    const maxMark = inputs[1].value.trim();
    const gradePoint = inputs[2].value.trim();

    if (!minMark || !maxMark || !gradePoint) { 
        alert("All fields must be filled!");
        return false; 
    }
    
    if (parseFloat(minMark) >= parseFloat(maxMark)) { 
        alert("MinMark must be less than MaxMark!"); 
        return false;
    }

    if (isNaN(minMark) || isNaN(maxMark) || isNaN(gradePoint)) {
        alert("Please enter valid numbers!");
        return false;
    }

    if (minMark < 0 || maxMark < 0 || gradePoint < 0) {
        alert("Negative numbers are not allowed!");
        return false;
    }


    if (minMark > 100 || maxMark > 100) {
        alert("Numbers more than 100 are not allowed!");
        return false;
    }
    
    
    if (gradePoint > 4) {
        alert("Grade Point more than 4.0 is not allowed!");
        return false;
    }
    
    if (isRangeOverlappingClient(minMark, maxMark, row)) {
        alert("This range overlaps with an existing grade!");
        return false;
    }

    form.minMark.value = minMark;
    form.maxMark.value = maxMark;
    form.gradePoint.value = gradePoint;

    return true;
}


// Delete grade confirmation
function confirmDeleteGrade(form) {
    return confirm('Are you sure you want to delete this grade?');
}

//  Search function
function searchGradeTable() {
    const input = document.getElementById("searchGrade").value.toLowerCase();
    const rows = document.getElementById("gradingTable").getElementsByTagName("tr");

    for (let i = 1; i < rows.length; i++) {
        let text = rows[i].innerText.toLowerCase();
        rows[i].style.display = text.includes(input) ? "" : "none";
    }
}

function isRangeOverlappingClient(min, max, excludeRow = null) {
    const rows = document.querySelectorAll("#gradingTable tbody tr");
    min = parseFloat(min);
    max = parseFloat(max);

    for (const row of rows) {
        if (row === excludeRow) continue; // skip the current row when editing

        const cells = row.querySelectorAll("td");
        if (!cells[1]) continue;

        const [rowMin, rowMax] = cells[1].innerText.split(" - ").map(x => parseFloat(x));

        // Check overlap
        if ((min >= rowMin && min <= rowMax) ||
            (max >= rowMin && max <= rowMax) ||
            (rowMin >= min && rowMin <= max) ||
            (rowMax >= min && rowMax <= max)) {
            return true;
        }
    }
    return false;
}

// Delete lecturer assignation confirmation 
function confirmRemoveAssignation(form) {
    return confirm('Are you sure you want to remove this lecturer assignation?');
}

//adminManageClass.jsp
function loadModuleDetails(moduleId) {
    if (!moduleId) return;
    fetch('${pageContext.request.contextPath}/AdminManageClass?action=getModuleInfo&moduleID=' + moduleId)
        .then(response => response.text())
        .then(html => {
            document.getElementById('moduleDetailsContainer').innerHTML = html;
    });
}

// Delete class confirmation 
function confirmRemoveClass(form) {
    return confirm('Are you sure you want to remove this class?');
}

//adminReports.jsp
function initCharts() {
    console.log("Initializing Analytics Dashboard...");

    // 1. Helper to safely grab data from the hidden inputs injected by fetch
    const getChartData = (id) => {
        const element = document.getElementById(id);
        if (!element || !element.value) {
            console.warn("Data element not found: " + id);
            return [];
        }
        try {
            // Converts the string "[1, 2, 3]" into a real JS Array
            return JSON.parse(element.value);
        } catch (e) {
            console.error("JSON Parse Error for " + id + ": ", e);
            return [];
        }
    };

    // Define Theme Colors
    const colors = {
        primary: '#2c3e50',
        secondary: '#34495e',
        active: '#2ecc71',
        blocked: '#e74c3c',
        blue: '#3498db',
        pink: '#e84393'
    };

    try {
        // --- Report 1: Users by Type (Bar) ---
        const ctxType = document.getElementById('chartUsersByType');
        const userTypeValues = getChartData('data-userType');

        if (ctxType) {
            new Chart(ctxType, {
                type: 'bar',
                data: {
                    labels: ['Students', 'Lecturers', 'Admins', 'Academic Leaders'],
                    datasets: [
                        {
                            label: 'Students',
                            data: [userTypeValues[0], 0, 0, 0],
                            backgroundColor: '#3498db'
                        },
                        {
                            label: 'Lecturers',
                            data: [0, userTypeValues[1], 0, 0],
                            backgroundColor: '#2ecc71'
                        },
                        {
                            label: 'Admins',
                            data: [0, 0, userTypeValues[2], 0],
                            backgroundColor: '#95a5a6'
                        },
                        {
                            label: 'Academic Leaders',
                            data: [0, 0, 0, userTypeValues[3]],
                            backgroundColor: '#f1c40f'
                        }
                    ]
                },
                options: { 
                    responsive: true, 
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top'
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: { stepSize: 1 }
                        }
                    }
                }
            });
        }
        
        // --- Report 2: Status Ratio (Doughnut) ---
        const ctxStatus = document.getElementById('chartStatus');
        if (ctxStatus) {
            new Chart(ctxStatus, {
                type: 'doughnut',
                data: {
                    labels: ['Active', 'Blocked'],
                    datasets: [{
                        data: getChartData('data-status'),
                        backgroundColor: [
                            colors.active,
                            colors.blocked
                        ]
                    }]
                },
                options: { cutout: '70%', responsive: true, maintainAspectRatio: false }
            });
        }

        // --- Report 3: Gender Distribution (Pie) ---
        const ctxGender = document.getElementById('chartGender');
        if (ctxGender) {
            new Chart(ctxGender, {
                type: 'pie',
                data: {
                    labels: ['Male', 'Female'],
                    datasets: [{
                        data: getChartData('data-gender'),
                        backgroundColor: [
                            colors.blue,
                            colors.pink
                        ]
                    }]
                },
                options: { responsive: true, maintainAspectRatio: false }
            });
        }

        // --- Report 4: Field Breakdown by Role (Stacked) ---
        const ctxField = document.getElementById('chartStudyField');
        if (ctxField) {
            new Chart(ctxField, {
                type: 'bar',
                data: {
                    labels: getChartData('data-fieldLabels'),
                    datasets: [
                        {
                            label: 'Students',
                            data: getChartData('data-role-students'),
                            backgroundColor: '#3498db' // Blue
                        },
                        {
                            label: 'Lecturers',
                            data: getChartData('data-role-lecturers'),
                            backgroundColor: '#2ecc71' // Green
                        },
                        {
                            label: 'Academic Leaders',
                            data: getChartData('data-role-leaders'),
                            backgroundColor: '#f1c40f' // Yellow
                        },
                        {
                            label: 'Admins',
                            data: getChartData('data-role-admins'),
                            backgroundColor: '#95a5a6' // Gray
                        }
                    ]
                },
                options: {
                    indexAxis: 'y',
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        x: { stacked: true, beginAtZero: true },
                        y: { stacked: true }
                    }
                }
            });
        }
        
        // --- Report 5: Lecturer Workload (Bar) ---
        const ctxWorkload = document.getElementById('chartWorkload');
        if (ctxWorkload) {
            new Chart(ctxWorkload, {
                type: 'bar',
                data: {
                    labels: getChartData('data-leaderLabels'),
                    datasets: [{
                        label: 'Assigned Lecturers',
                        data: getChartData('data-leaderData'),
                        backgroundColor: getChartData('data-leaderLabels')
                            .map(() => colors.primary)
                    }]
                },
                options: { 
                    responsive: true, 
                    maintainAspectRatio: false,
                    scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
                }
            });
        }

        console.log("All charts rendered successfully.");

    } catch (err) {
        console.error("Chart Rendering Interrupted: ", err);
    }
}

function showGlobalPopup(message, type = "warning") {
    let popup = document.querySelector(".global-popup");

    if (!popup) {
        popup = document.createElement("div");
        popup.className = "global-popup";
        document.body.appendChild(popup);
    }

    popup.className = "global-popup";
    if (type === "success") popup.classList.add("success");
    if (type === "error") popup.classList.add("error");

    popup.textContent = message;
    popup.classList.add("show");

    setTimeout(() => popup.classList.remove("show"), 4000);
}

document.getElementById("editProfileForm").addEventListener("submit", function () {
    const pwd = document.getElementById("password").value;
    console.log("Password length:", pwd.length);
    console.log("Contains special char:", /[!@#$%^&*()]/.test(pwd));
});

</script>
</body>
</html>
