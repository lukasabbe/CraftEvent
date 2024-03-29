package me.lukasabbe.craftevent.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ItemData {
    @SerializedName("item")
    public String ItemName;
    @SerializedName("biomes")
    public List<String> biomes;

    public List<BiomeData> biomeDatas = new ArrayList<>();
}
