package br.fipp.pedidosfx.db;

public class DBSingleton {
    private static Conexao conexao;

    private DBSingleton() {}

    public static boolean conectar()
    {
        conexao=new Conexao();
        return conexao.conectar(
                "jdbc:postgresql://isabelle.db.elephantsql.com/",
                "wounadai",
                "wounadai",
                "cgVQ95lSubjJvtw9RIr_mrCRSku2vfU7");
    }
    public static Conexao getConexao() {
        return conexao;
    }
}
