/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MyModule;
import model.MyModuleFacade;

/**
 *
 * @author USER
 */
@WebServlet(name = "ALEditModule", urlPatterns = {"/ALEditModule"})
public class ALEditModule extends HttpServlet {

    @EJB
    private MyModuleFacade myModuleFacade;


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String moduleID = request.getParameter("moduleID");
        String newName = request.getParameter("moduleName");

        MyModule existing = myModuleFacade.findByModuleName(newName);

        if (existing != null && !existing.getModuleID().equals(moduleID)) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/academicLeader/academicLeaderDashboard.jsp?tab=manageModules&error=duplicate"
            );
            return;
        }

        MyModule module = myModuleFacade.findByModuleID(moduleID);
        if (module == null) return;

        module.setModuleName(request.getParameter("moduleName"));
        module.setModuleDescription(request.getParameter("moduleDescription"));
        module.setAssignedLecturerUsername(
                request.getParameter("assignedLecturerUsername")
        );
        module.setStatus(request.getParameter("status"));
        myModuleFacade.update(module);

        response.sendRedirect(request.getContextPath()+ "/academicLeader/academicLeaderDashboard.jsp?tab=manageModules&success=updated"
        );
    }
}

