import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gerencia a lista de itens de um Personagem.
 * Implementa Cloneable para o saque de inimigos.
 */
public class Inventario implements Cloneable {
    
    private List<Item> itens;

    public Inventario() {
        this.itens = new ArrayList<>();
    }
    
    /**
     * Retorna a lista de itens. Necessário para a lógica de saque.
     */
    public List<Item> getItens() {
        return this.itens;
    }

    /**
     * Adiciona um item. Se já existir (pelo equals), aumenta a quantidade.
     */
    public void adicionarItem(Item itemParaAdicionar) {
        // Verifica se o item já existe (usando o método equals() do Item)
        int indice = itens.indexOf(itemParaAdicionar);

        if (indice != -1) {
            // Item encontrado! Aumenta a quantidade.
            Item itemExistente = itens.get(indice);
            itemExistente.setQuantidade(itemExistente.getQuantidade() + itemParaAdicionar.getQuantidade());
            System.out.println("Item '" + itemExistente.getNome() + "' atualizado. Qtd: " + itemExistente.getQuantidade());
        } else {
            // Item novo, adiciona na lista
            itens.add(itemParaAdicionar.clone()); // Clona o item ao adicionar
        }
    }

    /**
     * Remove um item (diminuindo a quantidade).
     */
    public void removerItem(Item itemParaRemover, int quantidade) {
        int indice = itens.indexOf(itemParaRemover);

        if (indice != -1) {
            Item itemExistente = itens.get(indice);
            int novaQuantidade = itemExistente.getQuantidade() - quantidade;
            itemExistente.setQuantidade(novaQuantidade);

            if (novaQuantidade <= 0) {
                itens.remove(indice); // Remove da lista se acabar
                System.out.println("Item '" + itemExistente.getNome() + "' removido do inventário.");
            }
        } else {
            System.out.println("Item '" + itemParaRemover.getNome() + "' não encontrado para remover.");
        }
    }
    
    public Item encontrarItem(String nome) {
        for (Item item : itens) {
            if (item.getNome().equalsIgnoreCase(nome)) {
                return item;
            }
        }
        return null;
    }

    /**
     * MÉTODO ATUALIZADO
     * Lista todos os itens, ordenados E NUMERADOS.
     * Usa o compareTo() da classe Item.
     */
    public void listarItens() {
        if (itens.isEmpty()) {
            System.out.println("Inventário vazio.");
            return;
        }
        
        System.out.println("--- INVENTÁRIO (Ordenado) ---");
        Collections.sort(itens); // Ordena a lista usando compareTo()
        
        // Adiciona um número (índice + 1) para seleção
        int i = 1;
        for (Item item : itens) {
            System.out.println("[" + i + "] " + item.toString());
            i++;
        }
        System.out.println("-----------------------------");
    }
    
    /**
     * --- MÉTODO NOVO ---
     * Pega um item da lista pelo seu número (índice).
     * O usuário digita "1", que corresponde ao índice "0".
     */
    public Item getItemPorIndice(int indice) {
        if (indice >= 0 && indice < itens.size()) {
            return itens.get(indice);
        }
        return null; // Retorna nulo se o índice for inválido
    }

    /**
     * Clona o inventário e TODOS os itens dentro dele (cópia profunda).
     * Usado para o jogador saquear o inventário do inimigo.
     */
    @Override
    public Inventario clone() {
        try {
            Inventario inventarioCopiado = (Inventario) super.clone();
            // Agora, clona a lista e cada item dentro dela
            inventarioCopiado.itens = new ArrayList<>();
            for (Item item : this.itens) {
                inventarioCopiado.itens.add(item.clone()); // Clona cada item
            }
            return inventarioCopiado;
            
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Não deve acontecer
        }
    }

    @Override
    public String toString() {
        return "Inventário com " + itens.size() + " tipos de itens.";
    }
}