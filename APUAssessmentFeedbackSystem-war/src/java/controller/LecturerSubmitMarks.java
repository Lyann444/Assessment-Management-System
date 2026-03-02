/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MyResultFacade;

/**
 *
 * @author USER
 */
@WebServlet(name = "LecturerSubmitMarks", urlPatterns = {"/LecturerSubmitMarks"})
public class LecturerSubmitMarks extends HttpServlet {

    @EJB
    private MyResultFacade resultFacade;

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String assessmentID = request.getParameter("assessmentID");

        request.getParameterMap().forEach((key, value) -> {

            if (key.startsWith("marks_")) {

                String studentName = key.substring(6);
                double marks = Double.parseDouble(value[0]);

                String feedback
                        = request.getParameter("feedback_" + studentName);

                resultFacade.saveOrUpdate(
                        studentName,
                        assessmentID,
                        marks,
                        feedback
                );
            }
        });

        response.sendRedirect(request.getContextPath() + "/lecturer/lecturerDashboard.jsp" + "?tab=keyInMarks&success=saved");
    }
}
