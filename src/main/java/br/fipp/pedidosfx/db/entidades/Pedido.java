package br.fipp.pedidosfx.db.entidades;

import java.time.Instant;
import java.util.Date;
public class Pedido {
    private int id;
    private Date data;
    private double frete;
    private double total;
    private Cliente cliente;

    public Pedido(int id, Date data, double frete, double total, Cliente cliente) {
        this.id = id;
        this.data = data;
        this.frete = frete;
        this.total = total;
        this.cliente = cliente;
    }
    public Pedido(int id) {
        this(id,Date.from(Instant.now()),0,0,null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getFrete() {
        return frete;
    }

    public void setFrete(double frete) {
        this.frete = frete;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public String toString() {
        return Integer.toString(this.id);
    }
}
