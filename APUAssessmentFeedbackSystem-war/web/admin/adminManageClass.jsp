<%-- 
    Document   : adminManageClass
    Created on : Dec 27, 2025, 9:05:28 PM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2 class="admin-title">Manage Class</h2>

<div class="assign-container">
    <form method="post" action="${pageContext.request.contextPath}/AdminManageClass">
        <input type="hidden" name="action" value="addClass">
        
        <div class="form-group">
            <label>Class Name</label>
            <input type="text" name="className" placeholder="e.g. UC1F2304IT" required>
        </div>

        <div class="form-group">
            <label>Module ID</label>
            <select name="moduleID" onchange="loadModuleDetails(this.value)" required>
                <option value="" hidden>Select Module</option>
                <c:forEach var="m" items="${moduleList}">
                    <option value="${m.moduleID}">${m.moduleID} - ${m.moduleName}</option>
                </c:forEach>
            </select>
        </div>

        <div id="moduleDetailsContainer">
            <div class="form-group">
                <label>Field</label>
                <p style="padding: 10px; background: #eee; border-radius: 5px; color: #888;">Select a module to view field</p>
            </div>
            
            <div class="form-group">
                <label>Students (Hold Ctrl to select multiple)</label>
                <select multiple disabled style="height: 100px;"><option>Select a module first</option></select>
            </div>
        </div>

        <div class="form-btn-container">
            <button type="submit" class="table-action-btn">Create & Register</button>
        </div>
    </form>
</div>

<hr>

<div class="table-container">
    <table class="user-table" data-sortable="true" id="classTable">
        <thead>
            <tr>
                <th class="sortable">Class ID</th>
                <th class="sortable">Class Name</th>
                <th class="sortable">Module ID</th>
                <th class="sortable">Field</th>
                <th>Registered Students</th>
                <th>Actions</th>
            </tr>
        </thead>
            <tbody>
                <c:forEach var="c" items="${classList}">
                    <tr>
                        <td>${c.classID}</td>
                        <td>${c.className}</td>
                        <td>${c.moduleID}</td>
                        <td>${c.field}</td>

                        <td>
                            <c:set var="found" value="false" />
                            <c:forEach var="reg" items="${registrations}">
                                <c:if test="${reg.classID == c.classID}">
                                    ${reg.studentName}<br/>
                                    <c:set var="found" value="true" />
                                </c:if>
                            </c:forEach>
                            <c:if test="${!found}">
                                <span style="color: #999; font-style: italic;">No students</span>
                            </c:if>
                        </td>

                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/AdminManageClass" onsubmit="return confirmRemoveClass(this)">
                                <input type="hidden" name="action" value="deleteClass">
                                <input type="hidden" name="classID" value="${c.classID}">
                                <button type="submit" class="icon-btn"> <img src="${pageContext.request.contextPath}/images/delete.png"></button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
    </table>
</div>