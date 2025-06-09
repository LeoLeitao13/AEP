package org.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.net.URL;

public class MapaController extends Application {

    @Override
    public void start(Stage stage) {
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        // Tentar carregar o HTML do mapa
        URL mapaURL = getClass().getResource("/MapaView.html");

        if (mapaURL == null) {
            System.err.println("❌ ERRO: Arquivo MapaView.html não encontrado em /resources");
            return;
        }

        System.out.println("✅ MapaView.html encontrado em: " + mapaURL.toExternalForm());
        engine.load(mapaURL.toExternalForm());

        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            System.out.println("📡 WebEngine state: " + newState);
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
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

    public static class JavaBridge {
        public void receberAvaliacao(String json) {
            System.out.println("📩 Avaliação recebida: " + json);
        }
    }
}
