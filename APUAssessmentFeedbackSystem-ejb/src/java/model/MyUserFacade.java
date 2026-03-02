/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author USER
 */
@Stateless
public class MyUserFacade extends AbstractFacade<MyUser> {

    @PersistenceContext(unitName = "APUAssessmentFeedbackSystem-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MyUserFacade() {
        super(MyUser.class);
    }

    //======= Admin =======
    public boolean existsByUsername(String username) {
        return em.find(MyUser.class, username) != null;
    }

    public void addUser(MyUser user) {
        em.persist(user);
    }

    public void updateUser(MyUser user) {
        em.merge(user);
    }

    public void blockUser(String username) {
        MyUser user = em.find(MyUser.class, username);
        if (user != null) {
            user.setStatus("Blocked");
            em.merge(user);
        }
    }

    //admin get different field
    public List<String> findDistinctFields() {
        return em.createQuery(
                "SELECT DISTINCT u.field FROM MyUser u ORDER BY u.field",
                String.class
        ).getResultList();
    }

    // Get only active and unassigned lecturers
    public List<MyUser> findActiveLecturers() {
        return em.createQuery(
                "SELECT u FROM MyUser u "
                + "WHERE u.userType = 'Lecturer' AND u.status = 'Active' AND (u.assignedAcademicLeader IS NULL OR u.assignedAcademicLeader = 'NA')",
                MyUser.class
        ).getResultList();
    }

    // Get only active academic leaders
    public List<MyUser> findActiveAcademicLeaders() {
        return em.createQuery(
                "SELECT u FROM MyUser u "
                + "WHERE u.userType = 'Academic Leader' AND u.status = 'Active'",
                MyUser.class
        ).getResultList();
    }

    // Get lecturers with assigned leaders
    public List<MyUser> findActiveLecturersWithLeaders() {
        return em.createQuery(
                "SELECT u FROM MyUser u "
                + "WHERE u.userType = 'Lecturer' AND u.status = 'Active'",
                MyUser.class
        ).getResultList();
    }

    // Assign academic leader  
    public boolean assignAcademicLeader(String lecturerUsername, String leaderUsername) {
        try {
            MyUser lecturer = em.find(MyUser.class, lecturerUsername);
            if (lecturer == null) {
                return false;
            }

            if ("NA".equals(leaderUsername)) {
                lecturer.setAssignedAcademicLeader("NA");
                lecturer.setField("General");
                em.merge(lecturer);
                return true;
            }

            MyUser leader = em.createQuery(
                    "SELECT u FROM MyUser u WHERE u.username = :username",
                    MyUser.class
            ).setParameter("username", leaderUsername).getSingleResult();

            // Assign academic leader
            lecturer.setAssignedAcademicLeader(leader.getUsername());

            // Copy field from academic leader to lecturer
            lecturer.setField(leader.getField());

            // Save changes
            em.merge(lecturer);
            return true; // Success
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Something went wrong
        }
    }

    public List<MyUser> findActiveStudentsByField(String field) {
        return em.createQuery(
                "SELECT u FROM MyUser u WHERE u.userType = 'Student' AND u.status = 'Active' AND u.field = :field",
                MyUser.class)
                .setParameter("field", field)
                .getResultList();
    }

    // Report 1: Count by User Type
    public long countByUserType(String type) {
        return em.createQuery("SELECT COUNT(u) FROM MyUser u WHERE u.userType = :type", Long.class)
                .setParameter("type", type)
                .getSingleResult();
    }

    // Report 2: Count by Status
    public long countByStatus(String status) {
        return em.createQuery("SELECT COUNT(u) FROM MyUser u WHERE u.status = :status", Long.class)
                .setParameter("status", status)
                .getSingleResult();
    }

    // Report 3: Count by Gender
    public long countByGender(String gender) {
        return em.createQuery("SELECT COUNT(u) FROM MyUser u WHERE u.gender = :gender", Long.class)
                .setParameter("gender", gender)
                .getSingleResult();
    }

    public List<Object[]> countUserBreakdownByField() {
        return em.createQuery("SELECT u.field, u.userType, COUNT(u) FROM MyUser u GROUP BY u.field, u.userType")
                .getResultList();
    }

    public List<Object[]> countLecturersByLeader() {
        return em.createQuery("SELECT u.assignedAcademicLeader, COUNT(u) FROM MyUser u WHERE u.userType = 'Lecturer' GROUP BY u.assignedAcademicLeader")
                .getResultList();
    }

    public List<MyUser> findEligibleStudentsForModule(String field, String moduleID) {
        return em.createQuery(
            "SELECT u FROM MyUser u " +
            "WHERE u.userType = 'Student' " +
            "AND u.status = 'Active' " +
            "AND u.field = :field " +
            "AND u.username NOT IN (" +
            "   SELECT sc.studentName " +
            "   FROM MyStudentClass sc, MyClass c " +
            "   WHERE sc.classID = c.classID " +
            "   AND c.moduleID = :moduleID" +
            ")",
            MyUser.class
        )
        .setParameter("field", field)
        .setParameter("moduleID", moduleID)
        .getResultList();
    }
    
    //======= Academic Leader =======
    //for register and login page search name
    public MyUser searchUsername(String username) {
        Query q = em.createNamedQuery("MyUser.searchUsername");
        q.setParameter("input", username);
        List<MyUser> answer = q.getResultList();
        if (answer.size() > 0) {
            return answer.get(0);
        }
        return null;
    }

    //for AL create module get list of lecturers that assigned to them - for assigning in create module
    public List<MyUser> findLecturersByAcademicLeader(String academicLeaderUsername) {
        return em.createNamedQuery(
                "MyUser.findLecturersByAcademicLeader",
                MyUser.class
        )
                .setParameter("al", academicLeaderUsername)
                .getResultList();
    }
}
