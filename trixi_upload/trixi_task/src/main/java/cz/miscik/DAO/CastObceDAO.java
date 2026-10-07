package cz.miscik.DAO;

import cz.miscik.entities.CastObce;
import jakarta.persistence.EntityManager;

public class CastObceDAO extends DAO<CastObce>{

    public CastObceDAO(EntityManager em) {
        super(em, CastObce.class);
    }
}