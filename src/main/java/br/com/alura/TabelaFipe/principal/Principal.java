package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.models.DadosVeiculo;
import br.com.alura.TabelaFipe.models.Modelos;
import br.com.alura.TabelaFipe.models.VeiculoInfo;
import br.com.alura.TabelaFipe.services.ConsumoAPI;
import br.com.alura.TabelaFipe.services.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    private ConsumoAPI consumo = new ConsumoAPI();

    private ConverteDados conversor = new ConverteDados();

    Scanner leitura = new Scanner(System.in);

    public void exibeMenu() {
        System.out.println("""
                *** Bem vindo a tabela Fipe de veiculos! ***
                Por favor digite qual dos veiculos deseja analisar.
                
                1 - Carros
                2 - Motos
                3 - Caminhoes
                """);
        var escolhaUsuario = leitura.next();
        String endereco;

        if (escolhaUsuario.toLowerCase().contains("carr")) {
            endereco = URL_BASE + "carros/marcas";
        } else if (escolhaUsuario.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        } else {
            endereco = URL_BASE + "caminhoes/marcas";
        }

        var json = consumo.obterURL(endereco);
        System.out.println(json);

        var marcas = conversor.obterLista(json, DadosVeiculo.class);
        marcas.stream()
                .sorted(Comparator.comparing(DadosVeiculo::codigo))
                .forEach(System.out::println);

        System.out.println("Digite o código da marca que deseja");
        var codigoModelos = leitura.next();

        endereco = endereco + "/" + codigoModelos +"/modelos";
        json = consumo.obterURL(endereco);
        var modeloLista = conversor.obterDados(json, Modelos.class);

        System.out.println("\nModelos dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(DadosVeiculo::codigo))
                .forEach(System.out::println);

        System.out.println("Digite um trecho do veiculo que deseja: ");
        var nomeVeiculo = leitura.next();

        List<DadosVeiculo> modelosFiltrados = modeloLista.modelos().stream().
                filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos filtrados: ");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite o código do modelo para buscar os valores");
        codigoModelos = leitura.next();

        endereco = endereco + "/" + codigoModelos + "/anos";

        json = consumo.obterURL(endereco);
        List<DadosVeiculo> anos = conversor.obterLista(json , DadosVeiculo.class);
        List<VeiculoInfo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterURL(enderecoAnos);
            VeiculoInfo veiculo = conversor.obterDados(json , VeiculoInfo.class);
            veiculos.add(veiculo);
            
        }

        System.out.println("\nTodos os veiculos filtrados por ano: ");
        veiculos.forEach(System.out::println);
    }
}
