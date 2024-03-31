package com.example.rentapp.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AddCategoryRequirementsRequest {

    @NotNull
    private Long categoryId;

    private List<Long> documentTypeIds;

}
