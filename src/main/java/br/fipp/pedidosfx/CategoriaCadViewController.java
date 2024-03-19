package br.fipp.pedidosfx;

import br.fipp.pedidosfx.db.dals.CategoriaDAL;
import br.fipp.pedidosfx.db.entidades.Categoria;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static br.fipp.pedidosfx.CategoriaViewController.alterar;
import static br.fipp.pedidosfx.CategoriaViewController.categoria;

public class CategoriaCadViewController implements Initializable {
    public TextField tfNome;
    public TextField tfDescricao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(alterar && categoria != null) {
            tfNome.setText(categoria.getNome());
            tfDescricao.setText(categoria.getDescricao());
        }
    }

    public void onConfirmar(ActionEvent actionEvent) {
        Categoria c = new Categoria(tfNome.getText(), tfDescricao.getText());
        CategoriaDAL catDAL = new CategoriaDAL();
        if(alterar && categoria!=null) {
            c.setId(categoria.getId());
            System.out.println(catDAL.alterar(c));
        }
        else
            System.out.println(catDAL.gravar(c));
        ((Button)actionEvent.getSource()).getScene().getWindow().hide();

    }

    public void onCancelar(ActionEvent actionEvent) {
        ((Button)actionEvent.getSource()).getScene().getWindow().hide();
    }

}
