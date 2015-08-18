package com.genie.services;

import java.util.List;

import com.genie.model.SchoolYear;
import com.genie.model.Semester;
import com.genie.utils.DaoUtil;

/**
 * @author ccubukcu
 *
 */
public class SchoolYearService {
	
	public static List<SchoolYear> getAll() {
		return DaoUtil.getSchoolYearDAO().getAll();
	}
	
	public static List<SchoolYear> getAllWithSemesters() {
		return DaoUtil.getSchoolYearDAO().getAllWithSemesters();
	}
	
	public static boolean saveSchoolYear(SchoolYear newYear) {
		return DaoUtil.getSchoolYearDAO().save(newYear);
	}
	
	public static boolean updateSchoolYear(SchoolYear year) {
		return DaoUtil.getSchoolYearDAO().update(year);
	}
	
	public static boolean deleteSchoolYear(SchoolYear year) {
		return DaoUtil.getSchoolYearDAO().delete(year);
	}
	
	public static boolean isSemesterCreated(Long yearId, int semester) {
		List<Semester> semesters = SemesterService.getByYearId(yearId);
		for (Semester sem : semesters) {
			if(sem.getSemesterOrder() == semester) {
				return true;
			}
		}
		
		return false;
	}
}
