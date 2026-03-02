<%-- 
    Document   : studentDashboard
    Created on : 19-Dec-2025, 09:29:37
    Author     : USER
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/student.css">

<html>
<body>

<jsp:include page="/general/header.jsp" />
<jsp:include page="/general/editProfile.jsp" />

<div class="wrapper">

    <jsp:include page="/general/sidebar.jsp" />

    <div class="content">
        <div class="tabs-row">
            <!-- Tabs -->
            <div class="tabs-wrapper">
                <div class="tabs">
                    <div class="tab" onclick="loadPage('${pageContext.request.contextPath}/StudentViewResult'); setActiveTab(this);">
                        View Result
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
                    <p>Welcome to Student Dashboard</p>
                </div>
            </div>

        </div>
    </div>
</div>

<jsp:include page="/general/footer.jsp" />

<!-- jsPDF core -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>

<!-- jsPDF AutoTable plugin -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.5.29/jspdf.plugin.autotable.min.js"></script>

<script>
<!--studentDashboard.jsp-->
function showTab(tab, title) {

    // Change active tab
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    tab.classList.add('active');

    // Replace ONLY dynamic content
    document.getElementById("dynamicContent").innerHTML =
        "<div class='welcome-panel'>" +
            "<div class='slide-in'>" +
                "<h2>" + title + "</h2>" +
                "<p>Function coming soon.</p>" +
            "</div>" +
        "</div>";
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
            container.innerHTML = data;

            // Attach PDF listeners after content is loaded
            attachPdfListeners(container);
            
            // Make the loaded tables sortable
            container.querySelectorAll("table[data-sortable='true']").forEach(table => {
                makeTableSortable(table);
            });            
        });
}

<!--studentViewResult.jsp-->
// Function to attach PDF download buttons
function attachPdfListeners(container) {
const assessmentBtn = container.querySelector('#downloadAssessmentPdf');
    if (assessmentBtn) {
        assessmentBtn.addEventListener('click', () => {

            const { jsPDF } = window.jspdf;
            const doc = new jsPDF('p', 'mm', 'a4');

            //Bold title
            doc.setFont("helvetica", "bold");
            doc.setFontSize(16);
            doc.text('Assessment Results', 105, 15, { align: 'center' });

            //Reset font
            doc.setFont("helvetica", "normal");
            doc.setFontSize(10);

            doc.autoTable({
                html: '.assessment-table',
                startY: 25,
                theme: 'grid',
                styles: {
                    fontSize: 9,
                    cellPadding: 3
                },
                headStyles: {
                    fillColor: [52, 73, 94]
                }
            });

            doc.save('AssessmentResults.pdf');
        });
    }

    const finalBtn = container.querySelector('#downloadFinalPdf');
    if (finalBtn) {
        finalBtn.addEventListener('click', () => {

            const table = container.querySelector('.final-table');
            if (!table || table.rows.length <= 1) {
                alert("Final results are not available yet.");
                return;
            }

            const { jsPDF } = window.jspdf;
            const doc = new jsPDF('p', 'mm', 'a4');

            //Bold title
            doc.setFont("helvetica", "bold");
            doc.setFontSize(16);
            doc.text('Final Results', 105, 15, { align: 'center' });

            //Reset font
            doc.setFont("helvetica", "normal");
            doc.setFontSize(10);

            doc.autoTable({
                html: '.final-table',
                startY: 25,
                theme: 'grid',
                styles: {
                    fontSize: 10,
                    cellPadding: 4
                },
                headStyles: {
                    fillColor: [52, 73, 94]
                }
            });

            doc.save('FinalResults.pdf');
        });
    }
}

// Function to make a table sortable
function makeTableSortable(table) {
    const headers = table.querySelectorAll("th");
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

            <!--Append sorted rows-->
            rows.forEach(row => tbody.appendChild(row));
        });
    });
}

<!--studentEditProfile.jsp-->
window.addEventListener("DOMContentLoaded", () => {
    const urlParams = new URLSearchParams(window.location.search);

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
    
    window.history.replaceState({}, document.title, window.location.pathname);    
});

function togglePassword() {
    const pwd = document.getElementById("password");
    pwd.type = (pwd.type === "password") ? "text" : "password";
}

function showGlobalPopup(message, type = "warning") {
    let popup = document.querySelector(".global-popup");

    if (!popup) {
        popup = document.createElement("div");
        popup.className = "global-popup";
        document.body.appendChild(popup);
    }

    // reset the classes
    popup.className = "global-popup";

    //apply type to it
    if (type === "success") popup.classList.add("success");
    if (type === "error") popup.classList.add("error");
    
    popup.textContent = message;
    popup.classList.add("show");

    setTimeout(() => popup.classList.remove("show"), 4000);
}

</script>
   
</body>
</html>
