package com.simoc.api.common;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateUsername(String name, String firstSurname, String secondSurname)
    {
        String firtLetter = name.substring(0, 1);
        String username = firtLetter.toLowerCase() + firstSurname.toLowerCase() + secondSurname.substring(0, 1).toLowerCase();
        return username;
    }

    public static String getNameAndLastName(String complete, int type)
    {

        List<String> reservedWord = new ArrayList<>();
        reservedWord.add("da");
        reservedWord.add("de");
        reservedWord.add("del");
        reservedWord.add("la");
        reservedWord.add("las");
        reservedWord.add("los");
        reservedWord.add("san");
        reservedWord.add("santa");


        List<String> listName = new ArrayList<>();

        String[] arrayNames = complete.split(",");
        if (arrayNames.length == 2)
        {

            String fullName = complete.replace(",", "");

            fullName = fullName.replace("  ", " ");

            arrayNames = fullName.split(" ");
        }
        String auxName = "";
        for (String name: arrayNames) {
            String nameAux = name.toLowerCase();

            if (reservedWord.contains(nameAux))
            {
                auxName += name + ' ';
            }
            else
            {
                listName.add(auxName + name);
                auxName = "";
            }
        }

        if (type == 2)
        {

            return listName.size() > 3 ? listName.get(2) + ' ' + listName.get(3) : listName.get(2);
        }

        return listName.get(type);

    }

    public static String generatePassword(int length)
    {
        StringBuilder builder = new StringBuilder();
        while (length-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
