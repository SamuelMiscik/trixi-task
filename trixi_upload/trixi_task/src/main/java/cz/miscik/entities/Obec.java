package cz.miscik.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "obec")
public class Obec {

    @Id
    @Column(name = "kod", nullable = false)
    private Integer kod;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // One-to-many relationship with CastObce, this is the inverse side of the relationship
    @OneToMany(mappedBy = "obec")
    private List<CastObce> castiObce = new ArrayList<>();

    //getters and setters
    public Integer getKod() {
        return kod;
    }

    public void setKod(Integer kod) {
        this.kod = kod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CastObce> getCastiObce() {
        return castiObce;
    }

    public void setCastiObce(List<CastObce> castiObce) {
        this.castiObce = castiObce;
    }
}