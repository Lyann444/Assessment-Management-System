<%-- 
    Document   : manageModules
    Created on : 20-Dec-2025, 10:21:31
    Author     : USER
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h2 class="al-title">Assessment Review</h2>

<div class="user-actions">
    <select id="statusFilter" onchange="filterByStatus(this.value)">
        <option value="PENDING" ${selectedStatus=='PENDING'?'selected':''}>Pending</option>
        <option value="APPROVED" ${selectedStatus=='APPROVED'?'selected':''}>Approved</option>
        <option value="REJECTED" ${selectedStatus=='REJECTED'?'selected':''}>Rejected</option>
    </select>

    <input type="text"
           id="searchAssessment"
           placeholder="Search by word/phrases.."
           onkeyup="searchAssessmentTable()">
</div>

<div class="table-container">
<table class="user-table" id="assessmentTable">
    <thead>
        <tr>
            <th>Module</th>
            <th>Lecturer</th>
            <th>Assessment Design</th>
            <th>Status</th>
            <c:if test="${selectedStatus eq 'PENDING'}">
                <th>Actions</th>
            </c:if>
            <c:if test="${selectedStatus eq 'REJECTED'}">
                <th>Feedback</th>
            </c:if>
        </tr>
    </thead>

    <tbody>
        <c:forEach var="d" items="${designs}">
            <tr>
                <td>${d.moduleName}</td>
                <td>${d.lecturerUsername}</td>

                <td>
                    <c:forEach var="a" items="${assessmentMap[d.designID]}">
                        ${a.assessmentType} (<fmt:formatNumber value="${a.weightage}" maxFractionDigits="0"/>%)<br/>
                    </c:forEach>
                </td>

                <td>
                    <span class="status-label ${d.status}">
                        ${d.status}
                    </span>
                </td>

                <c:if test="${selectedStatus eq 'PENDING'}">
                    <td>
                        <button class="icon-btn approve"
                                title="Approve assessment"
                                onclick="approveDesign('${d.designID}')">
                            <img src="${pageContext.request.contextPath}/images/approve.png" alt="Approve">
                        </button>

                        <button class="icon-btn reject"
                                title="Reject assessment"
                                onclick="openRejectModal('${d.designID}')">
                            <img src="${pageContext.request.contextPath}/images/reject.png" alt="Reject">
                        </button>
                    </td>
                </c:if>

                <c:if test="${selectedStatus eq 'REJECTED'}">
                    <td>${d.feedback}</td>
                </c:if>
            </tr>
        </c:forEach>
    </tbody>
</table>
</div>
    
<!-- for reject feedback pop up -->
<div id="rejectModal" class="modal">
    <div class="modal-content">
        <h3>Reject Assessment</h3>

        <input type="hidden" id="rejectDesignID">
        
        <textarea id="rejectFeedback"
                  placeholder="Enter rejection feedback..."
                  rows="4"></textarea>

        <div class="modal-actions">
            <button onclick="submitReject()">Reject</button>
            <button onclick="closeRejectModal()">Cancel</button>
        </div>
    </div>
</div>