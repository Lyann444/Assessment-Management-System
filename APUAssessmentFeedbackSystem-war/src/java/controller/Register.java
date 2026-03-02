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
import model.MyUser;
import model.MyUserFacade;

/**
 *
 * @author USER
 */
@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {

    @EJB
    private MyUserFacade myUserFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");        

        try {
            String username = request.getParameter("username");
            //validate username
            if ("superadmin".equalsIgnoreCase(username)|| myUserFacade.searchUsername(username) != null) {
                request.setAttribute("error", "username");
                throw new Exception();
            }
            
            String password = request.getParameter("password");
            //validate weak password
            if (!isStrongPassword(password)) {
                request.setAttribute("error", "weakpassword");
                throw new Exception();
            }
            
            String phoneNumber = request.getParameter("phonenum");
            //validate phone number must be numeric
            if (!isNumeric(phoneNumber)) {
                request.setAttribute("error", "phone");
                throw new Exception();
            }
            
            String icNumber = request.getParameter("icnum");
            //validate ic must be numeric
            if (!isNumeric(icNumber)) {
                request.setAttribute("error", "ic");
                throw new Exception();
            }
            
            String email = request.getParameter("email");
            //validate correct email format
            if (!isValidEmail(email)) {
                request.setAttribute("error", "email");
                throw new Exception();
            }
            
            String gender = request.getParameter("gender");
            String address = request.getParameter("address");
            String userType = "Student";
            String status = "Active";
            String field = "General";
            String assignedAcademicLeader = "NA";
            
            myUserFacade.create(new MyUser(username,password,gender,phoneNumber,icNumber,email,address,userType,field, assignedAcademicLeader, status));
            request.setAttribute("success", "registered");
            request.getRequestDispatcher("general/login.jsp").include(request, response);
            
        }catch (Exception e) {

            request.getRequestDispatcher("general/register.jsp")
                .include(request, response);
        }
    }
            
            
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        processRequest(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        processRequest(req, res);
    }

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
    
    private boolean isNumeric(String value) {
        return value != null && value.matches("\\d+");
    }
}
