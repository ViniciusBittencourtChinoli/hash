/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg03;

/**
 *
 * @author itzvi
 */
import java.util.Random;

public class TabelaHash {
    private static final int SEED = 123456789;
    private static final int[] tamanhos = {20, 200, 2000, 20000, 200000};
    private static final String[] funcoesHash = {"resto da divisão", "multiplicação", "dobramento"};

    public static void main(String[] args) {
        Random random = new Random(SEED);

        for (int tamanho : tamanhos) {
            for (String funcaoHash : funcoesHash) {
                //nova tabela hash
                TabelaHash tabela = new TabelaHash(tamanho, funcaoHash);

                //conjunto de dados aleatório
                int[] conjuntoDados = new int[tamanho];
                for (int i = 0; i < tamanho; i++) {
                    conjuntoDados[i] = random.nextInt(1000000000);
                }

                //medição do tempo de inserção e o número de colisões
                long startTimeInsercao = System.nanoTime();
                int colisoes = tabela.inserir(conjuntoDados);
                long endTimeInsercao = System.nanoTime();
                long tempoInsercao = (endTimeInsercao - startTimeInsercao); // Tempo em nanossegundos

                //medição do tempo de busca
                long startTimeBusca = System.nanoTime();

                // busca os elementos do conjunto de dados na tabela hash
                for (int codigo : conjuntoDados) {
                    tabela.buscar(codigo);
                }

                // fim da medição do tempo de busca
                long endTimeBusca = System.nanoTime();
                long tempoBusca = (endTimeBusca - startTimeBusca); // Tempo em nanossegundos

                System.out.println("Tamanho da Tabela: " + tamanho);
                System.out.println("Função de Hash: " + funcaoHash);
                System.out.println("Tempo de Inserção (ns): " + tempoInsercao);
                System.out.println("Número de Colisões: " + colisoes);
                System.out.println("Tempo de Busca (ns): " + tempoBusca);
                System.out.println("------------------------------");
            }
        }
    }

    private int tamanho;
    private int[] tabela;
    private String funcaoHash;

    public TabelaHash(int tamanho, String funcaoHash) {
        this.tamanho = tamanho;
        this.funcaoHash = funcaoHash;
        tabela = new int[tamanho];
    }

    public int inserir(int[] conjuntoDados) {
        int colisoes = 0;
        for (int codigo : conjuntoDados) {
            int indice = hash(codigo);

            while (tabela[indice] != 0) {
                colisoes++;
                indice = (indice + 1) % tamanho;
            }

            tabela[indice] = codigo;
        }
        return colisoes;
    }

    public boolean buscar(int codigo) {
        int indice = hash(codigo);

        while (tabela[indice] != codigo && tabela[indice] != 0) {
            indice = (indice + 1) % tamanho;
        }

        return tabela[indice] == codigo;
    }

    private int hash(int codigo) {
        if (funcaoHash.equals("resto da divisão")) {
            return hashRestoDaDivisao(codigo);
        } else if (funcaoHash.equals("multiplicação")) {
            return hashMultiplicacao(codigo);
        } else if (funcaoHash.equals("dobramento")) {
            return hashDobramento(codigo);
        }

        return codigo % tamanho;
    }

    private int hashRestoDaDivisao(int codigo) {
        return codigo % tamanho;
    }

    private int hashMultiplicacao(int codigo) {
        return (int) (tamanho * ((codigo * 0.6180339887) % 1));
    }

    private int hashDobramento(int codigo) {
        int resultado = 0;
        while (codigo > 0) {
            resultado += codigo % tamanho;
            codigo /= tamanho;
        }
        return resultado % tamanho;
    }
}