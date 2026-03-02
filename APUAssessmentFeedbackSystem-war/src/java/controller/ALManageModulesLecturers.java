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
import model.MyUser;
import model.MyUserFacade;

/**
 *
 * @author USER
 */
@WebServlet(name = "ALManageModulesLecturers", urlPatterns = {"/ALManageModulesLecturers"})
public class ALManageModulesLecturers extends HttpServlet {

    @EJB
    private MyUserFacade myUserFacade;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        MyUser al = (MyUser) req.getSession().getAttribute("loggedInUser");
        if (al == null) return;

        List<MyUser> lecturers =
                myUserFacade.findLecturersByAcademicLeader(al.getUsername());

        res.setContentType("application/json");
        res.getWriter().print(
            lecturers.stream()
                .map(u -> "\"" + u.getUsername() + "\"")
                .collect(java.util.stream.Collectors.joining(",", "[", "]"))
        );
    }
}
