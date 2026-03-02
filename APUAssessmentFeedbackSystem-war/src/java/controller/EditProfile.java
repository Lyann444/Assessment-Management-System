/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import static java.lang.System.out;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.MyUser;
import model.MyUserFacade;

/**
 *
 * @author User
 */
@WebServlet(name = "EditProfile", urlPatterns = {"/EditProfile"})
public class EditProfile extends HttpServlet {

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
    private MyUserFacade myUserFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUsername") == null) {
            response.sendRedirect(request.getContextPath() + "/general/login.jsp");
            return;
        }

        String username = (String) session.getAttribute("loggedInUsername");
        MyUser user = myUserFacade.find(username);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/general/login.jsp?error=USER_NOT_FOUND");
            return;
        }

        String userType = (String) session.getAttribute("loggedInUserType");
        String dashboardPath = "";

        // Determine dashboard path based on user type
        if ("Student".equals(userType)) {
            dashboardPath = "student/studentDashboard.jsp";
        } else if ("Lecturer".equals(userType)) {
            dashboardPath = "lecturer/lecturerDashboard.jsp";
        } else if ("Academic Leader".equals(userType)) {
            dashboardPath = "academicLeader/academicLeaderDashboard.jsp";
        } else if ("Admin".equals(userType)) {
            dashboardPath = "admin/adminDashboard.jsp";
        } else {
            dashboardPath = "login.jsp?error=role";
        }

        // GET → load profile page with pre-filled user data
        if (request.getMethod().equalsIgnoreCase("GET")) {
            request.setAttribute("loggedInUser", user);
            request.setAttribute("dashboardPath", dashboardPath);
            request.getRequestDispatcher("general/editProfile.jsp").forward(request, response);
            return;
        }

        
        try {
            // POST → update profile
            // Update profile fields first
            String password = request.getParameter("password");
            String gender = request.getParameter("gender");
            String phone = request.getParameter("phonenum");
            String ic = request.getParameter("icnum");
            String email = request.getParameter("email");
            String address = request.getParameter("address");


            // Validation (no blanks allowed)
            // ===== VALIDATION =====
            if (!isStrongPassword(password)) {
                response.sendRedirect(request.getContextPath() + "/" + dashboardPath
                        + "?profileUpdated=false&error=weakpassword");
                return;
            }

            if (!isValidPhone(phone)) {
                response.sendRedirect(request.getContextPath() + "/" + dashboardPath
                        + "?profileUpdated=false&error=phone");
                return;
            }

            if (!isValidIC(ic)) {
                response.sendRedirect(request.getContextPath() + "/" + dashboardPath
                        + "?profileUpdated=false&error=ic");
                return;
            }

            if (!isValidEmail(email)) {
                response.sendRedirect(request.getContextPath() + "/" + dashboardPath
                        + "?profileUpdated=false&error=email");
                return;
            }
            // Update user if no issue
            user.setPassword(password.trim());
            user.setGender(gender.trim());
            user.setPhoneNumber(phone.trim());
            user.setIcNumber(ic.trim());
            user.setEmail(email.trim());
            user.setAddress(address.trim());

            myUserFacade.edit(user);
            session.setAttribute("loggedInUser", user);

            response.sendRedirect(request.getContextPath() + "/" + dashboardPath + "?profileUpdated=true");
        } catch (Exception e) {
            e.printStackTrace(); // log exception to server            
            response.sendRedirect(request.getContextPath() + "/" + dashboardPath
                    + "?profileUpdated=false&error=ERROR_UPDATING_PROFILE");
        }
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "EditProfile servlet";
    }// </editor-fold>

    private boolean isStrongPassword(String password) {
        return password != null &&
               password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[0-9].*") &&
               password.matches(".*[^A-Za-z0-9].*");
    }

    private boolean isValidEmail(String email) {
        return email != null &&
               email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
    
    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{10,12}");
    }

    private boolean isValidIC(String ic) {
        return ic.matches("\\d{12}");
    }
}
