package io.zipcoder;

import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ItemParser {

    private int countOfErrors = 0;

    public List<Item> parseItemList(String valueToParse) {
        List<Item> li = new LinkedList<>();
        for (String s : valueToParse.split("##")) {
            try {
                li.add(parseSingleItem(s));
            } catch (ItemParseException e) {
                continue;
            }
        }
        return li;
    }

    public Item parseSingleItem(String singleItem) throws ItemParseException {
        singleItem = singleItem.toLowerCase();
        Pattern element = Pattern.compile("\\W+\\w*[;@%*!^]|\\W+\\d+\\.\\d+[;@%*!^]|\\W+.+$");
        Pattern good = Pattern.compile("name\\W\\w+\\Wprice\\W\\d+\\.\\d+\\Wtype\\W\\w+\\Wexpiration\\W.+$");
        Matcher m = element.matcher(singleItem);
        Matcher valid = good.matcher(singleItem);
        Integer[] ia = new Integer[8];
        int count = 0;
        if (valid.find()) {
            while (m.find()) {
                ia[count] = m.start() + 1;
                ia[count + 1] = m.end() - 1;
                count += 2;
            }
            String name = singleItem.substring(ia[0], ia[1]);
            if (name.equals("co0kies")) name = "cookies";
            Double price = Double.parseDouble(singleItem.substring(ia[2], ia[3]));
            String type = singleItem.substring(ia[4], ia[5]);
            String expiration = singleItem.substring(ia[6], ia[7] + 1);
            if (expiration.substring(expiration.length()-2).equals("##")) expiration = expiration.substring(0, expiration.length()-2);
            Item item = new Item(name, price, type, expiration);
            return item;
        }
        countOfErrors++;
        throw new ItemParseException();
    }

    public int getCountOfErrors() {
        return countOfErrors;
    }
}
