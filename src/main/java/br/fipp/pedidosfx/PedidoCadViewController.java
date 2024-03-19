package br.fipp.pedidosfx;

import br.fipp.pedidosfx.db.DBSingleton;
import br.fipp.pedidosfx.db.dals.*;
import br.fipp.pedidosfx.db.entidades.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static br.fipp.pedidosfx.PedidoViewController.pedido;
import static br.fipp.pedidosfx.PedidoViewController.visualizar;

public class PedidoCadViewController implements Initializable {
    public TextField tfPesquisa;
    public TableView <Cliente> tvCliente;
    public TableColumn <Cliente, Integer> colID;
    public TableColumn <Cliente, String> colNome;
    public TableColumn <Cliente, String> colCidade;
    public TableColumn <Cliente, String> colTelefone;
    public Text txNome;
    public Text txDoc;
    public Text txEnd;
    public Text txEmail;
    public ChoiceBox <Categoria> cbCategoria;
    public ChoiceBox <Produto> cbProduto;
    public Text txEstoque;
    public TextField tfQuant;
    public TableView <ItemPedido> tvItens;
    public TableColumn <ItemPedido, Integer> colQuant;
    public TableColumn <ItemPedido, Produto> colProd;
    public TableColumn <ItemPedido, Double> colValor;
    public TextField tfFrete;
    public Text txTotal;
    private Pedido novoPedido;
    private List<Produto> produtos;
    private List<ItemPedido> itens;
    private double frete;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarPedido();
        inicializarTabelaItens();
        if (visualizar && pedido!=null) {
            tfFrete.setText(Double.toString(pedido.getFrete()));
            txTotal.setText(Double.toString(pedido.getTotal()));
            txNome.setText(pedido.getCliente().getNome());
            txDoc.setText(Long.toString(pedido.getCliente().getDocumento()));
            txEnd.setText(pedido.getCliente().getEndereco());
            txEmail.setText(pedido.getCliente().getEmail());
            carregarItensPedido(pedido.getId());
        }
        else{
            inicializarTabelaCliente();
            carregarClientes("");
            tvCliente.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    txNome.setText(tvCliente.getSelectionModel().getSelectedItem().getNome());
                    txDoc.setText(Long.toString(tvCliente.getSelectionModel().getSelectedItem().getDocumento()));
                    txEnd.setText(tvCliente.getSelectionModel().getSelectedItem().getEndereco());
                    txEmail.setText(tvCliente.getSelectionModel().getSelectedItem().getEmail());
                }
            });
            carregarCategorias();
            cbCategoria.setOnAction(this::carregarProdutos);
            cbProduto.setOnAction(this::mostrarEstoque);
        }
    }

    private void carregarItensPedido(int id) {
        List<ItemPedido> itens = new ItemPedidoDAL().get("ped_id ="+id);
        tvItens.setItems(FXCollections.observableArrayList(itens));
    }
    private void inicializarTabelaItens() {
        colProd.setCellValueFactory(new PropertyValueFactory<>("produto"));
        colQuant.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("preco"));
    }
    private void inicializarPedido() {
        if(!visualizar) {
            novoPedido = new Pedido(0);
            frete = 0;
            produtos = new ProdutoDAL().get("");
        }
        itens = new ArrayList<>();
    }

    private void inicializarTabelaCliente() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
    }

    private void carregarClientes(String filtro) {
        List<Cliente> clientes = new ClienteDAL().get(filtro);
        tvCliente.setItems(FXCollections.observableArrayList(clientes));
    }

    private void carregarCategorias() {
        List<Categoria> categorias = new CategoriaDAL().get("");
        cbCategoria.getItems().addAll(categorias);
    }

    private void carregarProdutos(ActionEvent actionEvent) {
        txEstoque.setText("0");
        cbProduto.getItems().removeAll();
        cbProduto.setItems(FXCollections.observableArrayList(new ArrayList<>()));
        List<Produto> produtos = new ProdutoDAL().get("cat_id = "+cbCategoria.getValue().getId()+ " and pro_estoque > 0;");
        cbProduto.getItems().addAll(produtos);
    }
    private void mostrarEstoque(ActionEvent actionEvent) {
        if(cbProduto.getValue() != null)
            txEstoque.setText(Integer.toString(produtos.get(getIndex(cbProduto.getValue())).getEstoque()));
    }
    public void onPesquisar(KeyEvent keyEvent) {
        String filtro = tfPesquisa.getText();
        carregarClientes("upper(cli_nome) like '%"+filtro.toUpperCase()+"%'");
    }

    private int getIndex(Produto produto) {
        for (Produto p:
             produtos) {
            if(p.getId() == produto.getId())
                return produtos.indexOf(p);
        }
        return -1;
    }
    public void onAdicionarItem(ActionEvent actionEvent) {
       if(cbProduto.getValue() == null)
           JOptionPane.showMessageDialog(null,"Selecione ao menos 1 produto.");
       else if (tfQuant.getText().isBlank()) {
           JOptionPane.showMessageDialog(null,"Informe a quantidade desejada.");
       }
       else if (produtos.get(getIndex(cbProduto.getValue())).getEstoque() < Integer.parseInt(tfQuant.getText())) {
           JOptionPane.showMessageDialog(null,"Produto indisponível na quantidade desejada.");
       }
       else {
           boolean flag = false;
           for (int i = 0; i < tvItens.getItems().size() && !flag; i++) {
               tvItens.getSelectionModel().select(i);
               if (tvItens.getSelectionModel().getSelectedItem().getProduto().getId() == cbProduto.getValue().getId()) {
                   ItemPedido itp = new ItemPedido(tvItens.getSelectionModel().getSelectedItem());
                   itens.remove(tvItens.getSelectionModel().getSelectedItem());
                   itp.setQuantidade(itp.getQuantidade() + Integer.parseInt(tfQuant.getText()));
                   itens.add(itp);
                   tvItens.setItems(FXCollections.observableArrayList(new ArrayList<>()));
                   tvItens.getItems().addAll(itens);
                   flag = true;
               }
           }
           if (!flag) {
               itens.add(new ItemPedido(0, Integer.parseInt(tfQuant.getText()),
                       cbProduto.getValue().getPreco(), cbProduto.getValue(), novoPedido));
               tvItens.setItems(FXCollections.observableArrayList(new ArrayList<>()));
               tvItens.getItems().addAll(itens);
           }
           atualizarTotal();
           produtos.get(getIndex(cbProduto.getValue())).setEstoque(produtos.get(getIndex(cbProduto.getValue())).getEstoque() - Integer.parseInt(tfQuant.getText()));
           txEstoque.setText(Integer.toString(produtos.get(getIndex(cbProduto.getValue())).getEstoque()));
       }
    }
    private void atualizarTotal() {
        double total = 0;
        for (ItemPedido itp :
                itens)
            total += itp.getQuantidade() * itp.getPreco();
        if(frete > 0)
            total += frete;
        txTotal.setText(String.format("%.2f",total));
    }

    public void onRemover(ActionEvent actionEvent) {
        Produto aux = produtos.get(getIndex(tvItens.getSelectionModel().getSelectedItem().getProduto()));
        aux.setEstoque(aux.getEstoque() + tvItens.getSelectionModel().getSelectedItem().getQuantidade());
        itens.remove(tvItens.getSelectionModel().getSelectedItem());
        tvItens.setItems(FXCollections.observableArrayList(new ArrayList<>()));
        tvItens.getItems().addAll(itens);
        atualizarTotal();
        if(cbProduto.getValue().getId() == aux.getId())
            txEstoque.setText(Integer.toString(aux.getEstoque()));
    }

    public void onAdicionarFrete(ActionEvent actionEvent) {
        if(!tfFrete.getText().isBlank())
            frete = Double.parseDouble(tfFrete.getText());
        else
            frete = 0;
        atualizarTotal();
    }

    public void onCancelar(ActionEvent actionEvent) {
        ((Button)actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onGravar(ActionEvent actionEvent) {
        Cliente cliente = tvCliente.getSelectionModel().getSelectedItem();
        if(cliente == null)
            JOptionPane.showMessageDialog(null,"Erro ao gravar: Cliente não selecionado");
        else if (itens.isEmpty()) {
            JOptionPane.showMessageDialog(null,"Erro ao gravar: Não há itens no novoPedido");
        }
        else {
            int id = DBSingleton.getConexao().getMaxPK("pedidos","ped_id")+1;
            novoPedido.setId(id);
            novoPedido.setCliente(cliente);
            novoPedido.setFrete(frete);
            String totalText = txTotal.getText();
            totalText = totalText.replace(",", ".");
            double total = Double.parseDouble(totalText);
            novoPedido.setTotal(total);
            PedidoDAL pedidoDAL = new PedidoDAL();
            if(pedidoDAL.gravar(novoPedido)) {
                ItemPedidoDAL itemPedidoDAL = new ItemPedidoDAL();
                if(itemPedidoDAL.gravar(itens)) {
                    JOptionPane.showMessageDialog(null, "Operação bem sucedida: Pedido registrado completamente.");
                    atualizarEstoque();
                    ((Button)actionEvent.getSource()).getScene().getWindow().hide();
                }
                else {
                    pedidoDAL.apagar(novoPedido);
                    JOptionPane.showMessageDialog(null,"Falha na operação: não foi possível registrar os itens.");
                }
            }
            else
                JOptionPane.showMessageDialog(null,"Falha na operação: não foi possível registrar o novoPedido.");
        }
    }
    private void atualizarEstoque() {
        ProdutoDAL produtoDAL = new ProdutoDAL();
        Produto p;
        for (ItemPedido itp:
             itens) {
            p = produtos.get(getIndex(itp.getProduto()));
            System.out.println("Atualização estoque "+p+" " + produtoDAL.alterar(p) );
        }
    }

}
