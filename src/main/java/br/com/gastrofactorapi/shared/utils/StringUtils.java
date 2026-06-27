package br.com.gastrofactorapi.shared.utils;

public class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada");
    }

    // Método para verificar se uma string é nula ou vazia
    // @param str A string a ser verificada
    // @return true se a string for nula ou vazia, false caso contrário
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Método para capitalizar a primeira letra de cada palavra em uma string
    // @param str A string a ser capitalizada
    // @return A string com a primeira letra de cada palavra capitalizada
    public static String capitalizeWords(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        String[] words = str.trim().split("\\s+");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return capitalized.toString().trim();
    }

    // Método para remover acentos de uma string
    // @param str A string da qual os acentos devem ser removidos
    // @return A string sem acentos
    public static String removeAccents(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        return java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    // Método para validar se uma string é um número
    // @param str A string a ser validada
    // @return true se a string for um número, false caso contrário
    public static boolean isNumeric(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para truncar uma string a um comprimento máximo
    // @param str A string a ser truncada
    // @param maxLength O comprimento máximo da string
    // @return A string truncada ou a string original se for mais curta
    public static String truncate(String str, int maxLength) {
        if (isNullOrEmpty(str) || maxLength <= 0) {
            return "";
        }
        return str.length() <= maxLength ? str : str.substring(0, maxLength);
    }

}
