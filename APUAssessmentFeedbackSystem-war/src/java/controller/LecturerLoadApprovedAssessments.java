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
import model.MyAssessment;
import model.MyAssessmentFacade;
import model.MyClass;
import model.MyClassFacade;
import model.MyModuleAssessmentDesign;
import model.MyModuleAssessmentDesignFacade;

/**
 *
 * @author USER
 */
@WebServlet(name = "LecturerLoadApprovedAssessments", urlPatterns = {"/LecturerLoadApprovedAssessments"})
public class LecturerLoadApprovedAssessments extends HttpServlet {

    @EJB
    private MyClassFacade classFacade;

    @EJB
    private MyModuleAssessmentDesignFacade designFacade;

    @EJB
    private MyAssessmentFacade assessmentFacade;

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String classID = request.getParameter("classID");

        MyClass c = classFacade.find(classID);

        MyModuleAssessmentDesign design = designFacade.findApprovedDesignByModuleID(c.getModuleID());

        List<MyAssessment> assessments
                = assessmentFacade.findByDesign(design);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < assessments.size(); i++) {
            MyAssessment a = assessments.get(i);

            json.append("{")
                    .append("\"assessmentID\":\"").append(a.getAssessmentID()).append("\",")
                    .append("\"assessmentType\":\"").append(a.getAssessmentType()).append("\",")
                    .append("\"weightage\":").append(a.getWeightage())
                    .append("}");

            if (i < assessments.size() - 1) {
                json.append(",");
            }
        }

if (design == null) {
    response.getWriter().write("[]");
    return;
}
        json.append("]");

        response.getWriter().write(json.toString());
    }
}
