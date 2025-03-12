package com.exemplo.ingressos.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 500)
    private String descricao;

    @ElementCollection
    @CollectionTable(name = "datas_evento", joinColumns = @JoinColumn(name = "evento_id"))
    @Column(name = "data")
    private List<LocalDate> datas;

    @Column(nullable = false)
    private String local;

    @Column(nullable = false)
    private Integer capacidade;

    @Column(nullable = false)
    private String categoria;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<LocalDate> getDatas() { return datas; }
    public void setDatas(List<LocalDate> datas) { this.datas = this.datas; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public Integer getCapacidade() { return capacidade; }
    public void setCapacidade(Integer capacidade) { this.capacidade = capacidade; }
}
