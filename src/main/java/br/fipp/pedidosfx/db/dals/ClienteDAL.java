package br.fipp.pedidosfx.db.dals;

import br.fipp.pedidosfx.db.DBSingleton;
import br.fipp.pedidosfx.db.entidades.Cliente;
import br.fipp.pedidosfx.db.entidades.Pedido;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAL{

    public boolean gravar(Cliente cliente) {
        if (DBSingleton.conectar()) {
            String sql = String.format(
                    "INSERT INTO clientes(cli_documento, cli_nome," +
                            " cli_endereco,cli_bairro, cli_cidade, cli_cep," +
                            " cli_uf, cli_email, cli_telefone) VALUES " +
                            "('%d','%s', '%s','%s', '%s','%s', '%s','%s','%s')",
                    cliente.getDocumento(), cliente.getNome(), cliente.getEndereco(),
                    cliente.getBairro(), cliente.getCidade(), cliente.getCep(),
                    cliente.getUf(), cliente.getEmail(), cliente.getTelefone()
            );

            boolean resultado = DBSingleton.getConexao().manipular(sql);
            String mensagemErro = DBSingleton.getConexao().getMensagemErro();

            if (resultado) {
                System.out.println("Operação realizada com sucesso");
                return resultado;
            }
            else {
                System.out.println("Erro na operação: " + mensagemErro);
                return false;
            }
        }
        else {
            System.out.println("Erro na conexão");
            return false;
        }

    }
    public boolean alterar(Cliente cliente) {
        String sql = String.format(
                "UPDATE clientes SET cli_documento=%d, cli_nome='%s', cli_endereco='%s'," +
                        " cli_bairro='%s', cli_cidade='%s', cli_cep='%s', cli_uf='%s', cli_email='%s'," +
                        " cli_telefone='%s' WHERE cli_id=%d",
                cliente.getDocumento(), cliente.getNome(), cliente.getEndereco(), cliente.getBairro(),
                cliente.getCidade(), cliente.getCep(), cliente.getUf(), cliente.getEmail(),
                cliente.getTelefone(), cliente.getId());
        return DBSingleton.getConexao().manipular(sql);
    }
    public boolean apagar(Cliente cliente) {
        PedidoDAL pedidoDAL = new PedidoDAL();
        List<Pedido> pedidos = pedidoDAL.get("cli_id = "+cliente.getId());
        if(!pedidos.isEmpty()) {
            JOptionPane.showMessageDialog(null,"Erro ao excluir: há pedidos registrados com o cliente.");
            return false;
        }
        return DBSingleton.getConexao().manipular("DELETE FROM clientes WHERE cli_id="+cliente.getId());
    }

    public Cliente get(int id) {
        String sql = "select * from clientes where cli_id ="+id;
        ResultSet rs = DBSingleton.getConexao().consultar(sql);
        try {
            if(rs.next())
                return new Cliente(
                        rs.getInt("cli_id"),
                        rs.getLong("cli_documento"),
                        rs.getString("cli_nome"),
                        rs.getString("cli_endereco"),
                        rs.getString("cli_bairro"),
                        rs.getString("cli_cidade"),
                        rs.getString("cli_cep"),
                        rs.getString("cli_uf"),
                        rs.getString("cli_email"),
                        rs.getString("cli_telefone")
                );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Cliente();
    }
    public List<Cliente> get(String filtro) {
        String sql = "select * from clientes ";
        if(!filtro.isEmpty()){ sql += " where " + filtro; }

        ResultSet rs = DBSingleton.getConexao().consultar(sql);
        List<Cliente> clientes = new ArrayList<>();
        try {
            while (rs.next()) {
                clientes.add(new Cliente(
                        rs.getInt("cli_id"),
                        rs.getLong("cli_documento"),
                        rs.getString("cli_nome"),
                        rs.getString("cli_endereco"),
                        rs.getString("cli_bairro"),
                        rs.getString("cli_cidade"),
                        rs.getString("cli_cep"),
                        rs.getString("cli_uf"),
                        rs.getString("cli_email"),
                        rs.getString("cli_telefone")
                ));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return clientes;
    }
}
