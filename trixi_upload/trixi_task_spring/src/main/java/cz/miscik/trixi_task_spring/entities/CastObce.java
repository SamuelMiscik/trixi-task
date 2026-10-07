package cz.miscik.trixi_task_spring.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "cast_obce")
public class CastObce {

    @Id
    @Column(name = "kod", nullable = false)
    private Integer kod;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Many-to-one relationship with Obec, this is the owning side of the relationship
    @ManyToOne(optional = false)
    @JoinColumn(name = "kod_obce", nullable = false) // specifies the foreign key column in the cast_obce table
    private Obec obec;

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

    public Obec getObec() {
        return obec;
    }

    public void setObec(Obec obec) {
        this.obec = obec;
    }
}