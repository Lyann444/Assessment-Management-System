/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author USER
 */
@Stateless
public class MyModuleAssessmentDesignFacade extends AbstractFacade<MyModuleAssessmentDesign> {

    //calling the child facade
    @EJB
    private MyAssessmentFacade myAssessmentFacade;

    @PersistenceContext(unitName = "APUAssessmentFeedbackSystem-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MyModuleAssessmentDesignFacade() {
        super(MyModuleAssessmentDesign.class);
    }
    
    //====== (Assessment review - AL)
    //to get the status (Assessment review - AL)
    public List<MyModuleAssessmentDesign> findByAL(String alUsername) {
        return em.createNamedQuery(
                "MyModuleAssessmentDesign.findByAL",
                MyModuleAssessmentDesign.class
        ).setParameter("alUsername", alUsername).getResultList();
    }

    public List<MyModuleAssessmentDesign> findByStatusAndAL(String status, String alUsername) {
        return em.createNamedQuery(
                "MyModuleAssessmentDesign.findByStatusAndAL",
                MyModuleAssessmentDesign.class
        ).setParameter("status", status)
                .setParameter("alUsername", alUsername)
                .getResultList();
    }

    //====== (FOR Design Assessment - lecturer)
    /*submit the assessment design (including parent and child) in only one transaction */
    public void submitAssessmentDesign(String moduleID, String moduleName, String lecturerUsername, String[] assessmentTypes, String[] weightages) {

        //creaating the parent record
        MyModuleAssessmentDesign design = new MyModuleAssessmentDesign();
        design.setDesignID(generateNextDesignID());
        design.setModuleID(moduleID);
        design.setModuleName(moduleName);
        design.setLecturerUsername(lecturerUsername);
        design.setStatus("PENDING");
        design.setFeedback(null);
        design.setSubmittedAt(new Timestamp(System.currentTimeMillis()));

        em.persist(design);

        //create the child record
        for (int i = 0; i < assessmentTypes.length; i++) {
            MyAssessment assessment = new MyAssessment();
            assessment.setAssessmentID(myAssessmentFacade.generateNextAssessmentID());
            assessment.setDesign(design);
            assessment.setAssessmentType(assessmentTypes[i]);
            assessment.setWeightage(Double.parseDouble(weightages[i]));

            // Use child facade (same transaction)
            myAssessmentFacade.create(assessment);
        }
    }

    private String generateNextDesignID() {
        String lastID = em.createQuery(
            "SELECT d.designID FROM MyModuleAssessmentDesign d ORDER BY d.designID DESC",
            String.class
        ).setMaxResults(1)
         .getResultStream()
         .findFirst()
         .orElse(null);

        int next = (lastID == null) ? 1
                : Integer.parseInt(lastID.substring(2)) + 1;

        return String.format("AD%03d", next);
    }

    
    //For Manage Assessment (Lecturer)
    public List<MyModuleAssessmentDesign>
    findByLecturerAndStatus(String lecturer, String status) {

        return em.createQuery(
            "SELECT d FROM MyModuleAssessmentDesign d " +
            "WHERE d.lecturerUsername = :lec " +
            "AND d.status = :status",
            MyModuleAssessmentDesign.class
        )
        .setParameter("lec", lecturer)
        .setParameter("status", status)
        .getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void resubmitAssessmentDesign(
            String designID,
            String[] assessmentTypes,
            String[] weightages) {

        MyModuleAssessmentDesign design = em.find(
            MyModuleAssessmentDesign.class, designID
        );

        //Reset parent
        design.setStatus("PENDING");
        design.setFeedback(null);
        design.setSubmittedAt(new Timestamp(System.currentTimeMillis()));

        //Delete old child records
        em.createQuery(
            "DELETE FROM MyAssessment a WHERE a.design = :design"
        ).setParameter("design", design)
         .executeUpdate();

        //Insert new child records
        for (int i = 0; i < assessmentTypes.length; i++) {
            MyAssessment a = new MyAssessment();
            a.setAssessmentID(myAssessmentFacade.generateNextAssessmentID());
            a.setDesign(design);
            a.setAssessmentType(assessmentTypes[i]);
            a.setWeightage(Double.parseDouble(weightages[i]));
            em.persist(a);
        }
    }
    
    //FOR LECTURER TO KEY IN MARKS
    public MyModuleAssessmentDesign
    findApprovedDesignByModuleID(String moduleID) {

        return em.createQuery(
            "SELECT d FROM MyModuleAssessmentDesign d " +
            "WHERE d.moduleID = :mid " +
            "AND d.status = 'APPROVED'",
            MyModuleAssessmentDesign.class
        )
        .setParameter("mid", moduleID)
        .getSingleResult();
    }
    
    public List<MyModuleAssessmentDesign> findByField(String field) {
        return em.createQuery(
                "SELECT d FROM MyModuleAssessmentDesign d WHERE d.moduleName IN " +
                "(SELECT m.moduleName FROM MyModule m WHERE m.field = :field)", 
                MyModuleAssessmentDesign.class)
                .setParameter("field", field)
                .getResultList();
    }   
}
