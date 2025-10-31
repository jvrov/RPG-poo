import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Inventario implements Cloneable {
    
    private List<Item> itens;

    public Inventario() {
        this.itens = new ArrayList<>();
    }
    
   
    public List<Item> getItens() {
        return this.itens;
    }

    public void adicionarItem(Item itemParaAdicionar) {
        int indice = itens.indexOf(itemParaAdicionar);

        if (indice != -1) {
            Item itemExistente = itens.get(indice);
            itemExistente.setQuantidade(itemExistente.getQuantidade() + itemParaAdicionar.getQuantidade());
            System.out.println("Item '" + itemExistente.getNome() + "' atualizado. Qtd: " + itemExistente.getQuantidade());
        } else {
            itens.add(itemParaAdicionar.clone()); 
        }
    }

  
    public void removerItem(Item itemParaRemover, int quantidade) {
        int indice = itens.indexOf(itemParaRemover);

        if (indice != -1) {
            Item itemExistente = itens.get(indice);
            int novaQuantidade = itemExistente.getQuantidade() - quantidade;
            itemExistente.setQuantidade(novaQuantidade);

            if (novaQuantidade <= 0) {
                itens.remove(indice); 
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


    public void listarItens() {
        if (itens.isEmpty()) {
            System.out.println("Inventário vazio.");
            return;
        }
        
        System.out.println("--- INVENTÁRIO (Ordenado) ---");
        Collections.sort(itens); 
        
        int i = 1;
        for (Item item : itens) {
            System.out.println("[" + i + "] " + item.toString());
            i++;
        }
        System.out.println("-----------------------------");
    }
    

    public Item getItemPorIndice(int indice) {
        if (indice >= 0 && indice < itens.size()) {
            return itens.get(indice);
        }
        return null; 
    }

  
    @Override
    public Inventario clone() {
        try {
            Inventario inventarioCopiado = (Inventario) super.clone();
            inventarioCopiado.itens = new ArrayList<>();
            for (Item item : this.itens) {
                inventarioCopiado.itens.add(item.clone()); 
            }
            return inventarioCopiado;
            
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); 
        }
    }

    @Override
    public String toString() {
        return "Inventário com " + itens.size() + " tipos de itens.";
    }
}