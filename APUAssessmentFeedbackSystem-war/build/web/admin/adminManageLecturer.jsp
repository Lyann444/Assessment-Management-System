<%-- 
    Document   : adminManageLecturer
    Created on : Dec 26, 2025, 11:01:50 PM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2 class="admin-title">Manage Lecturer</h2>

<form method="post"
      action="${pageContext.request.contextPath}/AdminManageLecturer"
      class="assign-form">

    <input type="hidden" name="action" value="addLecturerAssign">
    
    <div class="assign-container">
        <!-- Lecturer dropdown -->
        <div class="form-group">
            <label>Lecturer</label>
            <select name="lecturer" required>
                <option value="" hidden>Select Lecturer</option>
                <c:forEach var="l" items="${lecturers}">
                    <option value="${l.username}">
                        ${l.username}
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- Academic Leader dropdown -->
        <div class="form-group">
            <label>Academic Leader</label>
            <select name="academicLeader" required>
                <option value="" hidden>Select Academic Leader</option>
                <c:forEach var="a" items="${academicLeaders}">
                    <option value="${a.username}">
                        ${a.username}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-btn-container">        
            <button type="submit" class="table-action-btn">
                Assign
            </button>
        </div>
    </div>    
</form>

<hr>

<div class="table-container">
    <table class="user-table" data-sortable="true" id="lecturerTable">
        <thead>
            <tr>
                <th class="sortable">Lecturer</th>
                <th class="sortable">Field</th>
                <th class="sortable">Assigned Academic Leader</th>
                <th>Actions</th>                
            </tr>
        </thead>
        <tbody>
            <c:forEach var="u" items="${assignedList}">
                <tr>
                    <td>${u.username}</td>
                <!-- Lecturer Field -->
                <td>
                    <c:choose>
                        <c:when test="${empty u.field}">General</c:when>
                        <c:otherwise>${u.field}</c:otherwise>
                    </c:choose>
                </td>

                <!-- Academic Leader -->
                <td>
                    <c:choose>
                        <c:when test="${empty u.assignedAcademicLeader || u.assignedAcademicLeader == 'NA'}">
                            Not Assigned
                        </c:when>
                        <c:otherwise>${u.assignedAcademicLeader}</c:otherwise>
                    </c:choose>
                </td>
                <td>                    
                    <form method="post" action="${pageContext.request.contextPath}/AdminManageLecturer" style="display:inline" onsubmit="return confirmRemoveAssignation(this)">                        
                        <input type="hidden" name="action" value="deleteLecturerAssign">
                        <input type="hidden" name="lecturer" value="${u.username}">
                        <input type="hidden" name="field" value="General">
                        <input type="hidden" name="academicLeader" value="NA">
                        
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
