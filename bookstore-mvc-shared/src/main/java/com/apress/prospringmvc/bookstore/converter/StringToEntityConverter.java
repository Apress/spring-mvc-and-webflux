package com.apress.prospringmvc.bookstore.converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Set;

public class StringToEntityConverter implements GenericConverter {

    private static final String ID_FIELD = "id";

    private final Class<?> clazz;

    @PersistenceContext
    private EntityManager em;

    public StringToEntityConverter(Class<?> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
    	return Set.of(
    			new ConvertiblePair(String.class, this.clazz),
					new ConvertiblePair(this.clazz, String.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (String.class.equals(sourceType.getType())) {
            if (!StringUtils.hasText((String) source)) {
                return null;
            }
            var id = Long.parseLong((String) source);
            return this.em.find(this.clazz, id);
        } else if (this.clazz.equals(sourceType.getType())) {
						if (source == null) {
								return "";
						} else {
								var field = ReflectionUtils.findField(source.getClass(), ID_FIELD);
								if (field != null) {
									ReflectionUtils.makeAccessible(field);
									return ReflectionUtils.getField(field, source);
								}
						}
        }
        throw new IllegalArgumentException("Cannot convert " + source + " into a suitable type!");
    }
}
