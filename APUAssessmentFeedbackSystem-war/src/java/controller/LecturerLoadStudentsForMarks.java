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
import model.MyStudentClassFacade;

/**
 *
 * @author USER
 */
@WebServlet(name = "LecturerLoadStudentsForMarks", urlPatterns = {"/LecturerLoadStudentsForMarks"})
public class LecturerLoadStudentsForMarks extends HttpServlet {

    @EJB
    private MyStudentClassFacade studentClassFacade;

    @EJB
    private MyResultFacade resultFacade;

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String classID = request.getParameter("classID");
        String assessmentID = request.getParameter("assessmentID");

        List<String> students
                = studentClassFacade.findStudentNamesByClass(classID);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < students.size(); i++) {
            String student = students.get(i);

            MyResult r
                    = resultFacade.findByStudentAndAssessment(
                            student, assessmentID
                    );

            json.append("{")
                    .append("\"studentName\":\"").append(student).append("\",")
                    .append("\"marks\":")
                    .append(r == null ? "null" : r.getMarks())
                    .append("}");

            if (i < students.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");
        response.getWriter().write(json.toString());
    }
}
