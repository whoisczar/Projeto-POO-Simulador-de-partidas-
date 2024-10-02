package MODEL;

public class Jogador {
      
    public Jogador(int par, String nome_do_Jogador, double par2, String atacante, int par1, String brasileiro, String time_Exemplo) {
    }
    private int id;
    private String nome;
    private double altura; 
    private String nacionalidade;
    private int idade;
    private String posicao;
    private String timeNome;
    private int gols; 
    
    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }

    public String getTimeNome() {
        return timeNome;
    }

    public void setTimeNome(String timeNome) {
        this.timeNome = timeNome;
    }
    
    public int getGols() {
        return gols;
    }

    public void setGols(int gols) {
        this.gols = gols;
    }
}
