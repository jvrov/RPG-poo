import java.util.Objects;

public class Item implements Comparable<Item>, Cloneable {
    private String nome;
    private String descricao;
    private String efeito; 
    private int quantidade; 

    // Construtor Padrão
    public Item() {
        this.nome = "Item Vazio";
        this.descricao = "Nada";
        this.efeito = "Nenhum";
        this.quantidade = 0;
    }

    public Item(String nome, String descricao, String efeito, int quantidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.efeito = efeito;
        this.quantidade = quantidade;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getEfeito() { return efeito; }
    public int getQuantidade() { return quantidade; }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void usar() {
        if (this.quantidade > 0) {
            this.quantidade--; 
            System.out.println("Você usou 1 " + this.nome + ". Restam: " + this.quantidade);
        } else {
            System.out.println("Você não tem mais " + this.nome + " para usar.");
        }
    }

    @Override
    public int compareTo(Item outroItem) {
        return this.nome.compareTo(outroItem.getNome());
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return Objects.equals(nome, item.nome); 
    }

   
    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    
    @Override
    public Item clone() {
        try {
            return (Item) super.clone();
        } catch (CloneNotSupportedException e) {
            // Isso não deve acontecer, pois implementamos Cloneable
            return new Item(this.nome, this.descricao, this.efeito, this.quantidade);
        }
    }

    @Override
    public String toString() {
        return String.format("%s (x%d): %s", nome, quantidade, descricao);
    }
}