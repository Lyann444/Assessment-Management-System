function searchAssessmentTable() {
    const input = document.getElementById("searchAssessment").value.toLowerCase();
    const rows = document.querySelectorAll(".user-table tbody tr");

    rows.forEach(row => {
        row.style.display =
            row.innerText.toLowerCase().includes(input)
                ? ""
                : "none";
    });
}

function openEditModal(designID) {
    fetch(contextPath + "/LecturerLoadAssessment?designID=" + designID)
        .then(res => res.json())
        .then(data => {

            document.getElementById("editDesignID").value = designID;
            document.getElementById("editModuleName").value = data.moduleName;
            document.getElementById("editFeedback").value = data.feedback || "-";

            assessmentCount = 0;
            const container =
                document.getElementById("editAssessmentContainer");
            container.innerHTML = "";

            data.assessments.forEach(a => {
                addAssessment(container);   
                const box = container.lastElementChild;

                box.querySelector("select").value = a.assessmentType;
                box.querySelector("input").value = a.weightage;
            });

            document.getElementById("editAssessmentModal").style.display = "flex";
        });
}


function closeEditModal() {
    document.getElementById("editAssessmentModal").style.display = "none";
    document.getElementById("editAssessmentContainer").innerHTML = "";
    assessmentCount = 0;
}