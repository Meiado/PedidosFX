package br.fipp.pedidosfx.db.dals;

import br.fipp.pedidosfx.db.DBSingleton;
import br.fipp.pedidosfx.db.entidades.Categoria;
import br.fipp.pedidosfx.db.entidades.ItemPedido;
import br.fipp.pedidosfx.db.entidades.Produto;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAL{

    public boolean gravar(Produto produto) {
        if (DBSingleton.conectar()) {
            String sql= String.format("insert into produtos (pro_nome, pro_preco, pro_estoque, cat_id) values ('%s', %s, %d, %d)",
                    produto.getNome(),Double.toString(produto.getPreco()).replace(",","."), produto.getEstoque(), produto.getCategoria().getId());

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


    public boolean alterar(Produto produto) {

        String sql=String.format("UPDATE produtos SET pro_nome='%s', pro_preco=%s," +
                        " pro_estoque=%d, cat_id=%d WHERE pro_id=%d",
                produto.getNome(),Double.toString(produto.getPreco()).replace(",","."),produto.getEstoque(),
                produto.getCategoria().getId(),produto.getId());
        return DBSingleton.getConexao().manipular(sql);
    }


    public boolean apagar(Produto produto) {
        ItemPedidoDAL itemPedidoDAL = new ItemPedidoDAL();
        List<ItemPedido> itemPedidos = itemPedidoDAL.get("");
        for (ItemPedido i :
                itemPedidos) {
            if (i.getProduto().getId() == produto.getId())
            {
                JOptionPane.showMessageDialog(null,"Erro ao excluir: há pedidos registrados com o produto.");
                return false;
            }
        }
        return DBSingleton.getConexao().manipular("DELETE FROM produtos WHERE cat_id="+produto.getId());
    }

    public Produto get(int id) {
        String sql = "select * from produtos where pro_id = " + id;
        ResultSet rs = DBSingleton.getConexao().consultar(sql);
        try {
            if(rs.next()) {
                int cat_id = rs.getInt("cat_id");
                CategoriaDAL catDAL = new CategoriaDAL();
                Categoria categoria = catDAL.get("").stream().filter(c -> c.getId() == cat_id).
                        findFirst().orElse(null);
                return new Produto(
                        rs.getInt("pro_id"), rs.getString("pro_nome"),
                        rs.getDouble("pro_preco"), rs.getInt("pro_estoque"),
                        categoria
                        );
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    public List<Produto> get(String filtro) {
        String sql =  "select * from produtos ";
        if(!filtro.isEmpty()){ sql += " where " + filtro; }

        ResultSet rs = DBSingleton.getConexao().consultar(sql);
        List<Produto> produtos = new ArrayList<>();
        try {
            while(rs.next()) {
                int cat_id = rs.getInt("cat_id");
                CategoriaDAL catDAL = new CategoriaDAL();
                Categoria categoria = catDAL.get("").stream().filter(c -> c.getId() == cat_id).findFirst().orElse(null);
                produtos.add(new Produto(
                        rs.getInt("pro_id"),
                        rs.getString("pro_nome"),
                        rs.getDouble("pro_preco"),
                        rs.getInt("pro_estoque"),
                        categoria
                ));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return produtos;
    }
}
