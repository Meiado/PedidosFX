package br.fipp.pedidosfx.db.dals;

import br.fipp.pedidosfx.db.DBSingleton;
import br.fipp.pedidosfx.db.entidades.ItemPedido;
import br.fipp.pedidosfx.db.entidades.Pedido;
import br.fipp.pedidosfx.db.entidades.Produto;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemPedidoDAL {

    public boolean gravar(List<ItemPedido> itensPedidos) {
        if(DBSingleton.conectar()) {
            DecimalFormat df = new DecimalFormat("#.##");
            StringBuilder sql = new StringBuilder();
            for (ItemPedido item :
                        itensPedidos) {
                    sql.append(String.format("insert into itens_pedidos (itp_quant, itp_preco, pro_id, ped_id)" +
                                    "values (%d, %s, %d, %d); ",item.getQuantidade(), df.format(item.getPreco()),
                            item.getProduto().getId(), item.getPedido().getId()));
            }
            boolean resultado = DBSingleton.getConexao().manipular(sql.toString());
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
    public List<ItemPedido> get(String filtro) {
        String sql = "select * from itens_pedidos ";
        if(!filtro.isEmpty()){ sql += " where " + filtro; }
        ResultSet rs = DBSingleton.getConexao().consultar(sql);
        List<ItemPedido> itens_pedidos = new ArrayList<>();
        try {
            while(rs.next()) {
                int pro_id = rs.getInt("pro_id");
                int ped_id = rs.getInt("ped_id");
                Produto produto = new ProdutoDAL().get("").stream().filter(p -> p.getId() == pro_id).findFirst().orElse(null);
                Pedido pedido = new PedidoDAL().get("").stream().filter(p -> p.getId() == ped_id).findFirst().orElse(null);
                itens_pedidos.add(new ItemPedido(
                        rs.getInt("itp_id"),
                        rs.getInt("itp_quant"),
                        rs.getDouble("itp_preco"),
                        produto,
                        pedido
                ));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return itens_pedidos;
    }

    public boolean apagar(ItemPedido i) {
        ProdutoDAL produtoDAL = new ProdutoDAL();
        Produto p = produtoDAL.get(i.getProduto().getId());
        p.setEstoque(p.getEstoque()+i.getQuantidade());
        System.out.println("Produto: "+p+", estoque restaurado "+produtoDAL.alterar(p));
        return DBSingleton.getConexao().manipular("DELETE FROM itens_pedidos WHERE itp_id = "+i.getId());
    }
}
