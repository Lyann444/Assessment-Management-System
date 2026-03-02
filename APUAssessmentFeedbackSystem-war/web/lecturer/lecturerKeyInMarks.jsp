<%-- 
    Document   : lecturerKeyInMarks
    Created on : 26-Dec-2025, 12:26:49
    Author     : USER
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2 class="al-title">Key In Marks</h2>

<div class="form-group">
    <label>Select Class</label>
    <select id="classSelect" onchange="loadAssessments(this.value)">
        <option value="">-- Select Class --</option>
        <c:forEach items="${classes}" var="c">
            <option value="${c.classID}">
                ${c.className}
            </option>
        </c:forEach>
    </select>
       
    <label><br> Assessment</label>
    <select id="assessmentSelect"
            onchange="loadStudents()">
        <option value="">-- Select Assessment --</option>
    </select>
    <div><br></div>
    <div id="studentMarksSection"></div>
</div>

<div id="assessmentSection"></div>
<div id="studentMarksSection"></div>
