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
public class MyStudentClassFacade extends AbstractFacade<MyStudentClass> {

    @PersistenceContext(unitName = "APUAssessmentFeedbackSystem-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MyStudentClassFacade() {
        super(MyStudentClass.class);
    }
    
    public List<MyStudentClass> findByClassID(String classID) {
        return em.createQuery(
            "SELECT sc FROM MyStudentClass sc WHERE sc.classID = :cid",
            MyStudentClass.class
        )
        .setParameter("cid", classID)
        .getResultList();
    }
    
    public List<String> findStudentNamesByClass(String classID) {
        return em.createQuery(
            "SELECT sc.studentName FROM MyStudentClass sc " +
            "WHERE sc.classID = :cid",
            String.class
        )
        .setParameter("cid", classID)
        .getResultList();
    }
}
