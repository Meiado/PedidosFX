package br.fipp.pedidosfx;

import br.fipp.pedidosfx.db.dals.CategoriaDAL;
import br.fipp.pedidosfx.db.dals.ProdutoDAL;
import br.fipp.pedidosfx.db.entidades.Categoria;
import br.fipp.pedidosfx.db.entidades.Produto;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static br.fipp.pedidosfx.ProdutoViewController.produto;
import static br.fipp.pedidosfx.ProdutoViewController.alterar;

public class ProdutoCadViewController implements Initializable {
    public TextField tfNome;
    public TextField tfPreco;
    public TextField tfEstoque;
    public ChoiceBox<Categoria> listCategoria;
    private int id;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        preencherCategorias();
        if (alterar && produto!=null) {
            tfNome.setText(produto.getNome());
            tfPreco.setText(Double.toString(produto.getPreco()));
            tfEstoque.setText(Integer.toString(produto.getEstoque()));
            listCategoria.setValue(produto.getCategoria());
        }
    }
    private void preencherCategorias() {
        List<Categoria> categorias = new CategoriaDAL().get("");
        listCategoria.getItems().addAll(categorias);
    }

    public void onConfirmar(ActionEvent actionEvent) {
        ProdutoDAL prodDAL = new ProdutoDAL();
        Produto p = new Produto(
                tfNome.getText(), Double.parseDouble(tfPreco.getText()),
                Integer.parseInt(tfEstoque.getText()), listCategoria.getValue()
        );
        if(alterar && produto!=null) {
            p.setId(produto.getId());
            System.out.println(prodDAL.alterar(p));
        }
        else
            System.out.println(prodDAL.gravar(p));

        ((Button)actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onCancelar(ActionEvent actionEvent) {
        ((Button)actionEvent.getSource()).getScene().getWindow().hide();
    }

}