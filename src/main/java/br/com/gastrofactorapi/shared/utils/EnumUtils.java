package br.com.gastrofactorapi.shared.utils;

import java.util.Arrays;

import org.springframework.http.HttpStatus;

import br.com.gastrofactorapi.infrastructure.exceptions.BusinessException;

public class EnumUtils {

    private EnumUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada");
    }

    // Método genérico para converter string em enum, ignorando
    // maiúsculas/minúsculas
    // @param <E> O tipo do enum
    // @param enumClass A classe do enum
    // @param value A string a ser convertida
    public static <E extends Enum<E>> E getEnumFromString(Class<E> enumClass, String value) {
        if (value == null) {
            return null;
        }
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // Método para validar se um valor de enum é nulo e lançar uma exceção
    // personalizada
    // @param <E> O tipo do enum
    // @param enumValue O valor do enum a ser validado
    // @param message A mensagem de erro a ser usada na exceção
    public static <E extends Enum<E>> void validateNotNull(E enumValue, String message) {
        if (enumValue == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST.value(), message);
        }
    }

    public static <E extends Enum<E>> E getEnumFromStringOrThrow(Class<E> enumClass, String value) {
        if (value == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST.value(),
                    String.format("Valor nulo não é permitido para o enum %s. Valores válidos: %s",
                            enumClass.getSimpleName(), Arrays.toString(enumClass.getEnumConstants())));
        }

        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(HttpStatus.BAD_REQUEST.value(),
                    String.format("Valor '%s' não é válido para o enum %s. Valores válidos: %s",
                            value, enumClass.getSimpleName(), Arrays.toString(enumClass.getEnumConstants())));
        }
    }
}
