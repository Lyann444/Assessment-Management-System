/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.MyAssessment;
import model.MyAssessmentFacade;
import model.MyModuleAssessmentDesign;
import model.MyModuleAssessmentDesignFacade;
import model.MyUser;

/**
 *
 * @author USER
 */
@WebServlet(name = "ALAssessmentReview", urlPatterns = {"/ALAssessmentReview"})
public class ALAssessmentReview extends HttpServlet {

    @EJB
    private MyModuleAssessmentDesignFacade myModuleAssessmentDesignFacade;

    @EJB
    private MyAssessmentFacade myAssessmentFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        MyUser al = (MyUser) session.getAttribute("loggedInUser");

        if (al == null) {
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        String status = request.getParameter("status");
        if (status == null || status.equals("ALL")) {
            status = "PENDING"; // pending status is the default one
        }

        List<MyModuleAssessmentDesign> designs;

        if ("ALL".equals(status)) {
            designs = myModuleAssessmentDesignFacade.findByAL(al.getUsername());
        } else {
            designs = myModuleAssessmentDesignFacade.findByStatusAndAL(status, al.getUsername());
        }

        Map<String, List<MyAssessment>> assessmentMap = new HashMap<>();

        for (MyModuleAssessmentDesign d : designs) {
            assessmentMap.put(
                    d.getDesignID(), // key = designID string (for JSP convenience)
                    myAssessmentFacade.findByDesign(d) // pass ENTITY
            );
        }

        request.setAttribute("assessmentMap", assessmentMap);

        request.setAttribute("designs", designs);
        request.setAttribute("selectedStatus", status);

        request.getRequestDispatcher("/academicLeader/assessmentReview.jsp").forward(request, response);
    }
}
