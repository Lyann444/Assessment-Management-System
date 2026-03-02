<%-- 
    Document   : adminReports
    Created on : Dec 28, 2025, 12:45:26 AM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<h2 class="admin-title">Analysis Reports</h2>

<input type="hidden" id="data-userType" value='${userTypeData}'>

<input type="hidden" id="data-status" value='${statusData}'>

<input type="hidden" id="data-gender" value='${genderData}'>

<input type="hidden" id="data-fieldLabels" value='${fieldLabels}'>
<input type="hidden" id="data-role-students" value='${dataStudents}'>
<input type="hidden" id="data-role-lecturers" value='${dataLecturers}'>
<input type="hidden" id="data-role-admins" value='${dataAdmins}'>
<input type="hidden" id="data-role-leaders" value='${dataLeaders}'>

<input type="hidden" id="data-leaderLabels" value='${leaderLabels}'>
<input type="hidden" id="data-leaderData" value='${leaderData}'>

<div class="reports-grid">
    <div class="report-card">
        <h3>Users by Role</h3>
        <div class="chart-container">
            <canvas id="chartUsersByType"></canvas>
        </div>
    </div>

    <div class="report-card">
        <h3>Account Status Ratio</h3>
        <div class="chart-container">
            <canvas id="chartStatus"></canvas>
        </div>
    </div>

    <div class="report-card">
        <h3>Gender Distribution</h3>
        <div class="chart-container">
            <canvas id="chartGender"></canvas>
        </div>
    </div>

    <div class="report-card">
        <h3>Users by Field of Study</h3>
        <div class="chart-container">
            <canvas id="chartStudyField"></canvas>
        </div>
    </div>

    <div class="report-card">
        <h3>Lecturers per Academic Leader</h3>
        <div class="chart-container">
            <canvas id="chartWorkload"></canvas>
        </div>
    </div>
</div>