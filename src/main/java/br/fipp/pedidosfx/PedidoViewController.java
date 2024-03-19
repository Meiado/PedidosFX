package br.fipp.pedidosfx;
import br.fipp.pedidosfx.db.dals.ClienteDAL;
import br.fipp.pedidosfx.db.dals.PedidoDAL;
import br.fipp.pedidosfx.db.entidades.Cliente;
import br.fipp.pedidosfx.db.entidades.Pedido;
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
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
public class PedidoViewController implements Initializable {
    public TableView<Pedido> tableView;
    public TableColumn<Pedido, Integer> colID;
    public TableColumn<Pedido, Date> colData;
    public TableColumn<Pedido, Double> colFrete;
    public TableColumn<Pedido, Double> colTotal;
    public TableColumn<Pedido, Cliente> colCliente;

    public static Pedido pedido;
    public static boolean visualizar;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colFrete.setCellValueFactory(new PropertyValueFactory<>("frete"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        preencherTabela("");
        visualizar=false;
    }

    private void preencherTabela(String filtro) {
        List<Pedido> pedidos = new PedidoDAL().get(filtro);
        tableView.setItems(FXCollections.observableArrayList(pedidos));
    }

    public void onVisualizar(ActionEvent actionEvent) throws IOException {
        visualizar = true;
        if(tableView.getSelectionModel().getSelectedIndex() >= 0) {
            pedido = tableView.getSelectionModel().getSelectedItem();
            abrirViewPedido();
        }
        visualizar = false;
    }

    private void abrirViewPedido() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pedido-info-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Pedidos");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void onApagar(ActionEvent actionEvent) {
        if (tableView.getSelectionModel().getSelectedIndex() >= 0) {
            PedidoDAL pedidoDAL = new PedidoDAL();
            System.out.println(pedidoDAL.apagar(tableView.getSelectionModel().getSelectedItem()));
            tableView.setItems(FXCollections.observableArrayList(new ArrayList<>()));
            preencherTabela("");
        }
    }

    public void onFechar(ActionEvent actionEvent) {
        ((Button) actionEvent.getSource()).getScene().getWindow().hide();
    }

}