package com.genie.dao.impl;

import java.util.List;

import com.genie.dao.DocumentDAO;
import com.genie.model.Document;

/**
 * @author ccubukcu
 *
 */
public class DocumentDAOImpl extends BaseDAOImpl implements DocumentDAO {

	private static final long serialVersionUID = -2968251466167808605L;

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> getAll() {
		List<Document> items = null;

		try {
			items = entityManager
					.createQuery("select a from Document a").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public Document getById(long id) {
		return entityManager.find(Document.class, id);
	}

	@Override
	public boolean delete(Document item) {
		item = entityManager.getReference(Document.class, item.getDocumentId());
		if (item == null)
			return false;
		item.setActive(false);
		update(item);
		return true;
	}

}
