package com.xyzla.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/* json 字符与对像转换 */
public final class JacksonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    private static ObjectMapper objectMapper;


    static {
//        objectMapper = JsonMapper.builder()
//                .findAndAddModules()
//                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//                .build();

        // 在反序列化时忽略在 JSON 字符串中 存在，而在 Java 中不存在的属性
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());

        // objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 关键代码-解决No serializer found for class java.lang.Object异常
        // objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
        //     @Override
        //     public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        //         //jsonGenerator.writeString("");//空串
        //         jsonGenerator.writeObject(new Object());//空对象
        //     }
        // });
    }

    /*
     * 使用泛型方法，把 json 字符串转换为相应的 JavaBean 对象。 <br>
     * (1) 转换为普通 JavaBean: readValue(json,Student.class)<br>
     * (2) 转换为 List,如List<Student>,将第二个参数传递为 Student [].class.<br>
     * 然后使用 Arrays.asList(); 方法把得到的数组转换为特定类型的 List
     */
    public static <T> T readValue(String jsonStr, Class<T> valueType) {
        try {
            return objectMapper.readValue(jsonStr, valueType);
        } catch (Exception e) {
            logger.error("", e);
        }

        return null;
    }

    /* json 数组转 List */
    public static <T> T readValue(String jsonStr, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(jsonStr, valueTypeRef);
        } catch (Exception e) {
            logger.error("", e);
        }

        return null;
    }

    public static <T> T readValueExt(String jsonStr, Class klass) {
        JavaType javaType =
                objectMapper.getTypeFactory().constructCollectionType(List.class, klass);
        try {
            return objectMapper.readValue(jsonStr, javaType);
        } catch (IOException e) {
            logger.error("", e);
        }
        return null;
    }


    /**
     * 获取泛型的 Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static <T> List readValueOfList(String jsonStr, Class<T> valueType) {

        try {
            JavaType javaType = getCollectionType(objectMapper, List.class, valueType);
            return objectMapper.readValue(jsonStr, javaType);
        } catch (Exception e) {
            logger.error("", e);
        }

        return null;
    }


    /* 把 JavaBean 转换为 json 字符串 */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
