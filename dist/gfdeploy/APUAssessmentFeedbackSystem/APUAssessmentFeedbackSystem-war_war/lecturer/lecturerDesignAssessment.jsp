<%-- 
    Document   : lecturerDesignAssessment
    Created on : 25-Dec-2025, 15:51:25
    Author     : USER
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="panel">
    <h2>Design Assessment</h2>
    <c:choose>
        <c:when test="${empty lecturerModules}">
            <h4 class="empty-message">
                No modules available for assessment design.
            </h4>
        </c:when>

        <c:otherwise>
            <form id="assessmentForm"
                  action="${pageContext.request.contextPath}/LecturerSubmitAssessmentDesign"
                  method="post"
                  onsubmit="return validateForm();">

                <!-- Module Drop down -->
                <div class="form-group">
                    <br>
                    <label>Module</label>
                    <select name="moduleName" id="moduleSelect" required>
                        <option value="">-- Select Module --</option>
                        <c:forEach items="${lecturerModules}" var="m">
                            <option value="${m.moduleName}" data-id="${m.moduleID}">
                                ${m.moduleID} - ${m.moduleName}
                            </option>
                        </c:forEach>
                    </select>
                    <input type="hidden" name="moduleID" id="moduleID">
                </div>

                <!-- Weightage Bar -->
                <div class="weightage-wrapper">
                    <div class="weightage-text">
                        Total Weightage: <span id="weightTotal">0</span>%
                    </div>
                    <div class="weightage-bar-bg">
                        <div id="weightBar" class="weightage-bar"></div>
                    </div>
                </div>

                <!-- Assessment Container -->
                <div id="assessmentContainer"></div>

                <div class="form-actions">
                    <button type="button" class="primary-btn secondary" id="addBtn" onclick="addAssessment()">+ Add Assessment</button>
                    <button type="submit" class="primary-btn">Submit Design</button>
                </div>
            </form>
        </c:otherwise>
    </c:choose>
</div>
