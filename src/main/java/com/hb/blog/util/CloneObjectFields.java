package com.hb.blog.util;

import com.hb.blog.dto.UserDTO;
import com.hb.blog.model.User;

import java.lang.reflect.Field;

public class CloneObjectFields {
    public static void cloneNonNullProperties(Object source, Object target){
        try{
            Field[] fields = source.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(source);
                if (value != null) {
                    field.set(target, value);
                }
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
