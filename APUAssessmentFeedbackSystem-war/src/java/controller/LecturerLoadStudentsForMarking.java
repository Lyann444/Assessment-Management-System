/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MyResult;
import model.MyResultFacade;
import model.MyStudentClass;
import model.MyStudentClassFacade;
import model.MyUser;
import model.MyUserFacade;

/**
 *
 * @author USER
 */
@WebServlet(name = "LecturerLoadStudentsForMarking", urlPatterns = {"/LecturerLoadStudentsForMarking"})
public class LecturerLoadStudentsForMarking extends HttpServlet {

    @EJB
    private MyStudentClassFacade studentClassFacade;

    @EJB
    private MyUserFacade userFacade;

    @EJB
    private MyResultFacade resultFacade;

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String classID = request.getParameter("classID");
        String assessmentID = request.getParameter("assessmentID");

        List<MyStudentClass> list
                = studentClassFacade.findByClassID(classID);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        for (MyStudentClass sc : list) {
            MyUser student = userFacade.find(sc.getStudentName());

            MyResult r
                    = resultFacade.findByStudentAndAssessment(student.getUsername(), assessmentID);

            if (!first) {
                json.append(",");
            }
            first = false;

            json.append("{")
                    .append("\"studentName\":\"").append(student.getUsername()).append("\",")
                    .append("\"marks\":")
                    .append(r == null ? "null" : r.getMarks()).append(",")
                    .append("\"feedback\":\"")
                    .append(r == null || r.getFeedback() == null ? "" : r.getFeedback())
                    .append("\"")
                    .append("}");
        }

        json.append("]");
        response.getWriter().write(json.toString());
    }
}
