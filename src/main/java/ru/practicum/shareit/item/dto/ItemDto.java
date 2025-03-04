package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.RequestMethod;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    private Long id;
    @NotBlank(groups = RequestMethod.Create.class)
    private String name;
    @NotNull(groups = RequestMethod.Create.class)
    private String description;
    @NotNull(groups = RequestMethod.Create.class)
    private Boolean available;
    private Long owner;
    private Long request;
}
