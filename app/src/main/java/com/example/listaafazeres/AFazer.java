package com.example.listaafazeres;

import java.util.Random;
import java.util.UUID;

public class AFazer
{
    private String nome;
    private boolean finalizado;
    private double valorItem;
    private String id;
    private int quantidade;

    public AFazer() {
        this.id = String.valueOf(UUID.randomUUID());
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public double getValorItem() {
        return valorItem;
    }

    public void setValorItem(double valorItem) {
        this.valorItem = valorItem;
    }

    public String getId() {
        return id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
