package br.com.gastrofactorapi.adapters.input.api.recipe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PreparationMethodRequest {
    private Integer ordinationId;
    private String title;
    private String description;
}
