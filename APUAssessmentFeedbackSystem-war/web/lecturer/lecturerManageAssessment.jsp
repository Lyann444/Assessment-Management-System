<%-- 
    Document   : lecturerManageAssessment
    Created on : 26-Dec-2025, 20:40:18
    Author     : USER
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h2 class="al-title">Manage Assessments</h2>

<div class="user-actions">
    <select onchange="filterStatus(this.value)">
        <option value="REJECTED" ${selectedStatus eq 'REJECTED' ? 'selected' : ''}>Rejected</option>
        <option value="PENDING" ${selectedStatus eq 'PENDING' ? 'selected' : ''}>Pending</option>
        <option value="APPROVED" ${selectedStatus eq 'APPROVED' ? 'selected' : ''}>Approved</option>
    </select>
    <input type="text" id="searchAssessment" placeholder="Search by word/phrases..." onkeyup="searchAssessmentTable()">

</div>

<div class="table-container">
    <table class="user-table" id="manageAssessmentTable">
        <thead>
            <tr>
                <th>Design ID</th>
                <th>Module</th>
                <th>Assessment Design</th>
                <th>Submitted At</th>
                <th>Status</th>

                <c:if test="${selectedStatus eq 'REJECTED'}">
                    <th>Feedback</th>
                    <th>Action</th>
                    </c:if>
            </tr>
        </thead>

        <tbody>
            <c:forEach items="${designs}" var="d">
                <tr>
                    <td>${d.designID}</td>
                    <td>${d.moduleName}</td>

                    <!-- Assessment summary -->
                    <td>
                        <c:forEach items="${assessmentMap[d.designID]}" var="a">
                            ${a.assessmentType} (<fmt:formatNumber value="${a.weightage}" maxFractionDigits="0"/>%)<br/>
                        </c:forEach>
                    </td>

                    <td>${d.submittedAt}</td>

                    <td>
                        <span class="status-label ${d.status}">
                            ${d.status}
                        </span>
                    </td>

                    <c:if test="${selectedStatus eq 'REJECTED'}">
                        <td>${d.feedback}</td>

                        <td>
                            <button class="icon-btn"
                                    onclick="openEditModal('${d.designID}')">
                                <img src="${pageContext.request.contextPath}/images/edit.png">
                            </button>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<!--edit rejected assessment modal-->
<div id="editAssessmentModal" class="modal-overlay" style="display:none;">
    <div class="modal-box">

        <h3>Edit Rejected Assessment</h3>

        <form id="editAssessmentForm"
              action="${pageContext.request.contextPath}/LecturerResubmitAssessment"
              method="post"
              onsubmit="return validateEditForm();">

            <!-- hidden design ID -->
            <input type="hidden" name="designID" id="editDesignID">

            <!-- Module name (read-only) -->
            <div class="form-group">
                <label>Module</label>
                <input type="text" id="editModuleName" readonly>
            </div>

            <!-- Feedback -->
            <div class="form-group">
                <label>Academic Leader Feedback</label>
                <textarea id="editFeedback" readonly></textarea>
            </div>

            <!-- Reused assessment container -->
            <div id="editAssessmentContainer"></div>
            <br>
            <button type="button"
                    class="primary-btn secondary"
                    onclick="addAssessment(document.getElementById('editAssessmentContainer'))">
                + Add Assessment
            </button>
            <div class="form-actions">
                <button type="button"
                        class="primary-btn secondary"
                        onclick="closeEditModal()">
                    Cancel
                </button>

                <button type="submit"
                        class="primary-btn">
                    Resubmit
                </button>
            </div>
        </form>
    </div>
</div>
