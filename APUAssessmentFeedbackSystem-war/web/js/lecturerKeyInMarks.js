function loadAssessments(classID) {

    if (!classID)
        return;

    fetch(contextPath + "/LecturerLoadApprovedAssessments?classID=" + classID)
            .then(res => res.json())
            .then(data => {

                const select = document.getElementById("assessmentSelect");
                select.innerHTML =
                        "<option value=''>-- Select Assessment --</option>";

                data.forEach(a => {
                    select.innerHTML += `
                    <option value="${a.assessmentID}"
                            data-max="${a.weightage}">
                        ${a.assessmentType} (${a.weightage}%)
                    </option>`;
                });
            })
            .catch(err => console.error(err));
}

function loadStudents() {

    const classID =
            document.getElementById("classSelect").value;

    const assessmentSelect =
            document.getElementById("assessmentSelect");

    const assessmentID = assessmentSelect.value;

    if (!classID || !assessmentID)
        return;

    const maxMarks =
            assessmentSelect.selectedOptions[0]
            .getAttribute("data-max");

    fetch(contextPath +
            "/LecturerLoadStudentsForMarking" +
            "?classID=" + classID +
            "&assessmentID=" + assessmentID)
            .then(res => res.json())
            .then(data => renderStudentTable(data, assessmentID, maxMarks));
    
}

function renderStudentTable(students, assessmentID, maxMarks) {

    let html = `
        <form method="post"
              action="${contextPath}/LecturerSubmitMarks"
              onsubmit="return validateMarks(${maxMarks});">

        <input type="hidden"
               name="assessmentID"
               value="${assessmentID}">

        <table class="user-table">
            <thead>
                <tr>
                    <th>Student</th>
                    <th>Marks (0–${maxMarks})</th>
                    <th>Feedback</th>
                </tr>
            </thead>
            <tbody>
    `;

    students.forEach(s => {
        html += `
            <tr>
                <td>${s.studentName}</td>
                <td>
                    <input type="number"
                           name="marks_${s.studentName}"
                           value="${s.marks ?? ''}"
                           min="0"
                           max="${maxMarks}"
                           step="0.01"
                           required>
                </td>
                <td>
                    <textarea name="feedback_${s.studentName}"
                              rows="2"
                              placeholder="Optional feedback">${s.feedback ?? ""}</textarea>
                </td>
            </tr>
        `;
    });

    html += `
            </tbody>
        </table>

        <div class="form-actions">
            <button type="submit" class="primary-btn">
                Save Marks
            </button>
        </div>
        </form>
    `;

    document.getElementById("studentMarksSection").innerHTML = html;
}

function validateMarks(maxMarks) {

    let valid = true;

    document.querySelectorAll("input[type='number']")
            .forEach(i => {
                const value = Number(i.value);
                if (value < 0 || value > maxMarks) {
                    valid = false;
                }
            });

    if (!valid) {
        showGlobalPopup(
                "Marks must be between 0 and " + maxMarks,
                "error"
                );
    }

    return valid;
}
