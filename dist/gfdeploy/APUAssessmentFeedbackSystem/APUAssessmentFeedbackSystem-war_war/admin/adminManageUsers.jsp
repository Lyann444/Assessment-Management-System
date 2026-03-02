<%-- 
    Document   : adminManageUsers
    Created on : Dec 21, 2025, 12:04:07 AM
    Author     : User
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2 class="admin-title">Manage Users</h2>

<div class="user-actions">
    <button class="table-action-btn" type="button" onclick="openAddUserModal()">Add New User</button>    
    <input type="text" id="searchUser" placeholder="Search by username, type, or status..." onkeyup="searchUserTable()">
</div>

<div class="table-container">
    <table class="user-table" data-sortable="true" id="usersTable">
        <thead>
            <tr>
                <th class="sortable">Username</th>
                <th class="sortable">Gender</th>
                <th class="sortable">Phone Number</th>
                <th class="sortable">IC Number</th>
                <th class="sortable">Email</th>
                <th class="sortable">Address</th>
                <th class="sortable">User Type</th>
                <th class="sortable">Field</th>
                <th class="sortable">Status</th>
                <th>Actions</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach var="u" items="${users}">
                <tr>
                    <td>${u.username}</td>
                    <td>${u.gender}</td>
                    <td>${u.phoneNumber}</td>
                    <td>${u.icNumber}</td>
                    <td>${u.email}</td>
                    <td>${u.address}</td>
                    <td>${u.userType}</td>
                    <td>${u.field}</td>
                    <td>${u.status}</td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/AdminManageUsers" onsubmit="return prepareEdit(this);" style="display:inline">
                            <input type="hidden" name="action" value="editUser">
                            <input type="hidden" name="targetUser" value="${u.username}">
                            <input type="hidden" name="gender">
                            <input type="hidden" name="phoneNumber">
                            <input type="hidden" name="icNumber">
                            <input type="hidden" name="email">
                            <input type="hidden" name="address">
                            <input type="hidden" name="userType">
                            <input type="hidden" name="field">
                            <input type="hidden" name="status">

                            <button type="button" class="icon-btn" onclick="enableEdit(this)" title="Edit">
                                <img src="${pageContext.request.contextPath}/images/edit.png" alt="Edit">
                            </button>
 
                            <button type="submit" class="icon-btn" style="display:none" title="Save">
                                <img src="${pageContext.request.contextPath}/images/save.png" alt="Save">
                            </button>                        
                        </form>

                        <form method="post" action="${pageContext.request.contextPath}/AdminManageUsers" style="display:inline" onsubmit="return confirmDelete(this)">
                            <input type="hidden" name="action" value="blockUser">
                            <input type="hidden" name="targetUser" value="${u.username}">

                            <button type="submit"
                                    class="icon-btn ${u.status eq 'Blocked' ? 'btn-disabled' : ''}"
                                    ${u.status eq 'Blocked' ? 'disabled' : ''}
                                    title="Delete">
                                <img src="${pageContext.request.contextPath}/images/delete.png" alt="Delete">
                            </button>                        
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<datalist id="fieldOptionsEdit">
    <c:forEach var="f" items="${fields}">
        <option value="${f}" />
    </c:forEach>
</datalist>

<!-- Add New User Modal -->
<div id="addUserModal" class="modal">
    <div class="modal-content">
        <span class="close-btn" onclick="closeAddUserModal()">&times;</span>
        <h2 class="modal-title">Add New User</h2>

        <form id="addUserForm" method="post" action="${pageContext.request.contextPath}/AdminManageUsers" class="add-user-form">
            <input type="hidden" name="action" value="addUser">

            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" placeholder="Enter username" required>
            </div>

            <div class="form-group">
                <label>Password</label>
                <input type="text" name="password" placeholder="Enter password" required>
            </div>

            <div class="form-group">
                <label>Gender</label>
                <select name="gender" required>
                    <option value="" hidden>Select Gender</option>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                </select>
            </div>

            <div class="form-group">
                <label>Phone Number</label>
                <input type="text" name="phoneNumber" placeholder="Enter phone number" required>
            </div>

            <div class="form-group">
                <label>IC Number</label>
                <input type="text" name="icNumber" placeholder="Enter IC number" required>
            </div>

            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" placeholder="Enter email" required>
            </div>

            <div class="form-group">
                <label>Address</label>
                <input type="text" name="address" placeholder="Enter address" required>
            </div>

            <div class="form-group">
                <label>User Type</label>
                <select name="userType" required>
                    <option value="" hidden>Select User Type</option>
                    <option value="Admin">Admin</option>
                    <option value="Academic Leader">Academic Leader</option>
                    <option value="Lecturer">Lecturer</option>
                    <option value="Student">Student</option>
                </select>
            </div>

            <div class="form-group">
                <label>Field</label>
                <input list="fieldOptionsAdd" name="field" placeholder="Enter or select field" required>
                
                <datalist id="fieldOptionsAdd">
                    <c:forEach var="f" items="${fields}">
                        <option value="${f}" />
                    </c:forEach>
                </datalist>                
            </div>

            <div class="modal-actions">
                <button type="submit" class="table-action-btn">Add User</button>
            </div>
        </form>
    </div>
</div>
