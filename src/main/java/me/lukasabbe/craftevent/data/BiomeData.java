package me.lukasabbe.craftevent.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BiomeData {
    public String name;
    public int x;
    public int y;
    public int z;
    @SerializedName("entities")
    public List<EntityData> entities;
}
