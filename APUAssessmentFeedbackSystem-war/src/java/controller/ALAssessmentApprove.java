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
@WebServlet(name = "ALApproveAssessment", urlPatterns = {"/ALApproveAssessment"})
public class ALAssessmentApprove extends HttpServlet {

    @EJB
    private MyModuleAssessmentDesignFacade myModuleAssessmentDesignFacade;

    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        String designID = req.getParameter("designID");

        MyModuleAssessmentDesign d = myModuleAssessmentDesignFacade.find(designID);
        d.setStatus("APPROVED");
        d.setFeedback(null);
        myModuleAssessmentDesignFacade.edit(d);

        res.sendRedirect(req.getContextPath() + "/academicLeader/academicLeaderDashboard.jsp?tab=assessmentReview&success=approved");
    }

}
