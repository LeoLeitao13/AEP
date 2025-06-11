package org.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {
    private TextField origemField;
    private TextField destinoField;
    private TextArea resultadoArea;
    private MapaGrafo grafo;

    @Override
    public void start(Stage primaryStage) {
        grafo = new MapaGrafo();

        origemField = new TextField();
        destinoField = new TextField();
        Button calcularRotaBtn = new Button("Calcular Rota Segura");
        resultadoArea = new TextArea();
        resultadoArea.setEditable(false);

        VBox mainLayout = new VBox(10);
        mainLayout.setStyle("-fx-padding: 20;");
        HBox inputLayout = new HBox(10);
        inputLayout.getChildren().addAll(new Label("Origem:"), origemField, new Label("Destino:"), destinoField);
        calcularRotaBtn.setOnAction(e -> calcularRota());
        mainLayout.getChildren().addAll(inputLayout, calcularRotaBtn, new Label("Rota calculada:"), resultadoArea);

        Scene scene = new Scene(mainLayout, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Rotas Seguras - Protótipo");
        primaryStage.show();

        adicionarDadosExemplo();
    }

    private void calcularRota() {
        String origem = origemField.getText();
        String destino = destinoField.getText();
        CalculadoraRotaSegura calculadora = new CalculadoraRotaSegura();
        var caminho = calculadora.encontrarRotaMaisSegura(grafo, origem, destino);
        if (caminho.isEmpty()) {
            resultadoArea.setText("Nenhuma rota encontrada.");
        } else {
            resultadoArea.setText(String.join(" -> ", caminho));
        }
    }

    private void adicionarDadosExemplo() {
        grafo.adicionarTrecho("A", "B", new Trecho("AB", 4.5, 3.0, 5.0));
        grafo.adicionarTrecho("B", "C", new Trecho("BC", 5.0, 4.0, 4.0));
        grafo.adicionarTrecho("A", "C", new Trecho("AC", 2.0, 2.0, 2.0));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
