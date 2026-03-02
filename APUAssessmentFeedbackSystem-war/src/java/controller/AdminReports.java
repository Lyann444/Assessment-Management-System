/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MyUserFacade;

/**
 *
 * @author User
 */
@WebServlet(name = "AdminReports", urlPatterns = {"/AdminReports"})
public class AdminReports extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    @EJB
    private MyUserFacade userFacade;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. User Type Report (Static labels)
        long students = userFacade.countByUserType("Student");
        long lecturers = userFacade.countByUserType("Lecturer");
        long admins = userFacade.countByUserType("Admin");
        long academicLeaders = userFacade.countByUserType("Academic Leader");
        request.setAttribute("userTypeData", "[" + students + "," + lecturers + "," + admins + "," + academicLeaders + "]");

        // 2. Status Report (Static labels)
        long active = userFacade.countByStatus("Active");
        long blocked = userFacade.countByStatus("Blocked");
        request.setAttribute("statusData", "[" + active + "," + blocked + "]");

        // 3. Gender Report (Static labels)
        long male = userFacade.countByGender("Male");
        long female = userFacade.countByGender("Female");
        request.setAttribute("genderData", "[" + male + "," + female + "]");

        // 4. Field of Study Report (Dynamic labels)
        List<Object[]> results = userFacade.countUserBreakdownByField();        

        // Map to store: FieldName -> {Role: Count}
        Map<String, Map<String, Long>> matrix = new HashMap<>();
        Set<String> fields = new TreeSet<>();

        for (Object[] row : results) {
            String field = (String) row[0];
            String role = (String) row[1];
            long count = (Long) row[2];

            fields.add(field);
            matrix.putIfAbsent(field, new HashMap<>());
            matrix.get(field).put(role, count);
        }

        // Convert back to JS format
        StringBuilder labels = new StringBuilder("[");
        StringBuilder studentsByField = new StringBuilder("[");
        StringBuilder lecturersByField = new StringBuilder("[");
        StringBuilder adminsByField = new StringBuilder("[");
        StringBuilder leadersByField = new StringBuilder("[");

        for (String f : fields) {
            labels.append("\"").append(f).append("\",");
            studentsByField.append(matrix.get(f).getOrDefault("Student", 0L)).append(",");
            lecturersByField.append(matrix.get(f).getOrDefault("Lecturer", 0L)).append(",");
            adminsByField.append(matrix.get(f).getOrDefault("Admin", 0L)).append(",");
            leadersByField.append(matrix.get(f).getOrDefault("Academic Leader", 0L)).append(",");
        }

        // Clean trailing commas and close brackets
        String finalLabels = labels.toString().replaceAll(",$", "") + "]";
        request.setAttribute("fieldLabels", finalLabels); // Use THIS for the Chart labels

        request.setAttribute("dataStudents", studentsByField.toString().replaceAll(",$", "") + "]");
        request.setAttribute("dataLecturers", lecturersByField.toString().replaceAll(",$", "") + "]");
        request.setAttribute("dataAdmins", adminsByField.toString().replaceAll(",$", "") + "]");
        request.setAttribute("dataLeaders", leadersByField.toString().replaceAll(",$", "") + "]");

        // 5. Lecturers per Leader Report (Dynamic labels)
        // Note: Logic assumes a GROUP BY on assignedAcademicLeader in Facade
        List<Object[]> leaderResults = userFacade.countLecturersByLeader();
        request.setAttribute("leaderLabels", formatLabels(leaderResults));
        request.setAttribute("leaderData", formatData(leaderResults));

        request.getRequestDispatcher("admin/adminReports.jsp").forward(request, response);
    }
    

    // Helper to format ["Label1", "Label2"]
    private String formatLabels(List<Object[]> results) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < results.size(); i++) {
            sb.append("\"").append(results.get(i)[0]).append("\"");
            if (i < results.size() - 1) sb.append(",");
        }
        return sb.append("]").toString();
    }

    // Helper to format [10, 20]
    private String formatData(List<Object[]> results) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < results.size(); i++) {
            sb.append(results.get(i)[1]);
            if (i < results.size() - 1) sb.append(",");
        }
        return sb.append("]").toString();
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
