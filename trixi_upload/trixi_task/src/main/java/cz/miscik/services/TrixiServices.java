package cz.miscik.services;

import cz.miscik.DAO.CastObceDAO;
import cz.miscik.DAO.ObecDAO;
import cz.miscik.entities.CastObce;
import cz.miscik.entities.Obec;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.ArrayList;

public class TrixiServices {

    private final EntityManager em;

    private final CastObceDAO castObceDAO;
    private final ObecDAO obecDAO;

    public TrixiServices(EntityManager em) {
        this.em = em;
        this.obecDAO = new ObecDAO(em);
        this.castObceDAO = new CastObceDAO(em);
    }

    public void importData(ArrayList<Obec> villages, ArrayList<CastObce> partsOfVillages)
    {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            for (Obec village : villages) {
                obecDAO.create(village);
            }

            for (CastObce partOfVillage : partsOfVillages) {
                castObceDAO.create(partOfVillage);
            }


            tx.commit();

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }

    }
}
