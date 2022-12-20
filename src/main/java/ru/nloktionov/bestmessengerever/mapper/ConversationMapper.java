package ru.nloktionov.bestmessengerever.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.nloktionov.bestmessengerever.entity.Conversation;
import ru.nloktionov.bestmessengerever.payload.response.chat.ConversationPreview;
import ru.nloktionov.bestmessengerever.payload.response.message.LastMessage;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ConversationMapper {
    Conversation toEntity(ConversationPreview conversationPreview);

    @Mapping(target = "id", source = "conversation.id")
    @Mapping(target = "unreadMessagesCount", source = "unreadMessagesCount")
    @Mapping(target = "chatName", source = "conversationName")
    ConversationPreview toPreview(Conversation conversation, LastMessage lastMessage, Integer unreadMessagesCount, String conversationName);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Conversation partialUpdate(ConversationPreview conversationPreview, @MappingTarget Conversation conversation);
}