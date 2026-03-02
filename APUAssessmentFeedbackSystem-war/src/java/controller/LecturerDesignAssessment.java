/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MyModuleFacade;
import model.MyUser;

/**
 *
 * @author USER
 */
@WebServlet(name = "LecturerDesignAssessment", urlPatterns = {"/LecturerDesignAssessment"})
public class LecturerDesignAssessment extends HttpServlet {

    @EJB
    private MyModuleFacade myModuleFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        MyUser lecturer = (MyUser) request.getSession().getAttribute("loggedInUser");

        request.setAttribute("lecturerModules",myModuleFacade.findAvailableForDesign(lecturer.getUsername()));

        request.getRequestDispatcher("/lecturer/lecturerDesignAssessment.jsp").forward(request, response);
    }
}
