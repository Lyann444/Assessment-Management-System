<%-- 
    Document   : academicLeaderDasboard
    Created on : 19-Dec-2025, 09:29:37
    Author     : USER
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/academicLeader.css">

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
                    <div class="tab" onclick="loadPage('${pageContext.request.contextPath}/ALCreateModuleForm'); setActiveTab(this);">
                        Create Module
                    </div>
                    <div class="tab" onclick="loadPage('${pageContext.request.contextPath}/ALManageModules'); setActiveTab(this);">
                        Manage Modules
                    </div>
                    <div class="tab" onclick="loadPage('${pageContext.request.contextPath}/ALAssessmentReview'); setActiveTab(this);">
                        Assessment Review
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
                    <p>Welcome to Academic Leader Dashboard</p>
                </div>
            </div>

        </div>
    </div>
</div>

<jsp:include page="/general/footer.jsp" />

<script>
//to avoid editing any other row when editing current row (manage module)
let editingRow = null;

function setActiveTab(tab) {
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    tab.classList.add('active');
}

function loadPage(page) {
    fetch(page)
        .then(response => response.text())
        .then(data => {
            document.getElementById("dynamicContent").innerHTML = data;

            //after the html was injected then do this
            if (page.includes("ALManageModules")) {
                fetch("${pageContext.request.contextPath}/ALManageModulesLecturers")
                    .then(res => res.json())
                    .then(list => {
                        window.availableLecturers = list;
                    });
            }
        });
}


document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    const tab = params.get("tab");

    if (tab === "createModule") {
        loadPage("${pageContext.request.contextPath}/ALCreateModuleForm");
        document.querySelectorAll(".tab")[0].classList.add("active");
    }

    if (tab === "manageModules") {
        loadPage("${pageContext.request.contextPath}/ALManageModules");
        document.querySelectorAll(".tab")[1].classList.add("active");
    }
    if (tab === "assessmentReview") {
        loadPage("${pageContext.request.contextPath}/ALAssessmentReview?status=PENDING");
        document.querySelectorAll(".tab")[2].classList.add("active");
    }
    
    // --- Edit Profile Alerts ---
    if (params.get("profileUpdated") === "true") {
        showGlobalPopup("Profile updated successfully!", "success");
    }

    if (params.get("profileUpdated") === "false") {
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
        } else {
            showGlobalPopup("Failed to update profile. Please try again.", "error");
        }
    }

    // Clean URL once popup is shown
    if (params.get("profileUpdated")) {
        setTimeout(() => {
            window.history.replaceState({}, document.title, window.location.pathname);
        }, 200);
    }
});

