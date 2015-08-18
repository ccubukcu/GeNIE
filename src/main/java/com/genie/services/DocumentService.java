package com.genie.services;

import com.genie.model.Document;
import com.genie.utils.DaoUtil;

public class DocumentService {
	
	public static void saveDocument(Document doc) {
		DaoUtil.getDocumentDAO().save(doc);
	}

	public static void delete(Document doc) {
		DaoUtil.getDocumentDAO().delete(doc);
	}
}
