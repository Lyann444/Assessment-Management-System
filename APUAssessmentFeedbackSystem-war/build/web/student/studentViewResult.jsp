<%-- 
    Document   : studentViewResult
    Created on : Dec 20, 2025, 12:05:53 AM
    Author     : User
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h2 class="student-title">Assessment Results</h2>

<table class="result-table assessment-table" data-sortable="true">
    <thead>
        <tr>
            <th class="sortable">Module</th>
            <th class="sortable">Assessment</th>
            <th class="sortable">Marks (%)</th>
            <th class="sortable">Feedback</th>
        </tr>
    </thead>

    <tbody>
        <c:forEach var="row" items="${assessmentData}">
            <tr>
                <td><c:out value="${row.moduleName}"/></td>
                <td><c:out value="${row.assessmentType}"/></td>
                <td>
                    <fmt:formatNumber value="${row.marks}" maxFractionDigits="0"/>
                    /
                    <fmt:formatNumber value="${row.weightage}" maxFractionDigits="0"/>
                </td>
                <td><c:out value="${row.feedback}"/></td>            
            </tr>
        </c:forEach>
    </tbody>
</table>

<div class="result-actions">
    <button id="downloadAssessmentPdf" class="pdf-btn">
        Download Assessment Result (PDF)
    </button>
</div>

<hr style="margin: 30px 0; border-color: #ccc;">

<h2 class="student-title">Final Results</h2>

<table class="result-table final-table" data-sortable="true">
    <thead>
        <tr>
            <th class="sortable">Module</th>
            <th class="sortable">Total Marks (%)</th>
            <th class="sortable">Final Grade</th>
        </tr>
    </thead>

    <tbody>
        <c:choose>
            <c:when test="${not empty finalData}">
                <c:forEach var="module" items="${finalData}">
                    <tr>
                        <td><c:out value="${module.moduleName}"/></td>
                        <td><c:out value="${module.totalMarks}"/></td>
                        <td>
                            <c:set var="finalGradeChar" value="${module.finalGrade.substring(0,1)}" />
                            <span class="grade-${finalGradeChar}">
                                <c:out value="${module.finalGrade}"/>
                            </span>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="3" style="text-align:center;">Results not out (Pending assessments)</td>
                </tr>
            </c:otherwise>
        </c:choose>    
    </tbody>
</table>

<div class="result-actions">
    <c:choose>
        <c:when test="${not empty finalData}">
            <button id="downloadFinalPdf" class="pdf-btn">
                Download Final Result (PDF)
            </button>
        </c:when>
        <c:otherwise>
            <button class="pdf-btn" disabled>
                Final Results Not Available
            </button>
        </c:otherwise>
    </c:choose>
</div>