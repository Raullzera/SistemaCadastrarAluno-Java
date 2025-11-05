package DAO;

import model.Aluno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AlunoDAO {
    private Connection conn;

    public AlunoDAO(){
        conn = Conexao.getConnection();
    }

    public void salvar(Aluno aluno) {
        String sql = "INSERT INTO aluno (nome, email, curso, genero, receberEmail, receberNotificacao, rua, cidade)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getEmail());
            stmt.setString(3, aluno.getCurso());
            stmt.setString(4, aluno.getGenero());
            stmt.setBoolean(5, aluno.isReceberEmail());
            stmt.setBoolean(6, aluno.isReceberNotificacao());
            stmt.setString(7, aluno.getRua());
            stmt.setString(8, aluno.getCidade());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
