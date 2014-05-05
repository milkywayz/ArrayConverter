package net.milkycraft;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

public class Converter extends JavaPlugin {
	int recipes = 0;

	@Override
	public void onEnable() {
		this.writeJsonMats();
		this.writeJsonRecipes();
	}

	@SuppressWarnings("deprecation")
	private void writeJsonMats() {
		try {
			FileWriter fstream = new FileWriter("materials.json");
			BufferedWriter out = new BufferedWriter(fstream);
			JSONObject json = new JSONObject();
			for (Material m : Material.values()) {
				json.put(m.toString(), m.getId());
			}
			json.write(out);
			out.close();
		} catch (Exception ex) {
			System.err.println("Error: " + ex.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	private void writeJsonRecipes() {
		try {		
			FileWriter fstream = new FileWriter("recipes.json");
			BufferedWriter out = new BufferedWriter(fstream);
			JSONObject json = new JSONObject();
			for (Material m : Material.values()) {
				for(Recipe r : Bukkit.getRecipesFor(new ItemStack(m))) {
					recipes++;
					if(r instanceof ShapedRecipe) {
						ShapedRecipe sr = (ShapedRecipe)r;
						List<String[]> comp = new ArrayList<String[]>();
						for(Entry<Character, ItemStack> e : sr.getIngredientMap().entrySet())  {
							int id = e.getValue() == null ? 0 : e.getValue().getTypeId();
							comp.add(new String[]{e.getKey().toString(), String.valueOf(id)});
						}
						json.append(m.toString(), comp.toArray());
					} else if(r instanceof ShapelessRecipe) {
						ShapelessRecipe sr = (ShapelessRecipe)r;
						List<String[]> comp = new ArrayList<String[]>();
						for(ItemStack e : sr.getIngredientList()) {
							int id = e == null ? 0 : e.getTypeId();
							comp.add(new String[]{"x", String.valueOf(id)});
						}
						json.append(m.toString(), comp);
					}
				}
			}
			json.write(out);
			out.close();
		} catch (IOException ex) {
			System.err.println("Error: " + ex.getMessage());
		}
		System.out.println("Finished writing " + recipes + " bukkit recipes");
	}
}
