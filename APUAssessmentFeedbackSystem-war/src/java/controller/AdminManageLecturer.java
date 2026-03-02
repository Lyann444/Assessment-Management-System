/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
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

@WebServlet(name = "AdminManageLecturer", urlPatterns = {"/AdminManageLecturer"})
public class AdminManageLecturer extends HttpServlet {

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
    private MyUserFacade userFacade;
        
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        request.setAttribute("lecturers", userFacade.findActiveLecturers());
        request.setAttribute("academicLeaders", userFacade.findActiveAcademicLeaders());
        request.setAttribute("assignedList", userFacade.findActiveLecturersWithLeaders());

        request.getRequestDispatcher("/admin/adminManageLecturer.jsp")
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
        
        String action = request.getParameter("action"); // "assign", "editLecturerAssign", "deleteAssign"
        String lecturer = request.getParameter("lecturer");
        String academicLeader = request.getParameter("academicLeader");

        boolean success = false;
        
        if (lecturer != null && !lecturer.isEmpty()) {
            switch (action) {
                case "addLecturerAssign":
                    if (academicLeader != null && !academicLeader.isEmpty()) {
                        success = userFacade.assignAcademicLeader(lecturer, academicLeader);
                        request.getSession().setAttribute("targetLecturer", lecturer);
                        request.getSession().setAttribute("targetAcademicLeader", academicLeader);
                    }
                    response.sendRedirect(request.getContextPath() +
                            "/admin/adminDashboard.jsp?tab=manageLecturer&assignedLecturer=" + success);
                    return;

                case "deleteLecturerAssign":
                    success = userFacade.assignAcademicLeader(lecturer, "NA");
                    request.getSession().setAttribute("targetLecturer", lecturer);
                    response.sendRedirect(request.getContextPath() +
                            "/admin/adminDashboard.jsp?tab=manageLecturer&deletedLecturer=" + success);
                    return;

                default:
                    // Unknown action
                    response.sendRedirect(request.getContextPath() +
                            "/admin/adminDashboard.jsp?tab=manageLecturer&assignedLecturer=false");
                    return;
            }
        }

        // fallback if lecturer is null/empty
        response.sendRedirect(request.getContextPath() +
                "/admin/adminDashboard.jsp?tab=manageLecturer&assignedLecturer=false");
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
