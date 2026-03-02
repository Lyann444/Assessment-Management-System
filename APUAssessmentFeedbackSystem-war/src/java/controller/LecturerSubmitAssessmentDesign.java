
import model.MyModuleAssessmentDesignFacade;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

@WebServlet(name = "LecturerSubmitAssessmentDesign", urlPatterns = {"/LecturerSubmitAssessmentDesign"})
public class LecturerSubmitAssessmentDesign extends HttpServlet {

    @EJB
    private MyModuleAssessmentDesignFacade myModuleAssessmentDesignFacade;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

// logged-in lecturer 
        String lecturerUsername = (String) session.getAttribute("loggedInUsername");
        String moduleID = request.getParameter("moduleID");
        String moduleName = request.getParameter("moduleName");
        String[] types = request.getParameterValues("assessmentType");
        String[] weights = request.getParameterValues("weightage");

        // === Defensive validation === 
        if (lecturerUsername == null || moduleID == null
        || moduleName == null || types == null 
        || weights == null || types.length != weights.length 
        || types.length < 1 || types.length > 3) {
        response.sendRedirect( request.getContextPath() + "/lecturer/lecturerDashboard.jsp?tab=designAssessment&error=validation" ); return; }
        
        double total = 0;
        Set<String> uniqueTypes = new HashSet<>();

        try {
            for (int i = 0; i < types.length; i++) {
                double w = Double.parseDouble(weights[i]);
                total += w;
                if (!uniqueTypes.add(types[i])) {
                    throw new IllegalArgumentException("Duplicate type");
                }
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/lecturer/lecturerDashboard.jsp?tab=designAssessment&error=validation");
            return;
        }
        if (total
                != 100) {
            response.sendRedirect(request.getContextPath() + "/lecturer/lecturerDashboard.jsp?tab=designAssessment&error=validation");
            return;
        } // === Persist === 
        try {
            myModuleAssessmentDesignFacade.submitAssessmentDesign(moduleID, moduleName, lecturerUsername, types, weights);
            response.sendRedirect(request.getContextPath() + "/lecturer/lecturerDashboard.jsp?tab=designAssessment&success=submitted");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/lecturer/lecturerDashboard.jsp?tab=designAssessment&error=validation");
        }
    }
}
