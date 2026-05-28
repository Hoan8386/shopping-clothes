package com.vn.shopping.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResDataAIProductDTO {
    private String id;

    private String name;

    @JsonProperty("product_type")
    private String productType;

    private String color;

    private Integer price;

    private String gender;

    private String description;
}