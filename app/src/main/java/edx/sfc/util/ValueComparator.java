package edx.sfc.util;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator<String> {
    Map<String, Integer> map;

    public ValueComparator(Map<String, Integer> map) {
        this.map = map;
    }

    public int compare(String keyA, String keyB) {
        if (map.get(keyA) >= map.get(keyB)) {
            return -1;
        } else {
            return 1;
        }
    }
}