// 1. Criamos a Classe (O Molde/Planta)
class Item {

    // ATRIBUTOS (Características que o objeto terá)
    private String nome;
    private int quantidade;

    // CONSTRUTOR (Como nós "nascemos" ou criamos o objeto)
    public Item(String nome, int quantidade) {

        this.nome = nome;
        this.quantidade = quantidade;

    }

    // MÉTODO (O que o objeto sabe fazer ou como interage)
    public String getNome() {

        return nome;

    }

    public int getQuantidade() {

        return quantidade;

    }

    // Método para exibir o item de forma bonita
    public void exibirItem() {

        System.out.println("-[" + quantidade + "] " + nome);

    }

}