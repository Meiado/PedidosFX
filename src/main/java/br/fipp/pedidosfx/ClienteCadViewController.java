package br.fipp.pedidosfx;

import br.fipp.pedidosfx.db.dals.ClienteDAL;
import br.fipp.pedidosfx.db.entidades.Cliente;
import br.fipp.pedidosfx.util.MaskFieldUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

import static br.fipp.pedidosfx.ClienteViewController.alterar;
import static br.fipp.pedidosfx.ClienteViewController.cliente;

public class ClienteCadViewController implements Initializable {
    public TextField tfDocumento;
    public TextField tfNome;
    public TextField tfEndereco;
    public TextField tfBairro;
    public TextField tfCidade;
    public TextField tfCep;
    public TextField tfUf;
    public TextField tfEmail;
    public TextField tfTelefone;
    private int id;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {tfDocumento.requestFocus();});
        MaskFieldUtil.cepField(tfCep);
        if(alterar && cliente!=null) {
            tfDocumento.setText(Long.toString(cliente.getDocumento()));
            tfNome.setText(cliente.getNome());
            tfEndereco.setText(cliente.getEndereco());
            tfBairro.setText(cliente.getBairro());
            tfCidade.setText(cliente.getCidade());
            tfCep.setText(cliente.getCep());
            tfUf.setText(cliente.getUf());
            tfEmail.setText(cliente.getEmail());
            tfTelefone.setText(cliente.getTelefone());
        }
    }
    public void onConfirmar(ActionEvent actionEvent) {
        ClienteDAL cliDAL = new ClienteDAL();
        Cliente c = new Cliente(
                Long.parseLong(tfDocumento.getText()),tfNome.getText(),tfEndereco.getText(),
                tfBairro.getText(),tfCidade.getText(),tfCep.getText(),tfUf.getText(),tfEmail.getText(),
                tfTelefone.getText()
        );
        if(alterar && cliente!=null) {
            c.setId(cliente.getId());
            System.out.println(cliDAL.alterar(c));
        }
        else
            System.out.println(cliDAL.gravar(c));
        ((Button)actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onBuscarCep(KeyEvent actionEvent) {
        if(tfCep.getText().length()==9){
            Task task = new Task<Void>() {
                @Override
                protected Void call() {
                    //coloque aqui o código para acessar a API e processar o JSON

                    String json = consultaCep(tfCep.getText(),"json");
                    JSONObject my_obj = new JSONObject(json);
                    tfEndereco.setText(my_obj.getString("logradouro"));
                    tfBairro.setText(my_obj.getString("bairro"));
                    tfCidade.setText(my_obj.getString("localidade"));
                    tfUf.setText(my_obj.getString("uf"));
                    return null;
                }
            };
            new Thread(task).start();

        }
    }
    public static String consultaCep(String cep, String formato)
    {
        StringBuffer dados = new StringBuffer();
        try {
            URL url = new URL("https://viacep.com.br/ws/"+ cep + "/"+formato+"/");
            URLConnection con = url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setAllowUserInteraction(false);
            InputStream in = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String s = "";
            while (null != (s = br.readLine()))
                dados.append(s);
            br.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return dados.toString();
    }


    public void onCancelar(ActionEvent actionEvent) {
        ((Button)actionEvent.getSource()).getScene().getWindow().hide();
    }


}
