package com.hb.blog.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface GenericMapper<ENTITY , VIEW> {
    ENTITY from(VIEW view);

    VIEW to(ENTITY entity);

    default List<VIEW> to(Collection<ENTITY> entities){
        if(entities == null) return null;

        return entities.stream().map(this::to).collect(Collectors.toList());
    }

    default List<ENTITY> from(Collection<VIEW> views){
        if(views == null) return null;

        return views.stream().map(this::from).collect(Collectors.toList());
    }
}
