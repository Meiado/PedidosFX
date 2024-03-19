package br.fipp.pedidosfx;

import br.fipp.pedidosfx.db.dals.ClienteDAL;
import br.fipp.pedidosfx.db.dals.ProdutoDAL;
import br.fipp.pedidosfx.db.entidades.Produto;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class  ProdutoViewController implements Initializable {
    public TextField tfPesquisa;
    public TableView <Produto> tableView;
    public TableColumn <Produto, Integer> colID;
    public TableColumn <Produto, String> colNome;
    public TableColumn <Produto, Double> colPreco;
    public TableColumn <Produto, Integer> colEstoque;
    public TableColumn <Produto, String> colCategoria;
    public static Produto produto;

    public static boolean alterar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colEstoque.setCellValueFactory(new PropertyValueFactory<>("estoque"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        preencherTabela("");
        alterar = false;
    }

    private void preencherTabela(String filtro) {
        List<Produto> produtos = new ProdutoDAL().get(filtro);
        tableView.setItems(FXCollections.observableArrayList(produtos));
    }
    public void onNovoProduto(ActionEvent actionEvent) throws IOException {
        abrirCadProduto();
    }

    public void onPesquisar(KeyEvent keyEvent) {
        String filtro = tfPesquisa.getText();
        preencherTabela("upper(pro_nome) like '%"+filtro.toUpperCase()+"%'");
    }

    public void onAlterar(ActionEvent actionEvent) throws IOException {
        alterar = true;
        if(tableView.getSelectionModel().getSelectedIndex()>=0) {
            produto = tableView.getSelectionModel().getSelectedItem();
            abrirCadProduto();
            preencherTabela("");
        }
        alterar = false;
    }

    public void onApagar(ActionEvent actionEvent) {
        if(tableView.getSelectionModel().getSelectedIndex()>=0) {
            ProdutoDAL produtoDAL = new ProdutoDAL();
            System.out.println(produtoDAL.apagar(tableView.getSelectionModel().getSelectedItem()));
            tableView.setItems(FXCollections.observableArrayList(new ArrayList<>()));
            preencherTabela("");
        }
    }

    public void onFechar(ActionEvent actionEvent) {
        ((Button)actionEvent.getSource()).getScene().getWindow().hide();
    }
    private void abrirCadProduto() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("produto-cad-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage=new Stage();
        stage.setTitle("Produtos");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
