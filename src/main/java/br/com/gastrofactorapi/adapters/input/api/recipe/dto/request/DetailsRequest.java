package br.com.gastrofactorapi.adapters.input.api.recipe.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class DetailsRequest {

  @NotBlank(message = "Nome é obrigatório")
  private String name;
  private String image;
  private Integer servings;
  private String category;
}