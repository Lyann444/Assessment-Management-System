/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.MyModule;
import model.MyModuleFacade;
import model.MyUser;

/**
 *
 * @author USER
 */
@WebServlet(name = "ALCreateModule", urlPatterns = {"/ALCreateModule"})
public class ALCreateModule extends HttpServlet {

    @EJB
    private MyModuleFacade myModuleFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String moduleName = request.getParameter("moduleName");
            String moduleDescription = request.getParameter("moduleDescription");
            String assignedLecturerUsername = request.getParameter("assignedLecturerUsername");

            if (moduleName == null || moduleName.trim().isEmpty()
                    || moduleDescription == null || moduleDescription.trim().isEmpty()
                    || assignedLecturerUsername == null || assignedLecturerUsername.trim().isEmpty()) {
                return; //do not save if it is empty
            }

            moduleName = moduleName.trim();
            moduleDescription = moduleDescription.trim();

            //check if there is duplicate module name (which by assumption it wouldnt as 1 module is assumed to be handled by 1 lecturer)
            if (myModuleFacade.findByModuleName(moduleName) != null) {
                response.sendRedirect(request.getContextPath() + "/academicLeader/academicLeaderDashboard.jsp?tab=createModule&error=duplicate");
                return;
            }

            //auto generate module ID
            String lastID = myModuleFacade.getLastModuleID();
            String moduleID;

            if (lastID == null) {
                moduleID = "MD001";
            } else {
                int num = Integer.parseInt(lastID.substring(2));
                moduleID = "MD" + String.format("%03d", num + 1);
            }

            HttpSession session = request.getSession();
            MyUser academicLeader = (MyUser) session.getAttribute("loggedInUser");
            String academicLeaderUsername = academicLeader.getUsername();
            String field = academicLeader.getField();

            myModuleFacade.create(new MyModule(moduleID, moduleName, moduleDescription, field, assignedLecturerUsername, academicLeaderUsername));

            response.sendRedirect(request.getContextPath() + "/academicLeader/academicLeaderDashboard.jsp?tab=createModule&success=created");
        } catch (Exception e) {
            e.printStackTrace();
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
