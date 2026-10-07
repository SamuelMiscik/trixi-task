package cz.miscik.DAO;

import cz.miscik.entities.Obec;
import jakarta.persistence.EntityManager;

public class ObecDAO extends DAO<Obec>{

    public ObecDAO(EntityManager em) {
        super(em, Obec.class);
    }
}
