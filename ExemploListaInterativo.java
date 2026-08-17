import java.util.ArrayList;
import java.util.List;
import java.util.Scanner; // 1. Importamos o Scanner para ler o teclado

public class ExemploListaInterativo {

    public static void main(String[] args) {

        System.out.println("--- Preparando a nossa Lista de Compras ---\n");

        // 2. Inicialização mais enxuta!
        // Criamos a lista já com alguns itens iniciais usando List.of()
        List<String> listaDeCompras = new ArrayList<>(List.of("Café", "Pão de Queijo", "Leite", "Chocolate"));

        // 3. Criamos o leitor do teclado
        Scanner leitor = new Scanner(System.in);
        String novoItem = "";

        System.out.println("Itens iniciais: " + listaDeCompras);
        System.out.println("\n--- Agora é asua vez! Adicione itens à lista ---");
        System.out.println("(Digite o nome do item ou 'fim' para encerrar a digitação)\n");

        // 4. Laço de repetição para o usuário digitar os itens
        while(true) {
            
            System.out.print("Digite um item: ");
            novoItem = leitor.nextLine(); // Lê o que o usuário digitou

            // Se o usuário digitar "fim" (independente de maiúscula/minúscula) para o laço
            if(novoItem.equalsIgnoreCase("fim")) {

                break;
            }

             // Se não for "fim", adiciona o item na lista
            listaDeCompras.add(novoItem);
            System.out.println("-> '" + novoItem + "' adicionado com sucesso!");

        } // fim-while
    
        // 5. Fechamos o leitor (boa prática em java)
        leitor.close();
        System.out.println("\n-------------------------------------------");
        System.out.println("Temos: " + listaDeCompras.size() + " itens na lista.");
        System.out.println("Itens atuais: " + listaDeCompras);

        // 6. Removendo o leite (mantivemos a sua lógica original)
        if (listaDeCompras.contains("Leite")) {
         
            listaDeCompras.remove("Leite");
            System.out.println("Removemos o leite por padrão. Nova lista: " + listaDeCompras);
        
        }

        // 7. Mostrando a lista final de forma elegante
        System.out.println("\n--- Lista Final no Carrinho (Item por Item) ---");
        for (String item : listaDeCompras) {

            System.out.println("- [ ] " + item);
        
        }

        // 8. Verificação final
        if (listaDeCompras.isEmpty()) {

            System.out.println("\nA lista está vazia!");

        } else {

            System.out.println("\nAinda há itens para comprar!");

        }

        System.out.println("\nÉ isso aí Pe-Pe-ssoAll! Boas compras!");

    }

}