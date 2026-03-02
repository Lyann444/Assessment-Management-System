/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author USER
 */
@Stateless
public class MyClassFacade extends AbstractFacade<MyClass> {

    @PersistenceContext(unitName = "APUAssessmentFeedbackSystem-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MyClassFacade() {
        super(MyClass.class);
    }

    public String getNextClassID() {
        List<String> result = em.createQuery(
            "SELECT c.classID FROM MyClass c ORDER BY c.classID DESC",
            String.class)
            .setMaxResults(1)
            .getResultList();

        if (result.isEmpty()) return "CL001";

        int num = Integer.parseInt(result.get(0).substring(2));
        return String.format("CL%03d", num + 1);
    }
    
    /* FOR LECTURER TO KEY IN MARKS
     * Classes eligible for lecturer to key in marks
     * Conditions:
     * - Lecturer teaches the module
     * - Module is ACTIVE
     * - Assessment design is APPROVED
     */
    public List<MyClass> findClassesForMarking(String lecturerUsername) {

        return em.createQuery(
                "SELECT DISTINCT c FROM MyClass c "
                + "JOIN MyModule m ON c.moduleID = m.moduleID "
                + "JOIN MyModuleAssessmentDesign d ON d.moduleID = m.moduleID "
                + "WHERE m.assignedLecturerUsername = :lecturer "
                + "AND m.status = 'Active' "
                + "AND d.status = 'APPROVED'",
                MyClass.class
        )
                .setParameter("lecturer", lecturerUsername)
                .getResultList();
    }

}
