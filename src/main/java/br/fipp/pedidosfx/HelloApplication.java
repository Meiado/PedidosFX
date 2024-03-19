package br.fipp.pedidosfx;

import br.fipp.pedidosfx.db.DBSingleton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Pedidos FX");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {

        if(!DBSingleton.conectar())
        {
            JOptionPane.showMessageDialog(null,"Erro ao iniciar: "+
                    DBSingleton.getConexao().getMensagemErro());
            Platform.exit();
        }
        launch();
    }
}