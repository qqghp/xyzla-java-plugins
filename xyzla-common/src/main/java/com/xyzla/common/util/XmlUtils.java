package com.xyzla.common.util;


import com.thoughtworks.xstream.XStream;

/**
 * Xml和对象转换工具
 */
public class XmlUtils {

    /**
     * 对象转换为xml字符串
     *
     * @param obj
     * @return
     */
    public static String toXml(Object obj) {
        XStream xstream = new XStream();

        return xstream.toXML(obj);
    }

    /**
     * xml转换为对象
     *
     * @param xml
     * @param type
     * @return
     */
    public static <T> T toObject(String xml, Class<T> type) {
        XStream xstream = new XStream();

        xstream.processAnnotations(type);
        xstream.ignoreUnknownElements();

        Object obj = xstream.fromXML(xml);

        return type.cast(obj);
    }

}
