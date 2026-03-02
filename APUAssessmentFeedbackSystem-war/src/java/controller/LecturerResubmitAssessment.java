/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MyModuleAssessmentDesignFacade;

/**
 *
 * @author USER
 */
@WebServlet(name = "LecturerResubmitAssessment", urlPatterns = {"/LecturerResubmitAssessment"})
public class LecturerResubmitAssessment extends HttpServlet {

    @EJB
    private MyModuleAssessmentDesignFacade designFacade;

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String designID = request.getParameter("designID");
        String[] types = request.getParameterValues("assessmentType");
        String[] weights = request.getParameterValues("weightage");

        if (designID == null || types == null || weights == null) {
            redirectError(response, request, "missing");
            return;
        }

        if (types.length < 1 || types.length > 3) {
            redirectError(response, request, "count");
            return;
        }

        double total = 0;
        Set<String> uniqueTypes = new HashSet<>();

        try {
            for (int i = 0; i < types.length; i++) {

                if (types[i] == null || types[i].isEmpty()) {
                    redirectError(response, request, "empty");
                    return;
                }

                if (!uniqueTypes.add(types[i])) {
                    redirectError(response, request, "duplicate");
                    return;
                }

                total += Double.parseDouble(weights[i]);
            }
        } catch (Exception e) {
            redirectError(response, request, "weightage");
            return;
        }

        if (total != 100) {
            redirectError(response, request, "weightage");
            return;
        }

        designFacade.resubmitAssessmentDesign(designID, types, weights);

        response.sendRedirect(
                request.getContextPath() + "/lecturer/lecturerDashboard.jsp?tab=manageAssessment&success=resubmitted");
    }

    private void redirectError(HttpServletResponse response,
            HttpServletRequest request,
            String errorCode) throws IOException {

        response.sendRedirect(
                request.getContextPath() + "/lecturer/lecturerDashboard.jsp?tab=manageAssessment&error=" + errorCode);
    }
}
