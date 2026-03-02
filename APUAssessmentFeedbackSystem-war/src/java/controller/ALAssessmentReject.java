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
import model.MyModuleAssessmentDesign;
import model.MyModuleAssessmentDesignFacade;

/**
 *
 * @author USER
 */
@WebServlet(name = "ALRejectAssessment", urlPatterns = {"/ALRejectAssessment"})
public class ALAssessmentReject extends HttpServlet {

    @EJB
    private MyModuleAssessmentDesignFacade myModuleAssessmentDesignFacade;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        String designID = req.getParameter("designID");
        String feedback = req.getParameter("feedback");

        if (designID == null || designID.isEmpty()) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Design ID missing");
            return;
        }
        
        if (feedback == null || feedback.trim().isEmpty()) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Feedback missing");
            return;
        }
        
        MyModuleAssessmentDesign d = myModuleAssessmentDesignFacade.find(designID);
        if (d == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Assessment not found");
            return;
        }
        d.setStatus("REJECTED");
        d.setFeedback(feedback);
        myModuleAssessmentDesignFacade.edit(d);

        res.sendRedirect(req.getContextPath()+ "/academicLeader/academicLeaderDashboard.jsp"+ "?tab=assessmentReview&success=rejected");
    }
}
