package controller;

import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MyAssessment;
import model.MyAssessmentFacade;
import model.MyModuleAssessmentDesign;
import model.MyModuleAssessmentDesignFacade;

@WebServlet(name = "LecturerLoadAssessment", urlPatterns = {"/LecturerLoadAssessment"})
public class LecturerLoadAssessment extends HttpServlet {

    @EJB
    private MyAssessmentFacade assessmentFacade;

    @EJB
    private MyModuleAssessmentDesignFacade designFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String designID = request.getParameter("designID");

        if (designID == null || designID.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //Load parent
        MyModuleAssessmentDesign design = designFacade.find(designID);

        if (design == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //Load children
        List<MyAssessment> assessments = assessmentFacade.findByDesign(design);

        // Build JSON
        JsonObjectBuilder root = Json.createObjectBuilder();
        
        root.add("moduleID",design.getModuleName() == null ? "" : design.getModuleID());

        root.add("moduleName",design.getModuleName() == null ? "" : design.getModuleName());

        root.add("feedback",design.getFeedback() == null ? "" : design.getFeedback());

        JsonArrayBuilder assessmentsArray = Json.createArrayBuilder();

        for (MyAssessment a : assessments) {
            assessmentsArray.add(
                Json.createObjectBuilder()
                    .add("assessmentType", a.getAssessmentType())
                    .add("weightage", a.getWeightage())
            );
        }

        root.add("assessments", assessmentsArray);
        JsonObject result = root.build();
        response.getWriter().write(result.toString());
    }
}
