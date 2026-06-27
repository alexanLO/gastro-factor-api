package br.com.gastrofactorapi.adapters.input.api.recipe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsRequest {

  private String name;
  private Integer servings;
  private String category;
}