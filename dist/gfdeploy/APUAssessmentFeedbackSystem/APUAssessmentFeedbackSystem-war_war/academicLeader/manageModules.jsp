<%-- 
    Document   : manageModules
    Created on : 20-Dec-2025, 10:21:15
    Author     : USER
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2 class="al-title">Manage Modules</h2>

<div class="user-actions">
    <input type="text"
           id="searchModule"
           placeholder="Search by word/phrases.."
           onkeyup="searchModuleTable()">

    <button type="button"
            class="clear-sort-btn"
            onclick="clearSort()">
        Clear Sort
    </button>
</div>

<div class="table-container">
<table class="user-table" id="modulesTable" data-sortable="true">
    <thead>
        <tr>
            <th class="sortable">Module ID</th>
            <th class="sortable">Module Name</th>
            <th class="sortable">Description</th>
            <th class="sortable">Lecturer</th>
            <th>Field</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
    </thead>

    <tbody>
        <c:forEach var="m" items="${modules}">
            <tr>
                <td>${m.moduleID}</td>
                <td>${m.moduleName}</td>
                <td>${m.moduleDescription}</td>
                <td>${m.assignedLecturerUsername}</td>
                <td>${m.field}</td>
                <td>${m.status}</td>

                <td>
                    <!-- EDIT -->
                    <form method="post"
                          action="${pageContext.request.contextPath}/ALEditModule"
                          onsubmit="return prepareModuleEdit(this)"
                          style="display:inline">

                        <input type="hidden" name="moduleID" value="${m.moduleID}">
                        <input type="hidden" name="moduleName">
                        <input type="hidden" name="moduleDescription">
                        <input type="hidden" name="assignedLecturerUsername">

                        <button type="button"
                                class="icon-btn"
                                onclick="enableModuleEdit(this)">
                            <img src="${pageContext.request.contextPath}/images/edit.png">
                        </button>

                        <button type="submit"
                                class="icon-btn"
                                style="display:none">
                            <img src="${pageContext.request.contextPath}/images/save.png">
                        </button>
                    </form>

                    <!-- DISABLE -->
                    <form method="post"
                          action="${pageContext.request.contextPath}/ALTerminateModule"
                          onsubmit="return confirm('Disable this module?')"
                          style="display:inline">

                        <input type="hidden" name="moduleID" value="${m.moduleID}">

                        <button type="submit"
                                class="icon-btn ${m.status eq 'Disable' ? 'btn-disabled' : ''}"
                                ${m.status eq 'Disable' ? 'disabled' : ''}>
                            <img src="${pageContext.request.contextPath}/images/delete.png">
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
</div>