package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.RequestMethod;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    private Long id;
    @NotBlank(groups = {RequestMethod.Create.class})
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private Long requestId;
}
