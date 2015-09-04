package com.genie.dao;

import java.util.List;

import com.genie.model.AbstractBaseEntity;
import com.genie.model.Assignment;
import com.genie.model.AssignmentSubmission;
import com.genie.model.CoursePlan;
import com.genie.model.GradeCriteria;

/**
 * @author ccubukcu
 *
 */
public interface CoursePlanDAO {
	public List<CoursePlan> getAll();
	public CoursePlan getById(long id);

	public boolean save(AbstractBaseEntity entity);
	public boolean update(AbstractBaseEntity entity);
	public boolean delete(CoursePlan u);
	public List<CoursePlan> getAllByYearAndSemester(Long schoolYearId, Long semesterId);
	public CoursePlan getByIdWithSemesterAndCourse(Long id);
	public List<CoursePlan> getAllPlansBySemesterCourseId(Long id);
	public List<CoursePlan> getAllPlansBySemesterCourseIdWithMaterials(Long id);
	public List<GradeCriteria> getGradeCriteriaByTypeAndSemesterCourse(int type, Long semesterCourseId);
	public List<Assignment> getAllAssignmentsBySemesterCourse(Long id);
	public GradeCriteria getCriteriaById(Long id);
	public List<CoursePlan> getAllPlansBySemesterCourseIdWithMaterialDocuments(Long id);
	public List<GradeCriteria> getGradeCriteriaBySemesterCourse(Long semesterCourseId);
	public List<AssignmentSubmission> getAllSubmissionsByAssignmentId(Long id);
}
