package com.hokidachi.common.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "cities")
public class City extends IdBasedEntity{

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 5, nullable = false)
    private String code;

    @OneToMany(mappedBy = "city")
    private Set<District> districts;

    public City() {

    }

    public City(Integer id) {
        this.id = id;
    }

    public City(String name) {
        this.name = name;
    }

    public City(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public City(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "City [id=" + id + ", name=" + name + ", code=" + code + "]";
    }

}