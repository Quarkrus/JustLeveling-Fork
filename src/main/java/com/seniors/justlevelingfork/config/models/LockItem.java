package com.seniors.justlevelingfork.config.models;

import com.seniors.justlevelingfork.JustLevelingFork;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LockItem {

    public String Item = "minecraft:diamond";

    public List<Aptitude> Aptitudes = List.of(new Aptitude());

    public LockItem() {
    }

    public LockItem(String itemName) {
        Item = itemName;
    }

    public LockItem(String itemName, Aptitude... aptitudes) {
        Item = itemName;
        Aptitudes = Arrays.stream(aptitudes).toList();
    }

    public static LockItem getLockItemFromString(String value, LockItem defaultValue) {
        try {
            return formatString(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    private static LockItem formatString(String value) {
        String[] initialSplit = value.split("#");
        LockItem lockItem = new LockItem(initialSplit[0]);

        List<Aptitude> aptitudeList = new ArrayList<>();
        String[] aptitudeSplit = initialSplit[1].split(";");

        for (String aptitudeString : aptitudeSplit) {
            String[] aptitude = aptitudeString.split(":");
            int level = Integer.parseInt(aptitude[1]);
            if(level < 2 || level > 1000) throw new IndexOutOfBoundsException();
            aptitudeList.add(new Aptitude(aptitude[0], level));
        }

        lockItem.Aptitudes = aptitudeList;
        return lockItem;
    }

    @Override
    public String toString() {
        if(Aptitudes.stream().anyMatch(Objects::isNull)){
            JustLevelingFork.getLOGGER().info(">> Found null aptitude at item {}", this.Item);
        }
        List<String> strings = new ArrayList<>();
        try{
            strings = Aptitudes.stream().map(Aptitude::toString).toList();
        } catch (NullPointerException e){
            JustLevelingFork.getLOGGER().info(">> Found null aptitude at item {}", this.Item);
        }

        String aptitudeStringList = String.join(";", strings);

        return String.format("%s#%s", Item, aptitudeStringList);
    }

    public static class Aptitude {

        public EAptitude Aptitude;

        public int Level;

        public Aptitude(String aptitudeName, int level) {
            try {
                Aptitude = EAptitude.valueOf(StringUtils.capitalize(aptitudeName));
            } catch (IllegalArgumentException e){
                JustLevelingFork.getLOGGER().info(">> Wrong aptitude name {}", aptitudeName);
                Aptitude = EAptitude.Strength;
            }
            Level = level;
        }

        public Aptitude() {
            Aptitude = EAptitude.Strength;
            Level = 2;
        }

        @Override
        public String toString() {
            return String.format("%s:%d", Aptitude.toString(), Level);
        }
    }
}
