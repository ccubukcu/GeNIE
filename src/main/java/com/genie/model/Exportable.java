package com.genie.model;

import java.util.List;

/**
 * @author ccubukcu
 *
 */
public interface Exportable {
	public List<String> getAsCellData();
	public boolean isExportable();
}
