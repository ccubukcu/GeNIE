package com.genie.dao;

import java.util.List;

import com.genie.model.AbstractBaseEntity;
import com.genie.model.Assignment;
import com.genie.model.AssignmentSubmission;

/**
 * @author ccubukcu
 *
 */
public interface AssignmentDAO {
	public List<Assignment> getAll();
	public Assignment getById(long id);

	public boolean save(AbstractBaseEntity entity);
	public boolean update(AbstractBaseEntity entity);
	public boolean delete(Assignment u);
	public List<Assignment> getAllBySemesterCourseId(Long semCourseId);
	public AssignmentSubmission getSubmissionWithDocument(String username, Long assignmentId);
	public boolean deleteSubmission(AssignmentSubmission submission);
}
