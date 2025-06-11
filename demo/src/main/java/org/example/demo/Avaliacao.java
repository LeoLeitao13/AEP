package org.example.demo;

public class Avaliacao {
    private final double seguranca;
    private final double iluminacao;
    private final double trafego;

    public Avaliacao(double seguranca, double iluminacao, double trafego) {
        this.seguranca = seguranca;
        this.iluminacao = iluminacao;
        this.trafego = trafego;
    }

    @Override
    public String toString() {
        return "Segurança: " + seguranca + ", Iluminação: " + iluminacao + ", Tráfego: " + trafego;
    }
}

