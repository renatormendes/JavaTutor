import java.util.ArrayList; // Importamos o ArrayList (a implementação da lista)
import java.util.List; // importamos a interface List

public class ExemploLista {
    
    public static void main(String[] args) {

        System.out.println("--- Preparando a nossa Lista de Compras ---\n");
                
        // 1. Criando a lista.
        // O <String> (Generic) diz que essa lista SÓ aceita textos.
        List<String> listaDeCompras = new ArrayList<>();

        // 2. Adicionando itens na lista usando o método .add()
        listaDeCompras.add("Café");
        listaDeCompras.add("Pão de Queijo");
        listaDeCompras.add("Leite");
        listaDeCompras.add("Chocolate");

        // 3. Verificando o tamanho da lista usando o método size().
        System.out.println("Temos: " + listaDeCompras.size() + " itens na lista.");

        // 4. Mostrando todos os itens da lista	
        System.out.println("Itens atuais: " + listaDeCompras);

        // 5. Pegando um item específico pelo "índice" usando o método .get()
        // Lembre-se: em programação, a contagem começa no ZERO!
        // 0 = Café, 1 = Pão de Queijo, 2 = Leite...
        String primeiroItem = listaDeCompras.get(0);
        System.out.println("\nO primeiro item que vou pegar é: " + primeiroItem);

        // 6. Removendo um item da lista usando o método .remove()
        // Decidi que não preciso mais de Leite. Vamos remover!
        listaDeCompras.remove("Leite");
        System.out.println("Removemos o leite. Nova lista: " + listaDeCompras);

        // 7. Percorrendo a lista de forma elegante (Laço For-Each)
        System.out.println("\n--- Lista Final no Carrinho (Item por Item) ---");
        
        for (String item : listaDeCompras) {
            
            System.out.println("- [ ] " + item);

        }

        // 8. Verificando se a lista esta vazia com o método .isEmpty()
        if (listaDeCompras.isEmpty()) {

            System.out.println("\nA lista está vazia!");

        } else {

            System.out.println("\nAinda há itens para comprar!");

        }

        System.out.println("\nÉ isso aí, Pe-pe-pessoal!!! E boas compras!");

    }

}