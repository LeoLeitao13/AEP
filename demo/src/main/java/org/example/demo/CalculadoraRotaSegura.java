package org.example.demo;

import java.util.*;


public class CalculadoraRotaSegura {
    public List<String> encontrarRotaMaisSegura(MapaGrafo grafo, String inicio, String fim) {
        Map<String, Double> distancias = new HashMap<>();
        Map<String, String> anteriores = new HashMap<>();
        PriorityQueue<String> fila = new PriorityQueue<>(Comparator.comparingDouble(distancias::get).reversed());

        for (String vertice : grafo.getAdjacencias().keySet()) {
            distancias.put(vertice, Double.NEGATIVE_INFINITY);
        }
        distancias.put(inicio, 0.0);
        fila.add(inicio);

        while (!fila.isEmpty()) {
            String atual = fila.poll();
            for (Conexao conexao : grafo.getAdjacencias().getOrDefault(atual, new ArrayList<>())) {
                double novaNota = distancias.get(atual) + conexao.getTrecho().getNotaMedia();
                if (novaNota > distancias.getOrDefault(conexao.getDestino(), Double.NEGATIVE_INFINITY)) {
                    distancias.put(conexao.getDestino(), novaNota);
                    anteriores.put(conexao.getDestino(), atual);
                    fila.add(conexao.getDestino());
                }
            }
        }

        List<String> caminho = new ArrayList<>();
        for (String v = fim; v != null; v = anteriores.get(v)) {
            caminho.add(0, v);
        }

        if (caminho.size() == 1 && !caminho.get(0).equals(inicio)) {
            return new ArrayList<>();
        }

        return caminho;
    }
}
