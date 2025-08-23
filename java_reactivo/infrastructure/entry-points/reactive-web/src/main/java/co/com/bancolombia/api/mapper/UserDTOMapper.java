package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.CreateUserDto;
import co.com.bancolombia.api.dto.EditUserDto;
import co.com.bancolombia.api.dto.UserDto;
import co.com.bancolombia.model.user.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel ="spring")
public interface UserDTOMapper {

    UserDto toResponse(User user);
    List<UserDto> toResponseList(List<User> users);
    User toModel(CreateUserDto createUserDto);
    User toDTO(EditUserDto editUserDto);
}