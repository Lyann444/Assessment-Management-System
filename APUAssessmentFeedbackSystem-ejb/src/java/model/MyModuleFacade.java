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
public class MyModuleFacade extends AbstractFacade<MyModule> {

    @PersistenceContext(unitName = "APUAssessmentFeedbackSystem-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MyModuleFacade() {
        super(MyModule.class);
    }

    // ==== for AL creates module =====
    //get the lastest module ID
    public String getLastModuleID() {
        try {
            return em.createNamedQuery(
                    "MyModule.findLatestModuleID",
                    String.class
            )
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            return null; // no module yet
        }
    }

    //to check whether there is duplicate module name when AL create
    public MyModule findByModuleName(String name) {
        try {
            return em.createNamedQuery(
                    "MyModule.findByModuleName",
                    MyModule.class
            ).setParameter("name", name)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // ==== for AL manage modules - based on own created modules =====
    public List<MyModule> findByAcademicLeader(String username) {
        return em.createNamedQuery("MyModule.findByAcademicLeader", MyModule.class)
                .setParameter("al", username)
                .getResultList();
    }

    public MyModule findByModuleID(String moduleID) {
        return em.find(MyModule.class, moduleID);
    }

    public void update(MyModule module) {
        em.merge(module);
    }
    
    //FOR Lecturer use
    public List<MyModule> findByLecturer(String lecturerUsername) {
        return em.createNamedQuery(
                "MyModule.findByLecturer",
                MyModule.class
        ).setParameter("lecturer", lecturerUsername)
         .getResultList();
    }

    public List<MyModule> findAvailableForDesign(String lecturerUsername) {
        return em.createNamedQuery(
                "MyModule.findAvailableForDesign",
                MyModule.class
        ).setParameter("lecturer", lecturerUsername)
         .getResultList();
    }
    
    //for admin
    public List<MyModule> findActiveModulesByField(String field) {
    return em.createQuery(
        "SELECT m FROM MyModule m WHERE m.field = :field AND m.status = 'Active'",
        MyModule.class)
        .setParameter("field", field)
        .getResultList();
    }

    // Count how many modules (and thus lecturers) an Academic Leader is managing
    public long countModulesByLeader(String leaderUsername) {
        return em.createQuery("SELECT COUNT(m) FROM MyModule m WHERE m.academicLeaderAssigning = :al", Long.class)
                 .setParameter("al", leaderUsername)
                 .getSingleResult();
    }
    
    //admin to get module that can be assign
    //Condition:
    // - Module that is in Active status
    // - Module that the design submission is in APPROVED status
    public List<MyModule> findAssignableModules() {
        return em.createQuery(
            "SELECT DISTINCT m " +
            "FROM MyModule m, MyModuleAssessmentDesign d " +
            "WHERE m.moduleID = d.moduleID " +
            "AND m.status = :moduleStatus " +
            "AND d.status = :designStatus",
            MyModule.class
        )
        .setParameter("moduleStatus", "Active")
        .setParameter("designStatus", "APPROVED")
        .getResultList();
    }

    
}
