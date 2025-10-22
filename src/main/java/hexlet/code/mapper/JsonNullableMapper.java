package hexlet.code.mapper;

import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class JsonNullableMapper {

    /**
     *
     * @param entity
     * @return entity entity
     * @param <T>
     */
    public <T> JsonNullable<T> wrap(T entity) {
        return JsonNullable.of(entity);
    }

    /**
     *
     * @param jsonNullable
     * @return entity entity
     * @param <T>
     */
    public <T> T unwrap(JsonNullable<T> jsonNullable) {
        return jsonNullable == null ? null : jsonNullable.orElse(null);
    }

    /**
     *
     * @param nullable
     * @return boolean boolean
     * @param <T>
     */
    @Condition
    public <T> boolean isPresent(JsonNullable<T> nullable) {
        return nullable != null && nullable.isPresent();
    }
}
