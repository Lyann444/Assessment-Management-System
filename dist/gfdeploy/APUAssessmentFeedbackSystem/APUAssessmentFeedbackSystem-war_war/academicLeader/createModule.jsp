<%-- 
    Document   : createModule panel
    Created on : 20-Dec-2025, 10:21:11
    Author     : USER
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="panel">

    <!-- SUCCESS -->
    <c:if test="${sessionScope.success == 'created'}">
        <div class="global-popup success show" id="moduleSuccess">
            Module created successfully.
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <c:if test="${sessionScope.error == 'duplicateName'}">
        <div class="global-popup error show" id="moduleError">
            Module name already exists.
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <h2>Create Module</h2>

    <form action="${pageContext.request.contextPath}/ALCreateModule" method="POST">

        <div class="form-group">
            <label>Module Name</label>
            <input type="text" name="moduleName" required>
        </div>

        <div class="form-group">
            <label>Module Description</label>
            <textarea name="moduleDescription"
                      rows="4"
                      placeholder="Brief description of the module"
                      required></textarea>
        </div>
        
        <div class="form-group">
            <label>Assign Lecturer</label>
            <select name="assignedLecturerUsername" required>
                <option value="">-- Select Lecturer --</option>

                <c:forEach var="lec" items="${lecturers}">
                    <option value="${lec.username}">
                        ${lec.username}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-actions">
            <button type="submit">Create Module</button>
        </div>

    </form>
</div>


<script>
    setTimeout(() => {
        const popup = document.getElementById("moduleSuccess");
        if (popup)
            popup.classList.remove("show");
    }, 4000);

    setTimeout(() => {
        const err = document.getElementById("moduleError");
        if (err)
            err.classList.remove("show");
    }, 4000);
</script>