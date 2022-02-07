package controllers;

import org.junit.jupiter.api.Test;

import java.util.*;

public class TransactionControllerTest {


    @Test
    public void testHashMap() {
        Map<String, List<Integer>> map = new HashMap<>();

        List<Integer> list = new ArrayList<>(List.of(2));
        list.add(3);
        list.add(5);
        map.put("test", list);

        map.get("test").add(4);
        map.get("test").add(7);

        for (int i : map.get("test")) {
            System.out.println(i);
        }

        System.out.println(map.get("ok").size());
    }
}
