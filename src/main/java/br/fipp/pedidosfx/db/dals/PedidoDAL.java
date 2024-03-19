package br.fipp.pedidosfx.db.dals;

import br.fipp.pedidosfx.db.DBSingleton;
import br.fipp.pedidosfx.db.entidades.Categoria;
import br.fipp.pedidosfx.db.entidades.Cliente;
import br.fipp.pedidosfx.db.entidades.ItemPedido;
import br.fipp.pedidosfx.db.entidades.Pedido;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAL {
    public boolean gravar(Pedido pedido) {
        if(DBSingleton.conectar()) {
            DecimalFormat df = new DecimalFormat("#.##");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String sql = String.format("insert into pedidos (ped_data, ped_frete, ped_total, cli_id)" +
                    " values ('%s', %s, %s, %d)", sdf.format(pedido.getData()), df.format(pedido.getFrete()),
                    df.format(pedido.getTotal()), pedido.getCliente().getId());
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

    public boolean apagar(Pedido entidade) {
        ItemPedidoDAL itemPedidoDAL = new ItemPedidoDAL();
        List<ItemPedido> itemPedidos = itemPedidoDAL.get("ped_id ="+entidade.getId());
        ProdutoDAL produtoDAL = new ProdutoDAL();
        if(!itemPedidos.isEmpty())
            for (ItemPedido i :
                    itemPedidos) {
                System.out.println("Item: "+i.getProduto()+", "+itemPedidoDAL.apagar(i));
                //Restaurar estoque
                i.getProduto().setEstoque(i.getProduto().getEstoque()+i.getQuantidade());
                produtoDAL.alterar(i.getProduto());
            }
        return DBSingleton.getConexao().manipular("DELETE FROM pedidos WHERE ped_id="+entidade.getId());
    }
    public Pedido get(int id) {
        String sql = "select * from pedidos where ped_id = " + id;
        ResultSet rs = DBSingleton.getConexao().consultar(sql);
        try {
            if(rs.next()) {
                int cli_id = rs.getInt("cli_id");
                ClienteDAL clienteDAL = new ClienteDAL();
                Cliente cliente = clienteDAL.get("").stream().filter(c -> c.getId() == cli_id).findFirst().orElse(null);
                return new Pedido(
                        rs.getInt("ped_id"),
                        rs.getDate("ped_data"),
                        rs.getDouble("ped_frete"),
                        rs.getDouble("ped_total"),
                        cliente
                );
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    public List<Pedido> get(String filtro) {
      String sql = "select * from pedidos ";
      if(!filtro.isEmpty()){ sql += " where " + filtro; }

      ResultSet rs = DBSingleton.getConexao().consultar(sql);
      List<Pedido> pedidos = new ArrayList<>();
      try {
          while(rs.next()) {
              int cli_id = rs.getInt("cli_id");
              ClienteDAL clienteDAL = new ClienteDAL();
              Cliente cliente = clienteDAL.get("").stream().filter(c -> c.getId() == cli_id).findFirst().orElse(null);
              pedidos.add(new Pedido(
                      rs.getInt("ped_id"),
                      rs.getDate("ped_data"),
                      rs.getDouble("ped_frete"),
                      rs.getDouble("ped_total"),
                      cliente
              ));
          }

      } catch (Exception e) {
          System.out.println(e);
      }
      return pedidos;
    }
}

