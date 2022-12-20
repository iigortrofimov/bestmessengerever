package ru.nloktionov.bestmessengerever.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.nloktionov.bestmessengerever.entity.User;
import ru.nloktionov.bestmessengerever.payload.response.UserDetails;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDetails userDetails);

    UserDetails toDetails(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDetails userDetails, @MappingTarget User user);
}