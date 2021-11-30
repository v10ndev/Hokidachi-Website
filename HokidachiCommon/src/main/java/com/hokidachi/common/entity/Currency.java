package com.hokidachi.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="currencies")
public class Currency extends IdBasedEntity{

    @Column(length = 64, nullable = false)
    private String name;

    @Column(length = 3, nullable = false)
    private String symbol;

    @Column(length = 4, nullable = false)
    private String code;

    public Currency() {

    }

    public Currency(String name, String symbol, String code) {
        this.name = name;
        this.symbol = symbol;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return name + " - " + code + " - " + symbol;
    }
}