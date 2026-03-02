/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MyUser;
import model.MyUserFacade;

/**
 *
 * @author User
 */
@WebServlet(name = "AdminManageUsers", urlPatterns = {"/AdminManageUsers"})
public class AdminManageUsers extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    private MyUserFacade userFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Get all users from database
        request.setAttribute("users", userFacade.findAll());
        request.setAttribute("fields", userFacade.findDistinctFields());

        // Forward to JSP
        request.getRequestDispatcher("/admin/adminManageUsers.jsp")
                .forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String targetUser = request.getParameter("targetUser");
        String newUser = request.getParameter("username");

        if ("addUser".equals(action)) {
            String result = handleAddUser(request);

            request.getSession().setAttribute("targetUser", newUser);

            if ("success".equals(result)) {
                response.sendRedirect(request.getContextPath()
                        + "/admin/adminDashboard.jsp?tab=manageUser&addedUser=true");
            } else {
                response.sendRedirect(request.getContextPath()
                        + "/admin/adminDashboard.jsp?tab=manageUser&addedUser=false&error=" + result);
            }
            return;
        } else if ("editUser".equals(action)) {
            String result = handleEditUser(request, targetUser);
            request.getSession().setAttribute("targetUser", targetUser);

            // Redirect to dashboard 
            if ("success".equals(result)) {
                response.sendRedirect(request.getContextPath()
                        + "/admin/adminDashboard.jsp?tab=manageUser&editedUser=true");
            } else {
                response.sendRedirect(request.getContextPath()
                        + "/admin/adminDashboard.jsp?tab=manageUser&editedUser=false&error=" + result);
            }
            return;
        } else if ("blockUser".equals(action)) {
            boolean success = handleBlockUser(request, targetUser);

            // store targetUser in session
            request.getSession().setAttribute("targetUser", targetUser);

            // Redirect to dashboard with a status query
            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/adminDashboard.jsp?tab=manageUser&blockUser=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/adminDashboard.jsp?tab=manageUser&blockUser=false");
            }
            return;
        }

        // fallback
        response.sendRedirect(request.getContextPath() + "/admin/adminDashboard.jsp");
    }

    // Private handlers
    private String handleAddUser(HttpServletRequest request) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phoneNumber");
        String ic = request.getParameter("icNumber");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String userType = request.getParameter("userType");
        String field = request.getParameter("field");

        // Empty check
        if (username == null || username.isEmpty()
                || password == null || password.isEmpty()
                || gender == null || gender.isEmpty()
                || phone == null || phone.isEmpty()
                || ic == null || ic.isEmpty()
                || email == null || email.isEmpty()
                || address == null || address.isEmpty()
                || userType == null || userType.isEmpty()
                || field == null || field.isEmpty()) {
            return "invalid";
        }

        // Duplicate username
        if (userFacade.find(username) != null) {
            return "username";
        }

        // Strong password
        boolean strongPassword
                = password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[^A-Za-z0-9].*");

        if (!strongPassword) {
            return "weakpassword";
        }

        // Phone
        if (!phone.matches("\\d{10,12}")) {
            return "phone";
        }

        // IC
        if (!ic.matches("\\d{12}")) {
            return "ic";
        }

        // Email
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "email";
        }

        // Persist
        MyUser user = new MyUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setGender(gender);
        user.setPhoneNumber(phone);
        user.setIcNumber(ic);
        user.setEmail(email);
        user.setAddress(address);
        user.setUserType(userType);
        user.setField(field);
        user.setAssignedAcademicLeader("NA");
        user.setStatus("Active");

        userFacade.addUser(user);
        return "success";
    }

    private String handleEditUser(HttpServletRequest request, String username) {

        MyUser user = userFacade.find(username);
        if (user == null) {
            return "notfound";
        }

        String gender = request.getParameter("gender");
        String phone = request.getParameter("phoneNumber");
        String ic = request.getParameter("icNumber");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String userType = request.getParameter("userType");
        String field = request.getParameter("field");
        String status = request.getParameter("status");

        //Empty check
        if (gender == null || gender.isEmpty()
                || phone == null || phone.isEmpty()
                || ic == null || ic.isEmpty()
                || email == null || email.isEmpty()
                || address == null || address.isEmpty()
                || userType == null || userType.isEmpty()
                || field == null || field.isEmpty()
                || status == null || status.isEmpty()) {
            return "invalid";
        }

        //Phone validation
        if (!phone.matches("\\d{10,12}")) {
            return "phone";
        }

        //IC validation
        if (!ic.matches("\\d{12}")) {
            return "ic";
        }

        //Email validation
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "email";
        }

        user.setGender(gender);
        user.setPhoneNumber(phone);
        user.setIcNumber(ic);
        user.setEmail(email);
        user.setAddress(address);
        user.setUserType(userType);
        user.setField(field);
        user.setStatus(status);

        userFacade.updateUser(user);
        return "success";
    }

    private boolean handleBlockUser(HttpServletRequest request, String username) {
        MyUser user = userFacade.find(username);
        if (user == null || "Blocked".equals(user.getStatus())) {
            return false; // already blocked or not found
        }

        try {
            userFacade.blockUser(username); // Set status = "block"
            return true; // success
        } catch (Exception e) {
            e.printStackTrace();
            return false; // failure
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
