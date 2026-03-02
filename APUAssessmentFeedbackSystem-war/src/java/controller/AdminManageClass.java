/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MyClass;
import model.MyClassFacade;
import model.MyModule;
import model.MyModuleFacade;
import model.MyStudentClass;
import model.MyStudentClassFacade;
import model.MyUser;
import model.MyUserFacade;

/**
 *
 * @author User
 */
@WebServlet(name = "AdminManageClass", urlPatterns = {"/AdminManageClass"})
public class AdminManageClass extends HttpServlet {

    @EJB
    private MyClassFacade classFacade;

    @EJB
    private MyStudentClassFacade studentClassFacade;

    @EJB
    private MyModuleFacade moduleFacade;

    @EJB
    private MyUserFacade userFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Load all modules and all existing classes
        request.setAttribute("moduleList", moduleFacade.findAssignableModules());
        request.setAttribute("classList", classFacade.findAll());
        request.setAttribute("registrations", studentClassFacade.findAll());

        request.getRequestDispatcher("/admin/adminManageClass.jsp").forward(request, response);
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

        String action = request.getParameter("action");

        // AJAX Handler: ONLY run this if action is 'getModuleInfo'
        if ("getModuleInfo".equals(action)) {
            String moduleID = request.getParameter("moduleID");
            MyModule module = moduleFacade.find(moduleID);

            if (module != null) {
                List<MyUser> students = userFacade.findEligibleStudentsForModule(module.getField(), module.getModuleID());

                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();

                // Generate Field Display
                out.println("<div class='form-group'>");
                out.println("  <label>Field</label>");
                out.println("  <div style='padding: 10px; background: #e8f4fd; border: 1px solid #3498db; border-radius: 6px; font-weight: bold; color: #2c3e50;'>");
                out.println(module.getField());
                out.println("  </div>");
                out.println("  <input type='hidden' name='field' value='" + module.getField() + "'>");
                out.println("</div>");

                // Generate Student Multi-select
                out.println("<div class='form-group'>");
                out.println("  <label>Students in " + module.getField() + " (Hold Ctrl to select multiple)</label>");
                out.println("  <select name='students' multiple required style='width:100%; height: 150px; border: 1px solid #ccc; border-radius: 6px; padding: 5px;'>");
                if (students != null && !students.isEmpty()) {
                    for (MyUser s : students) {
                        out.println("<option value='" + s.getUsername() + "'>" + s.getUsername() + " (" + s.getEmail() + ")</option>");
                    }
                } else {
                    out.println("<option disabled>No students available to be registered to this module in this field</option>");
                }
                out.println("  </select>");
                out.println("</div>");
                out.flush(); // Ensure everything is sent
                return;
            }
        }

        // If no AJAX action was found, load the full page normally
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

        if ("addClass".equals(action)) {
            try {
                String className = request.getParameter("className");
                String moduleID = request.getParameter("moduleID");
                String field = request.getParameter("field");
                String[] selectedStudents = request.getParameterValues("students");

                // --- DUPLICATE CHECK ---
                List<MyClass> existingClasses = classFacade.findAll();
                for (MyClass c : existingClasses) {
                    if (c.getClassName().equalsIgnoreCase(className)) {
                        // Redirect with a specific error parameter
                        response.sendRedirect(request.getContextPath()
                                + "/admin/adminDashboard.jsp?tab=manageClass&duplicateClass=true");
                        return;
                    }
                }

                String classID = classFacade.getNextClassID();
                MyClass newClass = new MyClass(classID, className, moduleID, field);
                classFacade.create(newClass);

                request.getSession().setAttribute("targetClassName", className);
                request.getSession().setAttribute("targetModuleID", moduleID);

                if (selectedStudents != null) {
                    for (String studentName : selectedStudents) {
                        MyStudentClass mapping = new MyStudentClass(studentName + "_" + classID, studentName, classID);
                        studentClassFacade.create(mapping);
                    }
                }
                response.sendRedirect(request.getContextPath() + "/admin/adminDashboard.jsp?tab=manageClass&addedClass=true");
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath() + "/admin/adminDashboard.jsp?tab=manageClass&addedClass=false");
            }
        } else if ("deleteClass".equals(action)) {
            try {
                String classID = request.getParameter("classID");

                // 1. Remove Student Registrations first to avoid Foreign Key errors
                List<MyStudentClass> registrations = studentClassFacade.findAll();
                for (MyStudentClass reg : registrations) {
                    if (reg.getClassID().equals(classID)) {
                        studentClassFacade.remove(reg);
                    }
                }

                // 2. Remove the Class itself
                MyClass c = classFacade.find(classID);
                if (c != null) {
                    // Set session attributes before deleting
                    request.getSession().setAttribute("targetClassName", c.getClassName());
                    request.getSession().setAttribute("targetModuleName", c.getModuleID());

                    classFacade.remove(c);
                }

                response.sendRedirect(request.getContextPath() + "/admin/adminDashboard.jsp?tab=manageClass&deletedClass=true");
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath() + "/admin/adminDashboard.jsp?tab=manageClass&deletedClass=false");
            }
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
