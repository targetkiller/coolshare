package org.scauhci.enumvalue;

import java.util.HashMap;

public enum DocumentType {
	common(0), secret(1);
    public final int value;

    DocumentType(int value) {
        this.value = value;
    }
    transient private final static HashMap<Integer, DocumentType> map = new HashMap<Integer, DocumentType>(2);
    transient private final static HashMap<Integer, String> mapStr = new HashMap<Integer, String>(2);

    static {
        map.put(common.value, common);
        map.put(secret.value, secret);
        mapStr.put(common.value, "公开");
        mapStr.put(secret.value, "私有");
    }

    public static DocumentType getType(int value) {
        return map.get(value);
    }

    public static String[] getTypes() {
        return mapStr.values().toArray(new String[mapStr.size()]);
    }

    @Override
    public String toString() {
        return mapStr.get(value);
    }
}
