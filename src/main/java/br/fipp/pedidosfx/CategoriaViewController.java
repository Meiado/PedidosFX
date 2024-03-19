package br.fipp.pedidosfx;

import br.fipp.pedidosfx.db.dals.CategoriaDAL;
import br.fipp.pedidosfx.db.dals.ProdutoDAL;
import br.fipp.pedidosfx.db.entidades.Categoria;
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


public class CategoriaViewController implements Initializable {
    public TextField tfPesquisa;
    public TableView <Categoria> tableView;
    public TableColumn<Categoria, Integer> colID;
    public TableColumn<Categoria, String> colNome;
    public TableColumn<Categoria, String> colDescricao;

    public static Categoria categoria;
    public static boolean alterar;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        preencherTabela("");
        alterar = false;
    }

    private void preencherTabela(String filtro) {
        List<Categoria> categorias = new CategoriaDAL().get(filtro);
        tableView.setItems(FXCollections.observableArrayList(categorias));
    }
    public void onPesquisar(KeyEvent keyEvent) {
        String filtro = tfPesquisa.getText();
        preencherTabela("upper(cat_nome) like '%"+filtro.toUpperCase()+"%'");
    }

    public void onNovaCategoria(ActionEvent actionEvent) throws IOException {
        abrirCadCategoria();
    }

    public void onAlterar(ActionEvent actionEvent) throws IOException {
        alterar = true;
        if(tableView.getSelectionModel().getSelectedIndex()>=0) {
            categoria = tableView.getSelectionModel().getSelectedItem();
            abrirCadCategoria();
            preencherTabela("");
        }
        alterar = false;
    }

    public void onApagar(ActionEvent actionEvent) {
        if(tableView.getSelectionModel().getSelectedIndex()>=0) {
            CategoriaDAL categoriaDAL = new CategoriaDAL();
            System.out.println(categoriaDAL.apagar(tableView.getSelectionModel().getSelectedItem()));
            tableView.setItems(FXCollections.observableArrayList(new ArrayList<>()));
            preencherTabela("");
        }
    }

    public void onFechar(ActionEvent actionEvent) {
        ((Button)actionEvent.getSource()).getScene().getWindow().hide();

    }
    private void abrirCadCategoria() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("categoria-cad-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage=new Stage();
        stage.setTitle("Categorias");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
