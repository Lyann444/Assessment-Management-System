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
import model.MyResultFacade;
import model.MyUser;
import model.dto.FinalResultDTO;
import model.dto.StudentResultDTO;

/**
 *
 * @author User
 */

@WebServlet(name = "StudentViewResult", urlPatterns = {"/StudentViewResult"})
public class StudentViewResult extends HttpServlet {

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
    private MyResultFacade resultFacade;       
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        MyUser student = (MyUser) request.getSession().getAttribute("loggedInUser");
        String username = (String) request.getSession().getAttribute("loggedInUsername");

        if (student == null) {
            response.sendRedirect(request.getContextPath() + "/general/login.jsp");
            return;
        }
        else{
            List<StudentResultDTO> assessmentResults = resultFacade.getStudentResults(username);
            List<FinalResultDTO> finalResults = resultFacade.getFinalModuleResults(username);
    
            // Set the attribute name exactly as used in your JSP <c:forEach>
            request.setAttribute("assessmentData", assessmentResults);
            request.setAttribute("finalData", finalResults); // New attribute for the second table
            
            request.getRequestDispatcher("student/studentViewResult.jsp").forward(request, response);
        }
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
