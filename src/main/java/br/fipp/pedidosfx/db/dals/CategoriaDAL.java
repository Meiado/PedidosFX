package br.fipp.pedidosfx.db.dals;

import br.fipp.pedidosfx.db.DBSingleton;
import br.fipp.pedidosfx.db.entidades.Categoria;
import br.fipp.pedidosfx.db.entidades.Produto;

import javax.swing.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAL {

    public boolean gravar(Categoria entidade) {
        if (DBSingleton.conectar()) {
            String sql = String.format("INSERT INTO categorias(cat_nome, cat_desc) VALUES ('%s', '%s')",
                    entidade.getNome(), entidade.getDescricao());

            boolean resultado = DBSingleton.getConexao().manipular(sql);
            String mensagemErro = DBSingleton.getConexao().getMensagemErro();

            if (resultado) {
                System.out.println("Operação realizada com sucesso");
                return resultado;
            } else {
                System.out.println("Erro na operação: " + mensagemErro);
                return false;
            }
        } else {
            System.out.println("Erro na conexão");
            return false;
        }
    }

    public boolean alterar(Categoria entidade) {
        String sql=String.format("UPDATE categorias SET cat_nome='%s', cat_desc='%s' WHERE cat_id=%d)",
                entidade.getNome(),entidade.getDescricao(),entidade.getId());
        return DBSingleton.getConexao().manipular(sql);
    }

    public boolean apagar(Categoria entidade) {
        ProdutoDAL produtoDAL = new ProdutoDAL();
        List<Produto> produtos =  produtoDAL.get("cat_id = "+entidade.getId());
        if(!produtos.isEmpty()) {
            JOptionPane.showMessageDialog(null,"Erro ao excluir: há produtos registrados na categoria.");
            return false;
        }
        return DBSingleton.getConexao().manipular("DELETE FROM categorias WHERE cat_id="+entidade.getId());
    }

    public Categoria get(int id) {
        String sql = "select * from categorias WHERE cat_id = " + id;
        ResultSet rs= DBSingleton.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                return new Categoria(rs.getInt("cat_id"), rs.getString("cat_nome"), rs.getString("cat_desc"));
            }
        } catch(Exception e) { System.out.println(e); }
        return null;
    }


    public List<Categoria> get(String filtro){
        String sql = "select * from categorias ";
        if(!filtro.isEmpty()){ sql += " where " + filtro; }

        ResultSet rs = DBSingleton.getConexao().consultar(sql);
        List<Categoria> categorias = new ArrayList<>();
        try {
            while(rs.next()) {
                categorias.add(new Categoria(
                    rs.getInt("cat_id"),
                    rs.getString("cat_nome"),
                    rs.getString("cat_desc")
                ));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return categorias;
    }
}
