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
import model.MyAssessment;
import model.MyAssessmentFacade;
import model.MyModuleAssessmentDesign;
import model.MyModuleAssessmentDesignFacade;
import model.MyUser;

/**
 *
 * @author USER
 */
@WebServlet(name = "LecturerManageAssessment", urlPatterns = {"/LecturerManageAssessment"})
public class LecturerManageAssessment extends HttpServlet {


    @EJB
    private MyModuleAssessmentDesignFacade designFacade;

    @EJB
    private MyAssessmentFacade assessmentFacade;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        MyUser lecturer =
            (MyUser) request.getSession().getAttribute("loggedInUser");

        if (lecturer == null) {
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        String status = request.getParameter("status");
        if (status == null) status = "REJECTED";

        List<MyModuleAssessmentDesign> designs =
            designFacade.findByLecturerAndStatus(
                lecturer.getUsername(), status
            );

        Map<String, List<MyAssessment>> assessmentMap = new HashMap<>();

        for (MyModuleAssessmentDesign d : designs) {
            assessmentMap.put(
                d.getDesignID(),
                assessmentFacade.findByDesign(d)
            );
        }

        request.setAttribute("designs", designs);
        request.setAttribute("assessmentMap", assessmentMap);
        request.setAttribute("selectedStatus", status);

        request.getRequestDispatcher(
            "/lecturer/lecturerManageAssessment.jsp"
        ).forward(request, response);
    }
}
