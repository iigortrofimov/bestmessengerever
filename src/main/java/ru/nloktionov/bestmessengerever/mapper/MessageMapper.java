package ru.nloktionov.bestmessengerever.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.nloktionov.bestmessengerever.entity.Message;
import ru.nloktionov.bestmessengerever.enums.MessageStatus;
import ru.nloktionov.bestmessengerever.payload.response.message.LastMessage;
import ru.nloktionov.bestmessengerever.payload.response.message.MessageDetails;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = MessageMapper.class)
public interface MessageMapper {
    Message toEntity(MessageDetails messageDetails);

    @Mapping(target = "content", source = "message.messageMeta.content")
    @Mapping(target = "timestamp", source = "message.createdAt")
    @Mapping(target = "chatId", source = "message.conversation.id")
    @Mapping(target = "messageStatus", source = "messageStatus")
    MessageDetails toMessageDetails(Message message, MessageStatus messageStatus);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Message partialUpdate(MessageDetails messageDetails, @MappingTarget Message message);

    @Mapping(target = "content", source = "messageMeta.content")
    @Mapping(target = "timestamp", source = "createdAt")
    @Mapping(target = "chatId", source = "conversation.id")
    LastMessage toLastMessage(Message message);

}