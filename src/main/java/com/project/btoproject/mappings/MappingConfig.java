package com.project.btoproject.mappings;
import com.project.btoproject.dto.EventDto;
import com.project.btoproject.dto.GuideDto;
import com.project.btoproject.model.Event;
import com.project.btoproject.model.Guide;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MappingConfig {
    GuideDto guideToGuideDto(Guide guide);
    List<GuideDto> guidesToGuideDtosList(List<Guide> guides);
    EventDto eventToEventDto(Event event);
    List<EventDto> eventsToEventDtosList(List<Event> events);
}
