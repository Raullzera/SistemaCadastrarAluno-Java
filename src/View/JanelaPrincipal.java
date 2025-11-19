package View;

import DAO.AlunoDAO;
import model.Aluno;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class JanelaPrincipal extends JFrame {

    private JTextField campoNome, campoEmail, campoRua, campoCidade;
    private JComboBox<String> comboCurso;
    private JCheckBox checkEmail, checkNotificacao;
    private JRadioButton radioMasc, radioFem;
    private JButton btnCadastrar, btnLimpar, btnSair;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public JanelaPrincipal(){
        setTitle("Sistema de Cadastro de Alunos");
        setSize(500,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar barra = new JMenuBar();
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));
        menuArquivo.add(itemSair);

        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener(e -> JOptionPane.showMessageDialog(this, "Sistema de Cadastro de Alunos\nVersão 2.0"));
        menuAjuda.add(itemSobre);

        barra.add(menuArquivo);
        barra.add(menuAjuda);
        setJMenuBar(barra);

        JTabbedPane abas = new JTabbedPane();

        JPanel painelCadastro = new JPanel(new GridLayout(7,2));
        painelCadastro.add(new JLabel("Nome:"));
        campoNome = new JTextField(20);
        painelCadastro.add(campoNome);

        painelCadastro.add(new JLabel("Email:"));
        campoEmail = new JTextField(20);
        painelCadastro.add(campoEmail);

        painelCadastro.add(new JLabel("Curso:"));
        String[] cursos = {"Java" , "PHP", "C#", "JavaScript"};
        comboCurso = new JComboBox<>(cursos);
        painelCadastro.add(comboCurso);

        painelCadastro.add(new JLabel("Gênero:"));
        JPanel painelGenero = new JPanel();
        radioMasc = new JRadioButton("Masculino");
        radioFem = new JRadioButton("Feminino");
        ButtonGroup grupoGenero = new ButtonGroup();
        grupoGenero.add(radioMasc);
        grupoGenero.add(radioFem);
        painelGenero.add(radioMasc);
        painelGenero.add(radioFem);
        painelCadastro.add(painelGenero);

        checkEmail = new JCheckBox("Receber Emails");
        checkNotificacao = new JCheckBox("Ativar Notificações");
        painelCadastro.add(checkEmail);
        painelCadastro.add(checkNotificacao);


        painelCadastro.add(new JLabel("Rua:"));
        campoRua = new JTextField(20);
        painelCadastro.add(campoRua);

        painelCadastro.add(new JLabel("Cidade:"));
        campoCidade = new JTextField(20);
        painelCadastro.add(campoCidade);

        JPanel painelBotoes = new JPanel();
        btnCadastrar = new JButton("Cadastrar ");
        btnLimpar = new JButton("Limpar ");

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnLimpar);


       JPanel painelCadastroCompleto = new JPanel(new BorderLayout());
       painelCadastroCompleto.add(painelCadastro, BorderLayout.CENTER);
       painelCadastroCompleto.add(painelBotoes, BorderLayout.SOUTH);

        abas.add("Cadastro", painelCadastroCompleto);

        modeloTabela = new DefaultTableModel(new Object[]{"ID","Nome","Email","Curso","Cidade"},0);
        tabela = new JTable(modeloTabela);

        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir");
        JPanel painelListaBotoes = new JPanel();
        painelListaBotoes.add(btnAtualizar);
        painelListaBotoes.add(btnExcluir);

        JPanel painelLista = new JPanel(new BorderLayout());
        painelLista.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painelLista.add(painelListaBotoes, BorderLayout.SOUTH);

        abas.add("Lista de Alunos", painelLista);

        btnCadastrar.addActionListener(e -> cadastrarAluno());
        btnLimpar.addActionListener(e -> limparCampos());
        btnAtualizar.addActionListener(e -> atualizarAluno());
        btnExcluir.addActionListener(e -> excluirAluno());

        getContentPane().add(abas);

        carregarTabela();

        setVisible(true);
    }

    private void cadastrarAluno(){
        Aluno aluno = new Aluno();
        aluno.setNome(campoNome.getText());
        aluno.setEmail(campoEmail.getText());
        aluno.setCurso((String) comboCurso.getSelectedItem());
        aluno.setGenero(radioMasc.isSelected()? "Masculino" : "Feminino");
        aluno.setReceberEmail(checkEmail.isSelected());
        aluno.setReceberNotificacao(checkNotificacao.isSelected());
        aluno.setRua(campoRua.getText());
        aluno.setCidade(campoCidade.getText());

        new AlunoDAO().salvar(aluno);
        carregarTabela();
        limparCampos();
        JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso no banco de dados!!!");

    }

    private void carregarTabela(){
        modeloTabela.setRowCount(0);
        List<Aluno> lista = new AlunoDAO().listar();
        for (Aluno a : lista){
            modeloTabela.addRow(new Object[]{a.getId(), a.getNome(), a.getEmail(), a.getCurso(), a.getCidade()});
        }
    }

    private void atualizarAluno(){
        int linha = tabela.getSelectedRow();
            if (linha == -1){
                JOptionPane.showMessageDialog(this, "Selecione um aluno para atualizar");
                return;
            }

            int id = (int) tabela.getValueAt(linha, 0);
            String novoNome = JOptionPane.showInputDialog("Novo nome:", tabela.getValueAt(linha,1));
            String novoEmail = JOptionPane.showInputDialog("Novo email:", tabela.getValueAt(linha,2));

            Aluno aluno = new Aluno();
            aluno.setId(id);
            aluno.setNome(novoNome);
            aluno.setEmail(novoEmail);
            aluno.setCurso((String) tabela.getValueAt(linha, 3));
            aluno.setCidade((String) tabela.getValueAt(linha, 4));

            new AlunoDAO().atualizar(aluno);
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Aluno atualizado com sucesso");
    }

    private void excluirAluno(){
        int linha = tabela.getSelectedRow();
        if (linha == -1){
            JOptionPane.showMessageDialog(this, "Selecione um aluno para excluir");
            return;
        }
        int id = (int) tabela.getValueAt(linha, 0);
        new AlunoDAO().excluir(id);
        carregarTabela();
        JOptionPane.showMessageDialog(this, "Aluno excluido com sucesso");
    }

    private void limparCampos(){
        campoNome.setText("");
        campoEmail.setText("");
        comboCurso.setSelectedIndex(0);
        radioMasc.setSelected(false);
        radioFem.setSelected(false);
        checkEmail.setSelected(false);
        checkNotificacao.setSelected(false);
        campoRua.setText("");
        campoCidade.setText("");
    }

    public static void main(String [] args){
        new JanelaPrincipal();
    }
}
