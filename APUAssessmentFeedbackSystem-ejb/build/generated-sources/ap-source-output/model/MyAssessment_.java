package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.MyModuleAssessmentDesign;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2025-12-31T11:26:00")
@StaticMetamodel(MyAssessment.class)
public class MyAssessment_ { 

    public static volatile SingularAttribute<MyAssessment, Double> weightage;
    public static volatile SingularAttribute<MyAssessment, String> assessmentType;
    public static volatile SingularAttribute<MyAssessment, MyModuleAssessmentDesign> design;
    public static volatile SingularAttribute<MyAssessment, String> assessmentID;

}