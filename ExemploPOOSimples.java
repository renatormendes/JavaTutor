public class ExemploPOOSimples {
    
    public static void main(String[] args) {

        System.out.println("--- Criando Objetos em Java --- \n");

        // Criando objetos reais (instanciação) a partir da classe 'Item'
        Item item1 = new Item("Café", 2); // Criamos o objeto item1
        Item item2 = new Item("Pão de Queijo", 10); // Criamos o objeto item2
        Item item3 = new Item("Leite", 3); // Criamos o objeto item3

        // Agora, em vez de uma lista de Strings, temos uma lista de OBJETOS 'Item'
        java.util.List<Item> carrinho = new java.util.ArrayList<>();

        // Adicionando os objetos na lista
        carrinho.add(item1);
        carrinho.add(item2);
        carrinho.add(item3);

        // Mostrando os dados usando o comportamento (método) do próprio objeto
        System.out.println("Sua lista de compras em POO:");
        for (Item produto : carrinho) {
            produto.exibirItem(); // Cada objeto sabe como se exibir!
        }
        System.out.println("\nÉ isso aí Pe-Pe-ssoAll! Boas compras!");
    }
}