package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroceryReporter {
    private final String originalFileText;

    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
    }

    @Override
    public String toString() {
        ItemParser ip = new ItemParser();
        List<Item> li = ip.parseItemList(originalFileText);
        System.out.println(li.get(0));
        Map<String, List<Double>> hm = li.stream().collect(Collectors.groupingBy(Item::getName,
                Collectors.mapping(Item::getPrice, Collectors.toList())));
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (String k : hm.keySet()) {
            sb.append(String.format("name:%8s \t\t seen: %s times\n============= \t\t =============\n", k, hm.get(k).size()));
            sb.append(String.format("Price:%7s\t\t seen: %s times\n", hm.get(k).get(0), hm.get(k).size()));
            sb.append("\n");
        }
        return sb.toString();
    }
}
