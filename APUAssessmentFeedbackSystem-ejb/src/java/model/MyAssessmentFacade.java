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
public class MyAssessmentFacade extends AbstractFacade<MyAssessment> {

    @PersistenceContext(unitName = "APUAssessmentFeedbackSystem-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MyAssessmentFacade() {
        super(MyAssessment.class);
    }

    public List<MyAssessment>
    findByDesign(MyModuleAssessmentDesign design) {
        return em.createNamedQuery(
                "MyAssessment.findByDesign",
                MyAssessment.class
        ).setParameter("design", design)
                .getResultList();
    }

    public String generateNextAssessmentID() {
        String lastID = em.createQuery(
                "SELECT a.assessmentID FROM MyAssessment a "
                + "WHERE a.assessmentID LIKE 'AS%' "
                + "ORDER BY a.assessmentID DESC",
                String.class
        ).setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null);

        int next = 1;

        if (lastID != null && lastID.matches("AS\\d{3}")) {
            next = Integer.parseInt(lastID.substring(2)) + 1;
        }

        return String.format("AS%03d", next);
    }
}
