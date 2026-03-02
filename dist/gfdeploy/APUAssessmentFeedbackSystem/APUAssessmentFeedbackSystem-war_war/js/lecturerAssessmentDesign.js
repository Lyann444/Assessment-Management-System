let assessmentCount = 0;
const MAX_ASSESSMENT = 3;

//Design Assessment
function initLecturerAssessmentDesign() {
    assessmentCount = 0;

    const container = document.getElementById("assessmentContainer");
    if (container) {
        container.innerHTML = "";
        addAssessment();
    }

    const select = document.getElementById("moduleSelect");
    const hidden = document.getElementById("moduleID");

    if (select && hidden) {
        select.onchange = function () {
            const opt = this.options[this.selectedIndex];
            hidden.value = opt.dataset.id || "";
        };
    }
}

function addAssessment(container = document.getElementById("assessmentContainer")) {
    if (!container || assessmentCount >= MAX_ASSESSMENT) return;

    assessmentCount++;

    const div = document.createElement("div");
    div.className = "assessment-box";

    div.innerHTML = `
        <h4>Assessment #${assessmentCount}</h4>

        <label>Assessment Type</label>
        <select name="assessmentType" required>
            <option value="">-- Select --</option>
            <option value="Coursework">Coursework</option>
            <option value="Test">Test</option>
            <option value="Quiz">Quiz</option>
            <option value="Final">Final</option>
        </select>

        <label>Weightage (%)</label>
        <input type="number" name="weightage" min="1" max="100"
               oninput="updateWeightage()" required />

        <button type="button"
                class="icon-btn reject remove-btn"
                onclick="removeAssessment(this)">✕</button>
        <hr>
    `;

    container.appendChild(div);
    updateRemoveButtons();
    toggleAddButton();
}

function updateRemoveButtons() {
    const buttons = document.querySelectorAll(".remove-btn");

    buttons.forEach((btn, index) => {
        btn.style.display = (buttons.length > 1 && index > 0) ? "inline-block" : "none";
    });
}

function removeAssessment(btn) {
    btn.parentElement.remove();
    assessmentCount--;
    updateWeightage();
    renumberAssessments();
    updateRemoveButtons();
    toggleAddButton();
}

function renumberAssessments() {
    const boxes = document.querySelectorAll(".assessment-box");
    boxes.forEach((box, index) => {
        box.querySelector("h4").innerText = "Assessment #" + (index + 1);
    });
}

function updateWeightage() {
    let total = 0;

    document.querySelectorAll("input[name='weightage']").forEach(i => {
        total += Number(i.value) || 0;
    });

    const totalText = document.getElementById("weightTotal");
    const bar = document.getElementById("weightBar");

    if (!totalText || !bar) return;

    totalText.innerText = total;
    bar.style.width = Math.min(total, 100) + "%";

    if (total === 100) bar.className = "weightage-bar ok";
    else if (total > 100) bar.className = "weightage-bar error";
    else bar.className = "weightage-bar warn";
}

function toggleAddButton() {
    const btn = document.getElementById("addBtn");
    if (!btn) return;

    btn.style.display =
        assessmentCount >= MAX_ASSESSMENT
            ? "none"
            : "inline-block";
}

function validateForm() {
    if (assessmentCount < 1) {
        showGlobalPopup("At least one assessment is required.", "warning");
        return false;
    }

    const types = [];
    const selects = document.querySelectorAll("select[name='assessmentType']");

    for (let sel of selects) {
        if (!sel.value) {
            showGlobalPopup("Please select all assessment types.", "warning");
            return false;
        }

        if (types.includes(sel.value)) {
            showGlobalPopup("Assessment types cannot be duplicated.", "error");
            return false;
        }

        types.push(sel.value);
    }

    let total = 0;
    document.querySelectorAll("input[name='weightage']").forEach(i => {
        total += Number(i.value) || 0;
    });

    if (total !== 100) {
        showGlobalPopup("Total weightage must be exactly 100%.", "error");
        return false;
    }

    return true;
}

document.addEventListener("DOMContentLoaded", () => {
    const select = document.getElementById("moduleSelect");
    const hidden = document.getElementById("moduleID");

    if (!select || !hidden) return;

    select.addEventListener("change", function () {
        const opt = this.options[this.selectedIndex];
        hidden.value = opt.dataset.id || "";
    });
});
