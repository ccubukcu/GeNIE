package com.genie.beans.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.genie.beans.BaseBean;
import com.genie.model.Authority;
import com.genie.model.Course;
import com.genie.model.Semester;
import com.genie.model.SemesterCourse;
import com.genie.model.User;
import com.genie.security.Role;
import com.genie.services.SemesterService;
import com.genie.services.UserService;
import com.genie.utils.ResourceUtil;

/**
 * @author ccubukcu
 *
 */
@ManagedBean
@ViewScoped
public class UserManagementBean extends BaseBean {
	private static final long serialVersionUID = -7485927705663512089L;

	private List<User> users;
	private User selectedUser;
	private List<Authority> userAuthorities;
	private Authority selectedAuthority;
	
	private List<Semester> semesters;
	private Semester selectedSemester;
	private List<Course> semesterCourses;
	private Course selectedCourse;
	private List<SelectItem> roleSelectItems;
	private String selectedRole;
	
	private boolean updatingRole;
	private boolean semesterSelected;
	private boolean roleSelected;
	private boolean courseSelected;
	private boolean courseAndSemesterRequired;
	
	@PostConstruct
	public void initBean() {
		refreshUsers();
		
		roleSelectItems = Role.getAsSelectItems();
	}
	
	public void refreshUsers() {
		users = UserService.getAllUsersWithAuthorities();
	}
	
	public void updateUser() {
		try {
			selectedUser.setAuthorities(userAuthorities);
			UserService.updateUserWithAuthority(selectedUser);
			updateSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			saveOrUpdateFailed();
		}
		clearSelectedUser();
		refreshUsers();
	}
	
	public void deleteUser() {
		try {
			UserService.deleteUser(selectedUser);
			refreshUsers();
			deleteSucessful();
		} catch (Exception e) {
			e.printStackTrace();
			operationFailed();
		}
	}
	
	public void clearSelectedUser() {
		selectedUser = null;
		userAuthorities = null;
		selectedAuthority = null;
	}
	
	public void prepareUserUpdateDialog() {
		refreshSemesters();
		refreshUserAuthorities();
	}
	
	public void refreshUserAuthorities() {
		userAuthorities = UserService.getAuthoritiesForUserWithCourseAndSemester(selectedUser.getUsername());
	}
	
	public void refreshSemesters() {
		semesters = SemesterService.getAll();
	}
	
	public void onSemesterSelected() {
		if(selectedSemester != null) {
			semesterCourses = new ArrayList<Course>();
			
			List<SemesterCourse> scList = SemesterService.getCoursesBySemesterId(selectedSemester.getId());
			
			if(scList != null && scList.size() > 0)
				for (SemesterCourse sc : scList) {
					semesterCourses.add(sc.getCourse());
				}
			semesterSelected = true;
		}
	}
	
	public void onCourseSelected() {
		courseSelected = true;
	}
	
	public boolean isUserButtonsDisabled() {
		if(selectedUser == null || (selectedUser != null && selectedUser.getUsername() == null)) {
			return true;
		}
		return false;
	}
	
	public boolean isAuthorityButtonsRendered() {
		if(selectedAuthority == null || (selectedAuthority != null && selectedAuthority.getUsername() == null)) {
			return true;
		}
		return false;
	}
	
	public int getAuthoritiesCount(User u) {
		if(u.getAuthorities() == null) return 0;
		else return u.getAuthorities().size();
	}
	
	public void onAuthoritySelected() {
		if(selectedAuthority != null) {

			selectedSemester = selectedAuthority.getSemester();
			selectedCourse = selectedAuthority.getCourse();
			selectedRole = selectedAuthority.getAuthority();
			
			onRoleSelected();
			
			if(courseAndSemesterRequired) {
				courseSelected = true;
				semesterSelected = true;
			}
			
			updatingRole = true;
		}
	}
	
	public void onAuthorityUnselected() {
		selectedAuthority = null;
		selectedSemester = null;
		selectedCourse = null;
		selectedRole = null;
		
		roleSelected = false;
		semesterSelected = false;
		courseSelected = false;
		updatingRole = false;
	}
	
