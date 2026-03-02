/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
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
 * @author USER
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    @EJB
    private MyUserFacade myUserFacade;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            //hard coded super admin
            if ("superadmin".equalsIgnoreCase(username) && "12345".equals(password)) {

                HttpSession session = request.getSession();
                session.setAttribute("loggedInUsername", "superadmin");
                session.setAttribute("loggedInUserType", "Admin");

                request.getRequestDispatcher("admin/adminDashboard.jsp")
                        .include(request, response);
                return;
            }

            MyUser found = myUserFacade.searchUsername(username);

            //Invalid username
            if (found == null) {
                request.setAttribute("error", "invalid");
                throw new Exception();
            }

            if ("Blocked".equalsIgnoreCase(found.getStatus())) {
                request.setAttribute("error", "blocked");
                request.getRequestDispatcher("general/login.jsp")
                        .include(request, response);
                return;
            }
            //Invalid password
            if (!password.equals(found.getPassword())) {
                request.setAttribute("error", "invalid");
                throw new Exception();
            }

            //Login successful
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUsername", found.getUsername());
            session.setAttribute("loggedInUserType", found.getUserType());
            session.setAttribute("loggedInUser", found);

            //Direct user based on userType
            switch (found.getUserType()) {
                case "Student":
                    request.getRequestDispatcher("student/studentDashboard.jsp").include(request, response);
                    return;

                case "Lecturer":
                    request.getRequestDispatcher("lecturer/lecturerDashboard.jsp").include(request, response);
                    return;

                case "Academic Leader":
                    request.getRequestDispatcher("academicLeader/academicLeaderDashboard.jsp").include(request, response);
                    return;

                case "Admin":
                    request.getRequestDispatcher("admin/adminDashboard.jsp").include(request, response);
                    return;

                default:
                    request.setAttribute("error", "role");
                    request.getRequestDispatcher("general/login.jsp").include(request, response);
                    return;
            }
        } catch (Exception e) {
            //login failed
            request.getRequestDispatcher("general/login.jsp").include(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
