/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MyGradingSystem;
import model.MyGradingSystemFacade;

/**
 *
 * @author User
 */

@WebServlet(name = "AdminManageGrading", urlPatterns = {"/AdminManageGrading"})
public class AdminManageGrading extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    @EJB
    private MyGradingSystemFacade gradingFacade;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MyGradingSystem> grades = gradingFacade.findAll();
        request.setAttribute("grades", grades);

        request.getRequestDispatcher("/admin/adminManageGrading.jsp")
               .forward(request, response);        
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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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

        String action = request.getParameter("action");
        String grade = request.getParameter("grade");

        try {
            double minMark = request.getParameter("minMark") != null 
                    ? Double.parseDouble(request.getParameter("minMark")) : 0;
            double maxMark = request.getParameter("maxMark") != null 
                    ? Double.parseDouble(request.getParameter("maxMark")) : 0;
            double gradePoint = request.getParameter("gradePoint") != null
                    ? Double.parseDouble(request.getParameter("gradePoint")) : 0;

            // Store targetGrade in session for alerts
            request.getSession().setAttribute("targetGrade", grade);
            
            boolean success = false;
            
            switch (action) {
                case "addGrade":
                    success = addGrade(grade, minMark, maxMark, gradePoint);
                    response.sendRedirect(request.getContextPath() + "/admin/adminDashboard.jsp?tab=manageGrading&addedGrade=" + success);
                    break;

                case "editGrade":
                    success = editGrade(grade, minMark, maxMark, gradePoint);
                    response.sendRedirect(request.getContextPath() + "/admin/adminDashboard.jsp?tab=manageGrading&editedGrade=" + success);
                    break;

                case "deleteGrade":
                    success = deleteGrade(grade);
                    response.sendRedirect(request.getContextPath() + "/admin/adminDashboard.jsp?tab=manageGrading&deletedGrade=" + success);
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/admin/adminDashboard.jsp?tab=manageGrading&errorGrade=true");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean addGrade(String grade, double minMark, double maxMark, double gradePoint) {
        try {
            if (gradingFacade.isGradeExists(grade) || gradingFacade.isRangeOverlapping(minMark, maxMark, null)) {
                return false;
            }
            MyGradingSystem g = new MyGradingSystem();
            g.setGrade(grade);
            g.setMinMark(minMark);
            g.setMaxMark(maxMark);
            g.setGradePoint(gradePoint);
            gradingFacade.create(g);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean editGrade(String grade, double minMark, double maxMark, double gradePoint) {
        try {
            MyGradingSystem g = gradingFacade.find(grade);
            if (g == null || gradingFacade.isRangeOverlapping(minMark, maxMark, grade)) {
                return false;
            }
            g.setMinMark(minMark);
            g.setMaxMark(maxMark);
            g.setGradePoint(gradePoint);
            gradingFacade.edit(g);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteGrade(String grade) {
        try {
            MyGradingSystem g = gradingFacade.find(grade);
            if (g == null) return false;
            gradingFacade.remove(g);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
