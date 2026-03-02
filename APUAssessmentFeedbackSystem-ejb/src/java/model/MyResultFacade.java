/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.dto.FinalResultDTO;
import model.dto.StudentResultDTO;

/**
 *
 * @author USER
 */
@Stateless
public class MyResultFacade extends AbstractFacade<MyResult> {

    @PersistenceContext(unitName = "APUAssessmentFeedbackSystem-ejbPU")
    private EntityManager em;

    @EJB
    private MyGradingSystemFacade gradingFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MyResultFacade() {
        super(MyResult.class);
    }

    //Find existing result
    public MyResult findByStudentAndAssessment(
            String studentName,
            String assessmentID) {

        List<MyResult> list = em.createQuery(
                "SELECT r FROM MyResult r "
                + "WHERE r.studentName = :sname "
                + "AND r.assessmentID = :aid",
                MyResult.class
        )
                .setParameter("sname", studentName)
                .setParameter("aid", assessmentID)
                .getResultList();

        return list.isEmpty() ? null : list.get(0);
    }

    //Save or update marks
    public void saveOrUpdate(String studentName,
            String assessmentID,
            double marks,
            String feedback) {

        MyResult r
                = findByStudentAndAssessment(studentName, assessmentID);

        if (r == null) {
            r = new MyResult();
            r.setMarksID(generateNextMarksID());
            r.setStudentName(studentName);
            r.setAssessmentID(assessmentID);
            r.setMarks(marks);
            r.setFeedback(feedback);
            em.persist(r);
        } else {
            r.setMarks(marks);
            r.setFeedback(feedback);
            em.merge(r);
        }
    }

    //Generate the result id 
    public String generateNextMarksID() {

        String lastID = em.createQuery(
                "SELECT r.marksID FROM MyResult r ORDER BY r.marksID DESC",
                String.class
        )
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null);

        int next = (lastID == null)
                ? 1
                : Integer.parseInt(lastID.substring(2)) + 1;

        return String.format("RS%03d", next);
    }

    // FOR STUDENT
    public List<StudentResultDTO> getStudentResults(String studentUsername) {

        List<StudentResultDTO> dtoList = new ArrayList<>();

        List<Object[]> results = em.createQuery(
                "SELECT d.moduleID, a.assessmentType, r.marks, a.weightage, r.feedback "
                + "FROM MyResult r "
                + "JOIN MyAssessment a ON r.assessmentID = a.assessmentID "
                + "JOIN MyModuleAssessmentDesign d ON a.design.designID = d.designID "
                + "WHERE r.studentName = :username",
                Object[].class
        )
                .setParameter("username", studentUsername)
                .getResultList();

        for (Object[] row : results) {

            String moduleID = (String) row[0];

            // Always get latest module name
            MyModule module = em.find(MyModule.class, moduleID);
            if (module == null) {
                continue; // safety guard
            }

            StudentResultDTO dto = new StudentResultDTO();
            dto.setModuleName(module.getModuleName());
            dto.setAssessmentType((String) row[1]);
            dto.setMarks(((Number) row[2]).doubleValue());
            dto.setWeightage(((Number) row[3]).doubleValue());
            dto.setFeedback((String) row[4]);

            dtoList.add(dto);
        }
        return dtoList;
    }

    public List<FinalResultDTO> getFinalModuleResults(String studentUsername) {

        List<FinalResultDTO> finalResults = new ArrayList<>();

        // 1. Only approved designs
        List<MyModuleAssessmentDesign> designs = em.createQuery(
                "SELECT DISTINCT d FROM MyModuleAssessmentDesign d "
                + "LEFT JOIN FETCH d.assessments "
                + "WHERE d.status = 'APPROVED'",
                MyModuleAssessmentDesign.class
        ).getResultList();

        for (MyModuleAssessmentDesign design : designs) {

            double totalMarks = 0;
            boolean allAssessmentsMarked = true;

            List<MyAssessment> assessments = design.getAssessments();
            if (assessments == null || assessments.isEmpty()) {
                continue;
            }

            for (MyAssessment assessment : assessments) {
                try {
                    MyResult result = em.createQuery(
                            "SELECT r FROM MyResult r "
                            + "WHERE r.assessmentID = :aid "
                            + "AND r.studentName = :student",
                            MyResult.class
                    )
                            .setParameter("aid", assessment.getAssessmentID())
                            .setParameter("student", studentUsername)
                            .getSingleResult();

                    // Lecturer keyed weighted marks, need to add up to form final marks
                    totalMarks += result.getMarks();

                } catch (Exception e) {
                    allAssessmentsMarked = false;
                    break;
                }
            }

            // Only show complete modules
            if (allAssessmentsMarked) {
                // Get latest module name via moduleID
                MyModule module = em.find(MyModule.class, design.getModuleID());
                if (module == null) {
                    continue; // safety check
                }
                FinalResultDTO dto = new FinalResultDTO();
                dto.setModuleName(module.getModuleName());

                // Round for cleanliness
                double roundedTotal = Math.round(totalMarks * 100.0) / 100.0;
                dto.setTotalMarks(roundedTotal);

                // Grade lookup
                String grade = gradingFacade.findGradeByMark(roundedTotal);
                dto.setFinalGrade(grade);
                dto.setIsComplete(true);

                finalResults.add(dto);
            }
        }

        return finalResults;
    }

}
