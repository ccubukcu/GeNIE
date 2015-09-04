package com.genie.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.genie.model.Assignment;
import com.genie.utils.DaoUtil;

@FacesConverter(value = "assignmentConverter")
public class AssignmentConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value == null || value.isEmpty())
			return null;
		return DaoUtil.getAssignmentDAO().getById(new Long(value));
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object o) {
		if (o.getClass().equals(String.class)) {
			return "";
		}
		if (o.getClass().equals(Long.class)) {
			return DaoUtil.getAssignmentDAO().getById((long) o).getId().toString();
		}
		return ((Assignment) o).getId().toString();
	}
}
