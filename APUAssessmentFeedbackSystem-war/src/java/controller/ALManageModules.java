/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
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
import javax.servlet.http.HttpSession;
import model.MyModule;
import model.MyModuleFacade;
import model.MyUser;
import model.MyUserFacade;

/**
 *
 * @author USER
 */
@WebServlet(name = "ALManageModules", urlPatterns = {"/ALManageModules"})
public class ALManageModules extends HttpServlet {

    @EJB
    private MyUserFacade myUserFacade;

    @EJB
    private MyModuleFacade myModuleFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        MyUser al = (MyUser) session.getAttribute("loggedInUser");

        if (al == null) {
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        List<MyModule> modules = myModuleFacade.findByAcademicLeader(al.getUsername());
        List<MyUser> lecturers = myUserFacade.findLecturersByAcademicLeader(al.getUsername());

        request.setAttribute("modules", modules);
        request.setAttribute("lecturers", lecturers);
        request.getRequestDispatcher("/academicLeader/manageModules.jsp")
                .include(request, response);
    }
}
