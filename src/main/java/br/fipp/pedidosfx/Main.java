package br.fipp.pedidosfx;

import br.fipp.pedidosfx.db.DBSingleton;
import br.fipp.pedidosfx.db.dals.*;
import br.fipp.pedidosfx.db.entidades.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        DBSingleton.conectar();
//        List<Categoria> categorias = new CategoriaDAL().get("");
//        for(Categoria c : categorias)
//            System.out.println(c.getNome());
//        List<Produto> produtos = new ProdutoDAL().get("");
//        for(Produto p : produtos)
//            System.out.println(p.getNome());
//        List<Pedido> pedidos = new PedidoDAL().get("");
//        for(Pedido p : pedidos)
//            System.out.println(p.getCliente().getNome() + " " + p.getTotal());
//        List<ItemPedido> itens_pedidos = new ItemPedidoDAL().get(1);
//        for(ItemPedido itp : itens_pedidos)
//            System.out.println(itp.getProduto().getNome()+" "+itp.getPreco()+" "+itp.getQuantidade());
    }
}

