package model;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.MyAssessment;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2025-12-31T11:26:00")
@StaticMetamodel(MyModuleAssessmentDesign.class)
public class MyModuleAssessmentDesign_ { 

    public static volatile SingularAttribute<MyModuleAssessmentDesign, String> feedback;
    public static volatile ListAttribute<MyModuleAssessmentDesign, MyAssessment> assessments;
    public static volatile SingularAttribute<MyModuleAssessmentDesign, String> lecturerUsername;
    public static volatile SingularAttribute<MyModuleAssessmentDesign, String> moduleName;
    public static volatile SingularAttribute<MyModuleAssessmentDesign, String> designID;
    public static volatile SingularAttribute<MyModuleAssessmentDesign, String> moduleID;
    public static volatile SingularAttribute<MyModuleAssessmentDesign, Timestamp> submittedAt;
    public static volatile SingularAttribute<MyModuleAssessmentDesign, String> status;

}