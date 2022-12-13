package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;

import java.util.*;
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
        Map<String, List<Double>> hm = li.stream().parallel().collect(Collectors.groupingBy(Item::getName,
                Collectors.mapping(Item::getPrice, Collectors.toList())));
        System.out.println(hm.keySet());
        Set<Double> prices = new TreeSet<>();
        StringBuilder sb = new StringBuilder();
        int dashes = 0;
        for (String k : hm.keySet()) {
            String j = k.substring(0,1).toUpperCase() + k.substring(1);
            sb.append(String.format("name:%8s        seen: %s times\n=============        =============\n", j, hm.get(k).size()));
            for (Double d : hm.get(k)) {
                if (!prices.contains(d)) {
                    prices.add(d);
                    int count = Collections.frequency(hm.get(k), d);
                    String time = "times";
                    if (count==1) time = "time";
                    sb.append(String.format("Price:   %s\t\t seen: %s %s\n", d, count, time));
                    if(dashes==0) {
                        dashes++;
                        sb.append("-------------        -------------\n");

                    }
                }

            }
            dashes = 0;
            sb.append("\n");
            prices.clear();
        }
        sb.append(String.format("Errors               seen: %s times\n", ip.getCountOfErrors()));
        return sb.toString();
    }
}
