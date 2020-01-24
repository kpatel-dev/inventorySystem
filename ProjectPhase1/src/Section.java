import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Section implements Serializable{

	private String sectionName;

	private ArrayList<Integer> aisleNumbers;

	private ArrayList<String> subSections;

	HashMap<Integer, ArrayList<Product>> aisleAndProduct;

	private HashMap<String, ArrayList<Product>> subsectionAndProduct;

	private ArrayList<Product> productList;


	/**
	 * Creates an Hashmap for corresponding product to aisle and product to subsection.
	 */
	Section(String Name) {
		ArrayList<String> sell = new ArrayList<>();
		Collections.addAll(sell, "produce", "dairy", "meat", "packaged products",
				"frozen food", "cleaning supplies", "pet food", "kitchen wares", "seasonal products");
		if (sell.contains(Name)) {
			this.sectionName = Name;
			this.aisleAndProduct = new HashMap<>();
			this.subsectionAndProduct = new HashMap<>();
			this.productList = new ArrayList<>();
			this.aisleNumbers = new ArrayList<>();
		} else {
			System.out.println("This can't be a section name, please select from the following: "
					+ sell.toString());
		}
	}


	/**
	 * Get all products in this section.
	 */
	ArrayList<Product> getAllProducts(){
		return productList;
	}


	/**
	 * Get the name of this section.
	 */
	String getSectionName() {
		return sectionName;
	}


	/**
	 * Adds a product in section together with aisle and subsection information.
	 */
	void addSubsectionAndAisleNum(Product product, int aisleNum, String subsection) {
		// check if the aisle number had already exist.
		if (!aisleAndProduct.containsKey(aisleNum)) {
			aisleAndProduct.put(aisleNum, new ArrayList<>());
		}
		if (!subsectionAndProduct.containsKey(subsection)) {
			subsectionAndProduct.put(subsection, new ArrayList<>());
		}
		// add product to both hashmaps that corresponds to aisle number and subsection.
		aisleAndProduct.get(aisleNum).add(product);
		subsectionAndProduct.get(subsection).add(product);
		productList.add(product);
	}


	/**
	 * Add only one int to the arraylist of all aisleNumbers in this section.
	 */
	void addAisleNumber(int num) {
		aisleNumbers = new ArrayList<>(aisleAndProduct.keySet());
		if (aisleNumbers.contains(num)) {
			aisleNumbers.add(num);
		}
	}

	/**
	 * Get the arraylist of all aisle numbers.
	 */
	ArrayList<Integer> getAisleNumForSection() {
		return aisleNumbers;
	}


	/**
	 * Gets the product's belonged aisle number when inputting product name.
	 */
	String getAisleNumForProduct(Product product) {
		// created "none" because in the for loop it has to return something.
		String none = "";
		// loop through the hashmap to find which key have it's corresponding value contains product.
		for (Map.Entry<Integer, ArrayList<Product>> e : aisleAndProduct.entrySet()) {
			if (e.getValue().contains(product)) {
				return e.getKey().toString();
			} else {
				System.out.println("No such product in Section" + sectionName);
			}
		}return none;
	}


	/**
	 * Get all the Products from the input aislenumber.
	 */
	ArrayList<Product> getProductForAisleNum(int num){
		if (aisleAndProduct.containsKey(num)) {
			return aisleAndProduct.get(num);
		}else{
			return new ArrayList<>();
		}
	}


	/**
	 * Add only one String to the arraylist of all subsections in this section.
	 */
	 void addSubsection(String sub) {
		subSections = new ArrayList<>(subsectionAndProduct.keySet());
		if (!subSections.contains(sub)) {
			subSections.add(sub);
		}
		Collections.sort(subSections);
	}


	/**
	 * Get the arraylist of all aisle numbers.
	 */
	protected ArrayList<String> getSubSectionForSection() {
		return subSections;
	}


	/**
	 * Gets the product's belonged subsection when inputting product name.
	 */
	protected String getSubsectionForProduct(Product product) {
		String none = "";
		// loop through the hashmap to find which key have it's corresponding value contains product.
		for (Map.Entry<String, ArrayList<Product>> e : subsectionAndProduct.entrySet()) {
			if (e.getValue().contains(product)) {
				return e.getKey();
			} else {
				System.out.println("No such product in Section " + sectionName);
			}
		} return none;
	}


	/**
	 * Gets all products using subsection;
	 */
	protected String getProductForSubsection(String subsection){
		String none = "";
		for (Map.Entry<String, ArrayList<Product>> e : subsectionAndProduct.entrySet()){
			if(e.getKey().equals(subsection)){
				return e.getValue().toString();
			}else{
				System.out.println("No subsection called" + subsection);
			}
		} return none;
	}
}