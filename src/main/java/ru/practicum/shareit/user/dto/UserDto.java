package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.RequestMethod;

@Data
@Builder
public class UserDto {
    private Long id;
    @NotBlank(groups = {RequestMethod.Create.class})
    private String name;
    @Email(groups = RequestMethod.Create.class)
    @NotBlank(groups = RequestMethod.Create.class)
    private String email;
}
