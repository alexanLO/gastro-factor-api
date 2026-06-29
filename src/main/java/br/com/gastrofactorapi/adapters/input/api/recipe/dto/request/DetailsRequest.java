package br.com.gastrofactorapi.adapters.input.api.recipe.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsRequest {

  @NotBlank(message = "Nome é obrigatório")
  private String name;
  private Integer servings;
  private String category;
}