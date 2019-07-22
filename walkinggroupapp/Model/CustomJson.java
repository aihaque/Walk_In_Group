package com.example.homepc.walkinggroupapp.Model;

import java.util.Arrays;

public class CustomJson {
    private String background;
    private String sticker;
    private String[] quests;

    public CustomJson() {
    }

    public String getBackground() { return background; }
    public void setBackground(String backrground) { this.background = backrground; }

    public String getSticker() { return sticker; }
    public void setSticker(String sticker) { this.sticker = sticker; }

    public String[] getQuests() { return quests; }
    public void setQuests(String[] quests) { this.quests = quests; }

    @Override
    public String toString() {
        return "CustomJson{" +
                "background=" + background +
                ", sticker='" + sticker + '\'' +
                ", quests=" + Arrays.toString(quests) +
                '}';
    }
}
