/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MyClass;
import model.MyClassFacade;
import model.MyUser;

/**
 *
 * @author USER
 */
@WebServlet(name = "LecturerKeyInMarks", urlPatterns = {"/LecturerKeyInMarks"})
public class LecturerKeyInMarks extends HttpServlet {

    @EJB
    private MyClassFacade classFacade;

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        MyUser lecturer
                = (MyUser) request.getSession().getAttribute("loggedInUser");

        if (lecturer == null) {
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        List<MyClass> classes
                = classFacade.findClassesForMarking(lecturer.getUsername());

        request.setAttribute("classes", classes);
        request.getRequestDispatcher(
                "/lecturer/lecturerKeyInMarks.jsp"
        ).forward(request, response);
    }
}
