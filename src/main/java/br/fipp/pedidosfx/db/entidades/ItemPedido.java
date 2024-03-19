package br.fipp.pedidosfx.db.entidades;

public class ItemPedido {
    private int id;
    private int quantidade;
    private double preco;
    private Produto produto;
    private Pedido pedido;

    public ItemPedido(int id, int quantidade, double preco, Produto produto, Pedido pedido) {
        this.id = id;
        this.quantidade = quantidade;
        this.preco = preco;
        this.produto = produto;
        this.pedido = pedido;
    }

    public ItemPedido(ItemPedido itp) {
        this.id = itp.getId();
        this.quantidade = itp.getQuantidade();
        this.preco = itp.getPreco();
        this.produto = itp.getProduto();
        this.pedido = itp.getPedido();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
