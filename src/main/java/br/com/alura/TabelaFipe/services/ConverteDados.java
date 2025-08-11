package br.com.alura.TabelaFipe.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;

public class ConverteDados implements IConverteDados {
    private ObjectMapper mapper = new ObjectMapper();


    // Método que ira converter uma String json para um objeto Java
    @Override
    public <T> T obterDados(String json, Class<T> classe) {

        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override

    // Método que ira converter um JSON em uma lista de objetos do tipo especificado
    public <T> List<T> obterLista(String json, Class<T> classe) {

        CollectionType lista = mapper.getTypeFactory()
                .constructCollectionType(List.class , classe);

        try {
            return mapper.readValue(json, lista);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
