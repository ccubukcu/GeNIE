package com.genie.services;

import java.util.List;

import com.genie.model.Semester;
import com.genie.model.SemesterCourse;
import com.genie.utils.DaoUtil;

/**
 * @author ccubukcu
 *
 */
public class SemesterService {
	
	public static List<Semester> getAll() {
		return DaoUtil.getSemesterDAO().getAll();
	}
	
	public static List<Semester> getAllWithCourses() {
		return DaoUtil.getSemesterDAO().getAllWithCourses();
	}

	public static boolean updateSemester(Semester selectedSemester) {
		return DaoUtil.getSemesterDAO().update(selectedSemester);
	}

	public static boolean saveSemester(Semester selectedSemester) {
		return DaoUtil.getSemesterDAO().save(selectedSemester);
	}

	public static boolean deleteSemester(Semester selectedSemester) {
		return DaoUtil.getSemesterDAO().delete(selectedSemester);
	}

	public static List<Semester> getByYearId(Long yearId) {
		return DaoUtil.getSemesterDAO().getAllByYearId(yearId);
	}

	public static Semester getByIdWithYear(Long id) {
		return DaoUtil.getSemesterDAO().getByIdWithYear(id);
	}
	
	public static List<SemesterCourse> getCoursesBySemesterId(Long id) {
		return DaoUtil.getSemesterCourseDAO().getBySemesterId(id);
	}	

}
