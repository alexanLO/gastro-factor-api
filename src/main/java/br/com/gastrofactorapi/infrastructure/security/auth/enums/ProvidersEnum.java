package br.com.gastrofactorapi.infrastructure.security.auth.enums;

public enum ProvidersEnum {

    LOCAL("local"),
    GOOGLE("google"),
    IOS("ios");

    private String value;

    ProvidersEnum(String value) {
        this.value = value;
    }
}