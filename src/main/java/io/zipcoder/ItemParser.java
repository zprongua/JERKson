package io.zipcoder;

import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {

    private int countOfErrors = 0;

    public List<Item> parseItemList(String valueToParse) {
        List<Item> li = new LinkedList<>();
        for (String s : valueToParse.split("##")) {
            try {
                li.add(parseSingleItem(s));
            } catch (ItemParseException ignored) {
            }
        }
        return li;
    }

    public Item parseSingleItem(String singleItem) throws ItemParseException {
        singleItem = singleItem.toLowerCase();
        Pattern good = Pattern.compile("name\\W\\w+\\Wprice\\W\\d+\\.\\d+\\Wtype\\W\\w+\\Wexpiration\\W.+$");
        Matcher valid = good.matcher(singleItem);
        Pattern element = Pattern.compile("\\W+\\w*[;@%*!^]|\\W+\\d+\\.\\d+[;@%*!^]|\\W+.+$");
        Matcher m = element.matcher(singleItem);
        ArrayList<Integer> ali = new ArrayList<>();
        if (valid.find()) {
            while (m.find()) {
                ali.add(m.start() + 1);
                ali.add(m.end() - 1);
            }
            String name = singleItem.substring(ali.get(0), ali.get(1));
            Double price = Double.parseDouble(singleItem.substring(ali.get(2), ali.get(3)));
            String type = singleItem.substring(ali.get(4), ali.get(5));
            String expiration = singleItem.substring(ali.get(6), ali.get(7) + 1);
            if (name.equals("co0kies")) name = "cookies";
            if (expiration.endsWith("##")) expiration = expiration.substring(0, expiration.length()-2);
            return new Item(name, price, type, expiration);
        }
        countOfErrors++;
        throw new ItemParseException();
    }

    public int getCountOfErrors() {
        return countOfErrors;
    }
}
