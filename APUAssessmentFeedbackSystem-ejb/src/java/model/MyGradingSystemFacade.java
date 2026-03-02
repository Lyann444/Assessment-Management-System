/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author USER
 */
@Stateless
public class MyGradingSystemFacade extends AbstractFacade<MyGradingSystem> {

    @PersistenceContext(unitName = "APUAssessmentFeedbackSystem-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MyGradingSystemFacade() {
        super(MyGradingSystem.class);
    }
    
    // Check if grade name already exists
    public boolean isGradeExists(String grade) {
        return em.find(MyGradingSystem.class, grade) != null;
    }
    
    // Check if min-max range overlaps with existing grades
    public boolean isRangeOverlapping(double minMark, double maxMark, String excludeGrade) {
        String jpql = "SELECT g FROM MyGradingSystem g WHERE g.grade != :exclude " +
                      "AND ((:min BETWEEN g.minMark AND g.maxMark) OR " +
                      "(:max BETWEEN g.minMark AND g.maxMark) OR " +
                      "(g.minMark BETWEEN :min AND :max) OR " +
                      "(g.maxMark BETWEEN :min AND :max))";
        return !em.createQuery(jpql, MyGradingSystem.class)
                  .setParameter("exclude", excludeGrade != null ? excludeGrade : "")
                  .setParameter("min", minMark)
                  .setParameter("max", maxMark)
                  .getResultList()
                  .isEmpty();
    }
    
    public void addGrade(MyGradingSystem grade) {
        em.persist(grade);
    }
    
    public void updateGrade(MyGradingSystem grade) {
        em.merge(grade);
    }
    
    public void deleteGrade(String existingGrade) {
        MyGradingSystem grade = em.find(MyGradingSystem.class, existingGrade);
        if (grade != null) {
            em.remove(grade);
        }
    }
    
    // Find grade by mark
    public String findGradeByMark(double mark) {
        try {
            MyGradingSystem grade = em.createQuery(
                "SELECT g FROM MyGradingSystem g WHERE :mark BETWEEN g.minMark AND g.maxMark",
                MyGradingSystem.class)
                .setParameter("mark", mark)
                .getSingleResult();
            return grade.getGrade();
        } catch (Exception e) {
            return "N/A";
        }
    }
}