document.addEventListener("click", function (event) {
    const modal = document.getElementById("editProfileModal");
    if (!modal) return;

    if (event.target === modal) {
        modal.classList.remove("show");
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
    if (type === "success") popup.classList.add("success");
    if (type === "error") popup.classList.add("error");

    popup.textContent = message;
    popup.classList.add("show");

    setTimeout(() => popup.classList.remove("show"), 4000);
}



//run after any dynamic content load
const originalLoadPage = loadPage;
loadPage = function(page) {
    originalLoadPage(page);

    setTimeout(() => {
        // rebind in back the sorting to avoid no more sorting after first load
        document
            .querySelectorAll("table[data-sortable='true']")
            .forEach(table => {
                table.dataset.sorted = "false";
                makeTableSortable(table);
                sortByStatus(table);  
            });

        //show popup after content loads
        const params = new URLSearchParams(window.location.search);

        let popupShown = false;
        
        //for create modules
        if (params.get("success") === "created") {
            showGlobalPopup("Module created successfully!", "success");
            popupShown = true;
        }
        
        //for manage modules
        if (params.get("success") === "updated") {
            showGlobalPopup("Module updated successfully","success");
            popupShown = true;
        }

        if (params.get("error") === "duplicate") {
            showGlobalPopup("Module name already exists. Please try again", "error");
            popupShown = true;
        }
        
        //for assessment review
        if (params.get("success") === "approved") {
            showGlobalPopup("Assessment approved successfully", "success");
            popupShown = true;
        }

        if (params.get("success") === "rejected") {
            showGlobalPopup("Assessment rejected", "error");
            popupShown = true;
        }
        
        if (popupShown || params.get("tab")) {
            window.history.replaceState({}, document.title, window.location.pathname);
        }
    }, 200);
};
// == for managing modules
function searchModuleTable() {
    const input = document.getElementById("searchModule");
    const filter = input.value.toLowerCase();
    const table = document.getElementById("modulesTable");
    const rows = table.getElementsByTagName("tr");

    for (let i = 1; i < rows.length; i++) {
        let rowText = rows[i].innerText.toLowerCase();
        rows[i].style.display = rowText.includes(filter) ? "" : "none";
    }
}

function enableModuleEdit(button) {
    const row = button.closest("tr");
    
    //check if other row is also editing or not first (to avoid multiple row in edit state
    if (editingRow && editingRow !== row) {
        showGlobalPopup("Please save the current edit first.");
        return;
    }

    if (row.classList.contains("editing")) return;

    editingRow = row;

    const cells = row.querySelectorAll("td");
    const form = button.closest("form");

    const moduleName = cells[1].textContent.trim();
    const description = cells[2].textContent.trim();
    const lecturer = cells[3].textContent.trim();
    const status = cells[5].textContent.trim();

    row.classList.add("editing");

    //module name
    const nameArea = document.createElement("textarea");
    nameArea.className = "edit-field edit-textarea name-area";
    nameArea.value = moduleName;
    cells[1].innerHTML = "";
    cells[1].appendChild(nameArea);

    //desc
    const descArea = document.createElement("textarea");
    descArea.className = "edit-field edit-textarea desc-area";
    descArea.value = description;
    cells[2].innerHTML = "";
    cells[2].appendChild(descArea);

    //lecturer dropdown
    const lecSelect = document.createElement("select");
    lecSelect.className = "edit-field lecturer-select";
    cells[3].innerHTML = "";
    cells[3].appendChild(lecSelect);
    populateLecturerDropdown(lecSelect, lecturer);

    //status dropdown
    const statusSelect = document.createElement("select");
    statusSelect.className = "edit-field status-select";
    statusSelect.innerHTML = `
        <option value="Active">Active</option>
        <option value="Disable">Disable</option>
    `;
    statusSelect.value = status;
    cells[5].innerHTML = "";
    cells[5].appendChild(statusSelect);

    //toggle of button
    const saveBtn = form.querySelector("button[style*='display:none']");
    button.style.display = "none";
    saveBtn.style.display = "inline-flex";

    //disable/enable buton
    const deleteBtn = row.querySelector(
        "form[action*='ALTerminateModule'] button"
    );

    statusSelect.addEventListener("change", () => {
        if (statusSelect.value === "Active") {
            deleteBtn.disabled = false;
            deleteBtn.classList.remove("btn-disabled");
        } else {
            deleteBtn.disabled = true;
            deleteBtn.classList.add("btn-disabled");
        }
    });
}

function populateLecturerDropdown(select, selected) {
    if (!window.availableLecturers || window.availableLecturers.length === 0) {
        select.innerHTML = `<option value="">No lecturers available</option>`;
        return;
    }

    select.innerHTML = "";

    window.availableLecturers.forEach(l => {
        const opt = document.createElement("option");
        opt.value = l;
        opt.textContent = l;
        if (l === selected) opt.selected = true;
        select.appendChild(opt);
    });
}


function prepareModuleEdit(form) {
    const row = form.closest("tr");
    const fields = row.querySelectorAll(".edit-field");

    const name = fields[0].value.trim();
    const desc = fields[1].value.trim();
    const lec  = fields[2].value.trim();
    const status = fields[3].value.trim();

    if (!name || !desc || !lec || !status) {
        showGlobalPopup("All fields must be filled!");
        return false;
    }

    form.moduleName.value = name;
    form.moduleDescription.value = desc;
    form.assignedLecturerUsername.value = lec;

    // add hidden status input
    if (!form.status) {
        const s = document.createElement("input");
        s.type = "hidden";
        s.name = "status";
        form.appendChild(s);
    }
    form.status.value = status;

    editingRow = null;
    row.classList.remove("editing");
    const table = document.getElementById("modulesTable");
    if (table) sortByStatus(table);
    
    return true;
}

function getCellValue(cell) {
    //if the cell contains a select (in edit mode)
    const select = cell.querySelector("select");
    if (select) return select.value.toLowerCase();

    //otherwise if is normal text
    return cell.textContent.trim().toLowerCase();
}

function makeTableSortable(table) {
    if (table.dataset.sorted === "true") return;
    table.dataset.sorted = "true";

    const headers = table.querySelectorAll("th.sortable");

    headers.forEach((header, index) => {
        header.addEventListener("click", () => {
            const tbody = table.querySelector("tbody");
            const rows = Array.from(tbody.querySelectorAll("tr"));
            const asc = header.classList.contains("desc") || !header.classList.contains("asc");

            headers.forEach(h => h.classList.remove("asc", "desc"));
            header.classList.add(asc ? "asc" : "desc");

            rows.sort((a, b) => {
                const aText = getCellValue(a.cells[index]);
                const bText = getCellValue(b.cells[index]);

                return asc
                    ? aText.localeCompare(bText)
                    : bText.localeCompare(aText);
            });

            rows.forEach(row => tbody.appendChild(row));
        });
    });
}

//for editing lecturer drop down
window.availableLecturers = [];

//display all active module before disable module (manage modules)
function sortByStatus(table) {
    const tbody = table.querySelector("tbody");
    const rows = Array.from(tbody.querySelectorAll("tr"));

    rows.sort((a, b) => {
        const statusA = a.cells[5].textContent.trim();
        const statusB = b.cells[5].textContent.trim();

        if (statusA === statusB) return 0;
        if (statusA === "Disable") return 1; 
        if (statusB === "Disable") return -1;
        return 0;
    });

    rows.forEach(row => tbody.appendChild(row));
}

//clear sort function (logic - goes based on status first > then based on moduleID
function resetTableToDefaultOrder(table) {
    const tbody = table.querySelector("tbody");
    const rows = Array.from(tbody.querySelectorAll("tr"));

    rows.sort((a, b) => {
        const statusA = a.cells[5].innerText.trim();
        const statusB = b.cells[5].innerText.trim();

        //priority of status
        if (statusA !== statusB) {
            return statusA === "Active" ? -1 : 1;
        }

        //under same status, reset by module name
        const idA = a.cells[0].innerText.trim();
        const idB = b.cells[0].innerText.trim();
        return idA.localeCompare(idB);
    });

    rows.forEach(row => tbody.appendChild(row));

    // Clear sort arrows
    table.querySelectorAll("th.sortable").forEach(th =>
        th.classList.remove("asc", "desc")
    );
}

function clearSort() {
    const table = document.getElementById("modulesTable");
    resetTableToDefaultOrder(table);
}


//AL Assessment Review
//for filtering by the dropdown (seeing assessment status)
const contextPath = "${pageContext.request.contextPath}";
function filterByStatus() {
    const status = document.getElementById("statusFilter").value;
    loadPage(contextPath + "/ALAssessmentReview?status=" + status);
}

//search function - assessment review AL
function searchAssessmentTable() {
    const input = document.getElementById("searchAssessment").value.toLowerCase();
    const rows = document.querySelectorAll("#assessmentTable tbody tr");

    rows.forEach(row => {
        row.style.display =
            row.innerText.toLowerCase().includes(input)
                ? ""
                : "none";
    });
}

function approveDesign(designID) {
    if (!confirm("Approve this assessment design?")) return;

    const form = document.createElement("form");
    form.method = "post";
    form.action = contextPath + "/ALApproveAssessment";

    const input = document.createElement("input");
    input.type = "hidden";
    input.name = "designID";
    input.value = designID;

    form.appendChild(input);
    document.body.appendChild(form);
    form.submit();
}

let currentRejectDesignID = null;

function openRejectModal(designID) {
    const hiddenInput = document.getElementById("rejectDesignID");

    if (!hiddenInput) {
        console.error("rejectDesignID input not found in DOM");
        showGlobalPopup("Internal error: modal not loaded", "error");
        return;
    }

    hiddenInput.value = designID;
    document.getElementById("rejectFeedback").value = "";
    document.getElementById("rejectModal").classList.add("show");
}

function closeRejectModal() {
    document.getElementById("rejectModal").classList.remove("show");
}

function submitReject() {
    const designInput = document.getElementById("rejectDesignID");
    const feedbackInput = document.getElementById("rejectFeedback");

    if (!designInput || !feedbackInput) {
        showGlobalPopup("Modal not loaded properly.", "error");
        return;
    }

    const designID = designInput.value;
    const feedback = feedbackInput.value.trim();

    if (!designID) {
        showGlobalPopup("Invalid assessment selected.", "error");
        return;
    }

    if (!feedback) {
        showGlobalPopup("Feedback is required.", "error");
        return;
    }

    fetch(contextPath + "/ALRejectAssessment", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
            designID: designID,
            feedback: feedback
        })
    })
    .then(res => {
        if (!res.ok) throw new Error();
        closeRejectModal();
        showGlobalPopup("Assessment rejected", "error");
        loadPage(contextPath + "/ALAssessmentReview?status=PENDING");
    })
    .catch(() => {
        showGlobalPopup("Reject failed.", "error");
    });
}



</script>

</body>
</html>
