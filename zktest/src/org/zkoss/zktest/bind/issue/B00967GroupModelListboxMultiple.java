package org.zkoss.zktest.bind.issue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.GroupsModelArray;

/**
 *
 */
public class B00967GroupModelListboxMultiple {

	private MyGroupsModelArray groupsModel;

	
	Set<Food> selected;
	
	public B00967GroupModelListboxMultiple() {
		groupsModel = new MyGroupsModelArray(FoodData.getAllFoodsArray(), new FoodComparator());
		groupsModel.setMultiple(true);
	}

	public MyGroupsModelArray getGroupsModel() {
		return groupsModel;
	}

	public Set<Food> getSelected() {
		return selected;
	}

	@NotifyChange({"selected","sortedName"})
	public void setSelected(Set<Food> selected) {
		this.selected = selected;
	}
	
	public List<Food> getSortedName(){
		if(selected==null) return null;
		List sorted = new ArrayList();
		for(Food f:selected){
			sorted.add(f.getName());
		}
		Collections.sort(sorted);
		return sorted;
		
	}
	
	@Command @NotifyChange({"selected","sortedName"})
	public void select(){
		selected = new HashSet<Food>();
		selected.add((Food)groupsModel.getChild(0,0));
		selected.add((Food)groupsModel.getChild(1,1));
	}

	public static class MyGroupsModelArray extends GroupsModelArray {
		public MyGroupsModelArray(Object[] data, Comparator cmpr) {
			super(data, cmpr);
		}

		protected Object createGroupHead(Object[] groupdata, int index, int col) {
			return new Object[] { groupdata[0], index, col };
		}

		// Create GroupFoot Data
		protected Object createGroupFoot(Object[] groupdata, int index, int col) {
			// Return the sum number of each group
			return groupdata.length;
		}
	}

	public static class FoodData {

		private static List<Food> foods = new ArrayList<Food>();
		static {
			foods.add(new Food("Vegetables", "Asparagus"));
			foods.add(new Food("Vegetables", "Beets"));
			foods.add(new Food("Seafood", "Salmon"));
			foods.add(new Food("Seafood", "Shrimp"));
			foods.add(new Food("Fruits", "Apples"));
		}

		public static List getAllFoods() {
			return foods;
		}

		public static Object[] getAllFoodsArray() {
			return foods.toArray();
		}

		// This Method only used in "Data Filter" Demo
		public static List getFilterFoods(String filter1, String filter2, String filter3) {
			List<Food> somefoods = new ArrayList<Food>();
			for (Iterator<Food> i = foods.iterator(); i.hasNext();) {
				Food tmp = i.next();
				if (tmp.getCategory().toLowerCase(java.util.Locale.ENGLISH).indexOf(filter1.trim().toLowerCase(java.util.Locale.ENGLISH)) >= 0
						&& tmp.getName().toLowerCase(java.util.Locale.ENGLISH).indexOf(filter2.trim().toLowerCase(java.util.Locale.ENGLISH)) >= 0) {
					somefoods.add(tmp);
				}
			}
			return somefoods;
		}

		// This Method only used in "Header and footer" Demo
		public static List<Food> getFoodsByCategory(String category) {
			List<Food> somefoods = new ArrayList<Food>();
			for (Iterator<Food> i = foods.iterator(); i.hasNext();) {
				Food tmp = i.next();
				if (tmp.getCategory().equals(category))
					somefoods.add(tmp);
			}
			return somefoods;
		}
	}

	public static class Food {
		private String category;
		private String name;

		public Food(String category, String name) {
			super();
			this.category = category;
			this.name = name;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	public class FoodComparator implements Comparator, Serializable {
		
		public int compare(Object o1, Object o2) {
			Food data = (Food) o1;
			Food data2 = (Food) o2;
			return data.getCategory().compareTo(data2.getCategory().toString());
		}

	}

}