	public void onRoleSelected() {
		if(selectedRole != null && !selectedRole.isEmpty()) {
				if(selectedRole.equals(Role.ROLE_ADMINISTRATOR.toString()) || selectedRole.equals(Role.ROLE_INSTRUCTOR.toString())) {
				courseAndSemesterRequired = false;
			} else {
				courseAndSemesterRequired = true;
			}
			roleSelected = true;
		}
	}
	
	public void addOrUpdateRole() {
		try {
			if(!updatingRole)
				selectedAuthority = new Authority();

			selectedAuthority.setAuthority(selectedRole);
			
			if(!selectedRole.equals(Role.ROLE_ADMINISTRATOR.toString()) && !selectedRole.equals(Role.ROLE_INSTRUCTOR.toString())) {
				selectedAuthority.setSemesterId(selectedSemester.getId());
				selectedAuthority.setCourseId(selectedCourse.getId());
			}
			
			if(updatingRole) {
				UserService.updateAuthority(selectedAuthority);
				updateSuccessful();
			} else {
				selectedAuthority.setUsername(selectedUser.getUsername());
				UserService.saveAuthority(selectedAuthority);
				saveSuccessful();
			}
			
			onAuthorityUnselected();
		} catch (Exception e) {
			e.printStackTrace();
			saveOrUpdateFailed();
		}
		refreshUserAuthorities();
	}
	
	public void deleteRole() {
		try {
			UserService.deleteAuthority(selectedAuthority);
			refreshUserAuthorities();
			onAuthorityUnselected();
			deleteSucessful();
		} catch (Exception e) {
			e.printStackTrace();
			operationFailed();
		}
	}
	
	public String getAuthorityButtonLabel() {
		return updatingRole ? ResourceUtil.getLabel("general.labels.edit") : ResourceUtil.getLabel("general.labels.add");
	}
	
	public String getDialogButtonClass() {
		String baseClass = "submit-input grad-btn ln-tr";
		if(updatingRole) {
			baseClass += " edit-button";
		} else {
			baseClass += " add-button";
		}
		
		return baseClass;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public List<Authority> getUserAuthorities() {
		return userAuthorities;
	}

	public void setUserAuthorities(List<Authority> userAuthorities) {
		this.userAuthorities = userAuthorities;
	}

	public Authority getSelectedAuthority() {
		return selectedAuthority;
	}

	public void setSelectedAuthority(Authority selectedAuthority) {
		this.selectedAuthority = selectedAuthority;
	}

	public List<Semester> getSemesters() {
		return semesters;
	}

	public void setSemesters(List<Semester> semesters) {
		this.semesters = semesters;
	}

	public Semester getSelectedSemester() {
		return selectedSemester;
	}

	public void setSelectedSemester(Semester selectedSemester) {
		this.selectedSemester = selectedSemester;
	}

	public List<Course> getSemesterCourses() {
		return semesterCourses;
	}

	public void setSemesterCourses(List<Course> semesterCourses) {
		this.semesterCourses = semesterCourses;
	}

	public Course getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(Course selectedCourse) {
		this.selectedCourse = selectedCourse;
	}

	public List<SelectItem> getRoleSelectItems() {
		return roleSelectItems;
	}

	public void setRoleSelectItems(List<SelectItem> roleSelectItems) {
		this.roleSelectItems = roleSelectItems;
	}

	public String getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(String selectedRole) {
		this.selectedRole = selectedRole;
	}

	public boolean isUpdatingRole() {
		return updatingRole;
	}

	public void setUpdatingRole(boolean updatingRole) {
		this.updatingRole = updatingRole;
	}

	public boolean isCourseAndSemesterRequired() {
		return courseAndSemesterRequired;
	}

	public void setCourseAndSemesterRequired(boolean courseAndSemesterRequired) {
		this.courseAndSemesterRequired = courseAndSemesterRequired;
	}

	public boolean isSemesterSelected() {
		return semesterSelected;
	}

	public void setSemesterSelected(boolean semesterSelected) {
		this.semesterSelected = semesterSelected;
	}

	public boolean isRoleSelected() {
		return roleSelected;
	}

	public void setRoleSelected(boolean roleSelected) {
		this.roleSelected = roleSelected;
	}

	public boolean isCourseSelected() {
		return courseSelected;
	}

	public void setCourseSelected(boolean courseSelected) {
		this.courseSelected = courseSelected;
	}
}
