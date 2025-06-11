package org.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.*;

public class MapaController extends Application {

    private final List<Avaliacao> avaliacoes = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        URL mapaURL = getClass().getResource("/MapaView.html");

        if (mapaURL == null) {
            System.err.println("❌ ERRO: Arquivo MapaView.html não encontrado em /resources");
            return;
        }

        System.out.println("✅ MapaView.html encontrado em: " + mapaURL.toExternalForm());
        engine.load(mapaURL.toExternalForm());

        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            System.out.println("📡 WebEngine state: " + newState);
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("javaConnector", new JavaBridge());
                System.out.println("✅ Conector JavaScript ativo");
            }
        });

        stage.setScene(new Scene(webView, 1000, 700));
        stage.setTitle("Mapa de Rotas com Barra Lateral");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public class JavaBridge {
        public void receberAvaliacao(String json) {
            System.out.println("📩 Avaliação recebida (JSON): " + json);
            try {
                Map<String, String> dados = parseJsonTexto(json);
                Avaliacao av = new Avaliacao(
                        dados.get("origem"),
                        dados.get("destino"),
                        Double.parseDouble(dados.get("seguranca")),
                        Double.parseDouble(dados.get("iluminacao")),
                        Double.parseDouble(dados.get("trafego"))
                );
                avaliacoes.add(av);
                System.out.println("✅ Salva: " + av);
            } catch (Exception e) {
                System.err.println("❌ Erro ao processar avaliação: " + e.getMessage());
            }
        }

        public void mostrarRelatorioEmJanela() {
            Platform.runLater(() -> {
                System.out.println("🔍 Criando nova janela de relatório");

                // Nova janela sempre que chamada
                Stage relatorioStage = new Stage();
                TextArea area = new TextArea();

                if (avaliacoes.isEmpty()) {
                    area.setText("Nenhuma avaliação recebida.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < avaliacoes.size(); i++) {
                        sb.append((i + 1)).append(". ").append(avaliacoes.get(i)).append("\n");
                    }
                    area.setText(sb.toString());
                }

                area.setEditable(false);
                Scene scene = new Scene(area, 500, 300);
                relatorioStage.setScene(scene);
                relatorioStage.setTitle("📋 Avaliações Recebidas");
                relatorioStage.show();
            });
        }

    }

    public static class Avaliacao {
        private final String origem;
        private final String destino;
        private final double seguranca;
        private final double iluminacao;
        private final double trafego;

        public Avaliacao(String origem, String destino, double seguranca, double iluminacao, double trafego) {
            this.origem = origem;
            this.destino = destino;
            this.seguranca = seguranca;
            this.iluminacao = iluminacao;
            this.trafego = trafego;
        }

        @Override
        public String toString() {
            return String.format("De %s → %s | Segurança: %.1f | Iluminação: %.1f | Tráfego: %.1f",
                    origem, destino, seguranca, iluminacao, trafego);
        }
    }

    private Map<String, String> parseJsonTexto(String json) {
        Map<String, String> map = new HashMap<>();
        json = json.replaceAll("[{}\"]", "");
        for (String pair : json.split(",")) {
            String[] parts = pair.split(":");
            map.put(parts[0].trim(), parts[1].trim());
        }
        return map;
    }
}
