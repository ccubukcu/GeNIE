package com.genie.utils;

import org.springframework.security.authentication.AuthenticationManager;

import com.genie.beans.SessionBean;
import com.genie.dao.AssignmentDAO;
import com.genie.dao.AuditLogDAO;
import com.genie.dao.AuthorityDAO;
import com.genie.dao.CourseDAO;
import com.genie.dao.CourseMaterialDAO;
import com.genie.dao.CoursePlanDAO;
import com.genie.dao.DocumentDAO;
import com.genie.dao.GamificationDAO;
import com.genie.dao.SchoolYearDAO;
import com.genie.dao.SemesterCourseDAO;
import com.genie.dao.SemesterDAO;
import com.genie.dao.SystemPropertiesDAO;
import com.genie.dao.UserDAO;
import com.genie.dao.impl.BaseDAOImpl;

/**
*
* @author ccubukcu
*/
public class DaoUtil {
	
	public static BaseDAOImpl getBaseDAO() {
		return (BaseDAOImpl) JsfUtil.getBean("baseDAO");
	}
	
	public static SessionBean getSessionBean() {
        return (SessionBean) JsfUtil.getBean("sessionBean");
    }
	
	public static UserDAO getUserDAO() {
		return (UserDAO) JsfUtil.getBean("userDAO");
	}
	
	public static CourseDAO getCourseDAO() {
		return (CourseDAO) JsfUtil.getBean("courseDAO");
	}
	
	public static CourseMaterialDAO getCourseMaterialDAO() {
		return (CourseMaterialDAO) JsfUtil.getBean("courseMaterialDAO");
	}
	
	public static DocumentDAO getDocumentDAO() {
		return (DocumentDAO) JsfUtil.getBean("documentDAO");
	}
	
	public static SchoolYearDAO getSchoolYearDAO() {
		return (SchoolYearDAO) JsfUtil.getBean("schoolYearDAO");
	}
	
	public static CoursePlanDAO getCoursePlanDAO() {
		return (CoursePlanDAO) JsfUtil.getBean("coursePlanDAO");
	}
	
	public static SemesterDAO getSemesterDAO() {
		return (SemesterDAO) JsfUtil.getBean("semesterDAO");
	}
	
	public static SemesterCourseDAO getSemesterCourseDAO() {
		return (SemesterCourseDAO) JsfUtil.getBean("semesterCourseDAO");
	}
	
	public static AssignmentDAO getAssignmentDAO() {
		return (AssignmentDAO) JsfUtil.getBean("assignmentDAO");
	}
	
	public static AuditLogDAO getAuditLogDAO() {
		return (AuditLogDAO) JsfUtil.getBean("auditLogDAO");
	}
	
	public static AuthorityDAO getAuthorityDAO() {
		return (AuthorityDAO) JsfUtil.getBean("authorityDAO");
	}
	
	public static SystemPropertiesDAO getSystemPropertiesDAO() {
		return (SystemPropertiesDAO) JsfUtil.getBean("systemPropertiesDAO");
	}
	
	public static GamificationDAO getGamificationDAO() {
		return (GamificationDAO) JsfUtil.getBean("gamificationDAO");
	}
	
	public static AuthenticationManager getAuthenticationManager() {
		return (AuthenticationManager) JsfUtil.getBean("authenticationManager");
	}
}
