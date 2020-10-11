package jogo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Tutorial - Conexão com o banco de dados em Java: https://www.youtube.com/watch?v=O4BdT0q-740 de Java Plugados
 * @author Bruno
 */
public class BancoDeDados {

    public Statement stm;   //Prepara e realiza pesquisas no banco de dados
    public ResultSet rs;    //Armazena o resultado de uma pesquisa passada para o statement
    private String driver = "org.postgresql.Driver";    //Identifica o serviço de banco de dados
    private String caminho = "jdbc:postgresql://localhost:5432/heliStaculos"; //Caminho para acessar o banco de dados - A última palavra é o nome do banco
    private String usuario = "postgres";
    private String senha = "post";
    public Connection conn; //Realiza a conexão com o banco de dados
    
    /**
     * Conecta-se com o banco de dados
     */
    public void conexao() { //Realiza a conexão com o banco
        try {   //Tenta conectar
            System.setProperty("jdbc.Drivers", driver); // Seta a propriedade do driver de conexão
            conn = DriverManager.getConnection(caminho, usuario, senha);    //Realiza a conexão com o banco de dados
        } catch (SQLException ex) { //Se der erro ele mandará uma mensagem com o erro
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nErro: " + ex);
        }
    }
    
    /**
     * Executa uma consulta no banco de dados. A resposta fica salva na variável "rs".
     * @param sql 
     */
    public void executaSQL(String sql){
        try {
            stm = conn.createStatement(rs.TYPE_SCROLL_INSENSITIVE,rs.CONCUR_READ_ONLY);
            rs = stm.executeQuery(sql);
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro na função: executaSQL.\nErro: "+ex);
        }
    }
    
    /**
     * Desconecta-se do banco de dados
     */
    public void desconecta() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao desconectar do banco de dados.");
        }
    }
}
