<%-- 
    Document   : adminManageGrading
    Created on : Dec 25, 2025, 12:42:31 AM
    Author     : User
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2 class="admin-title">Manage Grading</h2>

<div class="table-container">
    <div class="user-actions">
        <button class="table-action-btn" type="button" onclick="openAddGradeModal()">Add New Grade</button>
        <input type="text" id="searchGrade" placeholder="Search by grade or range..." onkeyup="searchGradeTable()">
    </div>

    <table class="user-table" data-sortable="true" id="gradingTable">
        <thead>
            <tr>
                <th class="sortable">Grade</th>
                <th class="sortable">Marks</th>
                <th class="sortable">Grade Point</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="g" items="${grades}">
                <tr>
                    <td>${g.grade}</td>
                    <td data-min="${g.minMark}" data-max="${g.maxMark}">${g.minMark} - ${g.maxMark}</td>
                    <td data-point="${g.gradePoint}">${g.gradePoint}</td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/AdminManageGrading" onsubmit="return prepareEditGrade(this);" style="display:inline">
                            <input type="hidden" name="action" value="editGrade">
                            <input type="hidden" name="grade" value="${g.grade}">
                            <input type="hidden" name="minMark">
                            <input type="hidden" name="maxMark">
                            <input type="hidden" name="gradePoint">

                            <button type="button" class="icon-btn" onclick="enableEditGrade(this)" title="Edit">
                                <img src="${pageContext.request.contextPath}/images/edit.png" alt="Edit">
                            </button>    
                            <button type="submit" class="icon-btn" style="display:none" title="Save">
                                <img src="${pageContext.request.contextPath}/images/save.png" alt="Save">
                            </button>                            
                        </form>

                        <form method="post" action="${pageContext.request.contextPath}/AdminManageGrading" style="display:inline" onsubmit="return confirmDeleteGrade(this)">
                            <input type="hidden" name="action" value="deleteGrade">
                            <input type="hidden" name="grade" value="${g.grade}">

                            <button type="submit" class="icon-btn" title="Delete">
                                <img src="${pageContext.request.contextPath}/images/delete.png" alt="Delete">
                            </button>                            
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<!-- Add Grade Modal -->
<div id="addGradeModal" class="modal">
    <div class="modal-content">
        <span class="close-btn" onclick="closeAddGradeModal()">&times;</span>
        <h2 class="modal-title">Add New Grade</h2>

        <form id="addGradeForm" method="post" action="${pageContext.request.contextPath}/AdminManageGrading" class="add-user-form">
            <input type="hidden" name="action" value="addGrade">

            <div class="form-group">
                <label>Grade</label>
                <input type="text" name="grade" placeholder="Enter grade (e.g. A, B+)" required>
            </div>

            <div class="form-group">
                <label>Min Mark</label>
                <input type="number" name="minMark" step="0.1" placeholder="0" min="0" required>
            </div>

            <div class="form-group">
                <label>Max Mark</label>
                <input type="number" name="maxMark" step="0.1" placeholder="100" min="0" required>
            </div>

            <div class="form-group">
                <label>Grade Point</label>
                <input type="number" name="gradePoint" step="0.1" placeholder="4.0" min="0" required>
            </div>

            <div class="modal-actions">
                <button type="submit" class="table-action-btn">Add Grade</button>
            </div>
        </form>
    </div>
</div>
