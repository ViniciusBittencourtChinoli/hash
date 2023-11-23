/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tabelahash;

/**
 *
 * @author itzvi
 */

import java.util.Random;
import java.util.Scanner;
import java.util.List;

class Registro {
    private int codigo;

    public Registro(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}

public class TesteHash {
    private static final int[] TAMANHOS = {20, 200, 2000, 20000, 200000};
    private static final int[] CONJUNTOS_DE_DADOS = {20000, 100000, 500000, 1000000, 5000000};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha o algoritmo de hash:");
        System.out.println("1. Resto da divisao");
        System.out.println("2. Multiplicacao");
        System.out.println("3. Dobramento com giro");

        int escolha = scanner.nextInt();

        switch (escolha) {
            case 1:
                teste("Resto da divisao");
                break;
            case 2:
                teste("Multiplicacao");
                break;
            case 3:
                teste("Dobramento com giro");
                break;
            default:
                System.out.println("Escolha inválida");
        }

        scanner.close();
    }
    
    
    

    public static void teste(String algoritmo) {
        
        for (int tamanho : TAMANHOS) {
            for (int conjuntoDeDados : CONJUNTOS_DE_DADOS) {
                long tempoInsercaoTotal = 0;
                long numeroColisoesTotal = 0;
                long tempoBuscaTotal = 0;
                long numeroComparacoesTotal = 0;

                for (int i = 0; i < 5; i++) {
                    long tempoInsercao = 0;
                    long numeroColisoes = 0;
                    long tempoBusca = 0;
                    long numeroComparacoes = 0;

                    int[] tabelaHash = new int[tamanho];
                    
                    Registro[] dados = gerarConjuntoDeDados(conjuntoDeDados);

                    // Inserir os dados na tabela hash
                    long inicioInsercao = System.nanoTime();
                    for (Registro registro : dados) {
                        int indice = calcularIndice(registro.getCodigo(), tamanho, algoritmo);
                        if (tabelaHash[indice] != 0) {
                            numeroColisoes++;
                        }
                        tabelaHash[indice] = registro.getCodigo();
                    }
                    long fimInsercao = System.nanoTime();
                    tempoInsercao = fimInsercao - inicioInsercao;

                    // Buscar os dados na tabela hash
                    long inicioBusca = System.nanoTime();
                    for (Registro registro : dados) {
                        int indice = calcularIndice(registro.getCodigo(), tamanho, algoritmo);
                        if (tabelaHash[indice] == registro.getCodigo()) {
                            numeroComparacoes++;
                        }
                    }
                    long fimBusca = System.nanoTime();
                    tempoBusca = fimBusca - inicioBusca;                    
                    
                    // Para imprimir os dados, precisa iterar sobre o array, array dados não contém os códigos dos registros, ele contém os registros em si
                    /*
                    for (Registro registro : dados) {
                        System.out.println(registro.getCodigo());
                    }
                    */

                    //System.out.println("Rodada: " + i);
                    //System.out.println("Tempo de insercao: " + tempoInsercao);
                    //System.out.println("Numero de colisoes: " + numeroColisoes);
                    //System.out.println("Tempo de busca: " + tempoBusca);
                    //System.out.println("Numero de comparacoes: " + numeroComparacoes);
                    //System.out.println();
                    //System.out.println(conjuntoDeDados);
                    //System.out.println(dados);
                    //System.out.println(tabelaHash);

                    
                    tempoInsercaoTotal += tempoInsercao;
                    numeroColisoesTotal += numeroColisoes;
                    tempoBuscaTotal += tempoBusca;
                    numeroComparacoesTotal += numeroComparacoes;
                    
                }
                

                long mediaTempoInsercao = tempoInsercaoTotal / 5;
                long mediaNumeroColisoes = numeroColisoesTotal / 5;
                long mediaTempoBusca = tempoBuscaTotal / 5;
                long mediaNumeroComparacoes = numeroComparacoesTotal / 5;

                System.out.println("Algoritmo: " + algoritmo);
                System.out.println("Tamanho da tabela hash: " + tamanho);
                System.out.println("Conjunto de dados: " + conjuntoDeDados);
                System.out.println("Tempo medio de insercao: " + mediaTempoInsercao);
                System.out.println("Numero medio de colisoes: " + mediaNumeroColisoes);
                System.out.println("Tempo medio de busca: " + mediaTempoBusca);
                System.out.println("Numero medio de comparacoes: " + mediaNumeroComparacoes);
                System.out.println();
            }
        }
    }

    public static Registro[] gerarConjuntoDeDados(int tamanho) {
        Random random = new Random(12345); // Seed para manter conjuntos iguais e válidos
        Registro[] dados = new Registro[tamanho];

        for (int i = 0; i < tamanho; i++) {
            int codigo = random.nextInt(1000000000); // Código de registro com 9 dígitos
            dados[i] = new Registro(codigo);
            
        }
        //System.out.println(dados);
        return dados;
        
    }

    public static int calcularIndice(int codigo, int tamanho, String algoritmo) {
        if (codigo == 0) {
            return 0;
        }

        if (algoritmo.equals("Resto da divisao")) {
            return calcularRestoDivisao(codigo, tamanho);
        } else if (algoritmo.equals("Multiplicacao")) {
            double a = (calcularRaizQuadrada(5) - 1) / 2;
            return arredondaBaixo(tamanho * ((codigo * a) % 1));
        } else if (algoritmo.equals("Dobramento com giro")) {
            if (tamanho <= 0) {
                return -1; // Tratamento para tamanho não positivo
            }

            int meio = String.valueOf(tamanho).length() / 2;

            if (meio == 0 || codigo < 0) {
                return -1;
            }

            int potencia = calcularPotencia(10, meio);
            int parte1 = codigo / potencia;
            int parte2 = codigo % potencia;
            int soma = parte1 + parte2;

            if (soma == 0) {
                return -1;
            } else {
                return soma % tamanho;
            }
        }
        return -1;
    }

    public static int calcularRestoDivisao(int codigo, int divisor) {
        return codigo - (divisor * (codigo / divisor));
    }

    public static double calcularRaizQuadrada(double numero) {
        // Implementação raiz quadrada
        return calcularRaizQuadradaAprox(numero, 0.0001);
    }

    private static double calcularRaizQuadradaAprox(double numero, double precisao) {
        double aproximacao = numero / 2;
        while (Math.abs(aproximacao * aproximacao - numero) > precisao) {
            aproximacao = (aproximacao + numero / aproximacao) / 2;
        }
        return aproximacao;
    }

    public static int calcularPotencia(int base, int expoente) {
        // Implementação potência
        int resultado = 1;
        for (int i = 0; i < expoente; i++) {
            resultado *= base;
        }
        return resultado;
    }

    public static int arredondaBaixo(double numero) {
        // Implementação arredondamento para baixo
        int piso = (int) numero;
        return numero < piso ? piso - 1 : piso;
    }
}