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

/*	Copyright (c) 2013, Nick Porillo milkywayz@mail.com
 *
 *	Permission to use, copy, modify, and/or distribute this software for any purpose 
 *  with or without fee is hereby granted, provided that the above copyright notice 
 *  and this permission notice appear in all copies.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE 
 *	INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE 
 *  FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS
 *	OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, 
 *  ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

public class Converter extends JavaPlugin {
	private int total = Material.values().length;
	private int percent = 0;
	int recipes = 0;

	@Override
	public void onEnable() {
		this.writeJsonMats();
		this.writeJsonRecipes();
	}

	private void writeJsonMats() {
		try {
			FileWriter fstream = new FileWriter("materials.json");
			BufferedWriter out = new BufferedWriter(fstream);
			JSONObject json = new JSONObject();
			for (Material m : Material.values()) {
				tick();
				json.put(m.toString(), m.getId());
			}
			json.write(out);
			out.close();
		} catch (Exception ex) {
			System.err.println("Error: " + ex.getMessage());
		}
	}

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

	private void tick() {
		this.percent++;
		if (this.percent == Math.round(total / 4)) {
			System.out.println("Converted 25%..");
		} else if (this.percent == Math.round(total / 2)) {
			System.out.println("Converted 50%..");
		} else if (this.percent == Math.round(total * 2 / 3)) {
			System.out.println("Converted 75%..");
		} else if (this.percent == total) {
			System.out.println("Converted 100%.. done");
		} else if (this.percent > total) {
			System.out.println("Error");
		}
	}
}
