import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * In control of Section.
 */
public class LocationManager {

	private ArrayList<Section> sectionList;
	private HashMap<Section, ArrayList<Product>> sectionAndProduct;

	LocationManager() {
		this.sectionList = new ArrayList<>();
		this.sectionAndProduct = new HashMap<>();
		ArrayList<String> sell = new ArrayList<>();
		Collections.addAll(sell, "produce", "dairy", "meat", "packaged products",
				"frozen food", "cleaning supplies", "pet food", "kitchen wares", "seasonal products");
		for (String x : sell) {
			Section section = new Section(x);
			this.sectionList.add(section);
		}
	}


	LocationManager(String filePath) throws ClassNotFoundException, IOException {
		this();
		File file = new File(filePath);

		if (file.exists() && !file.isDirectory()) {
			readFromFile(filePath);
		} else {
			file.createNewFile();
		}
	}


	private void readFromFile(String filePath) throws ClassNotFoundException {
		try {
			InputStream file = new FileInputStream(filePath);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);

			// Deserialize
			sectionList = (ArrayList<Section>) input.readObject();
			sectionAndProduct = (HashMap<Section, ArrayList<Product>>) input.readObject();
			Logger.addEvent("LocationManager", "readFromFile",
					"Loaded LocationManager from " + filePath, false);
		} catch (IOException e) {
			System.out.println("Unable to load Location Manager from file.");
			Logger.addEvent("LocationManager", "readFromFile",
					"IOException-unable to load LocationManager from " + filePath,
					false);
		}
	}


	/**
	 * Give back section list.
	 */
	ArrayList<Section> getSectionList() {
		return sectionList;
	}

	/**
	 * Give back section and product hashmap.
	 */
	HashMap<Section, ArrayList<Product>> getSectionProductHashMap() {
		return sectionAndProduct;
	}


	/**
	 * Add section into the list with input section.
	 */
	void addSection(Section sectionName) {
		if (!sectionList.contains(sectionName)) {
			sectionList.add(sectionName);
			Logger.addEvent("LocationManager", "addSection", "added "
					+ "section " + sectionName, false);
		}
	}


	/**
	 * Get section object with inputted section name.
	 */
	Section getSection(String sectionName) {
		for (Section section : sectionList) {
			if (section.getSectionName().equals(sectionName)) {
				return section;
			}
		}
		return null;
	}


	/**
	 * Change the location in specific section.
	 */
	public void changeLocation(Product productName, int aisleNum) {
		// find the value that contains such product and delete product from that aisle.
		productName.setAisle(aisleNum);
		// add the removed product to the input aisleNum.
		Logger.addEvent("LocationManager", "changeLocation",
				"changed " + productName + " to aisle " + aisleNum + " in " +
						productName.getSection() + " section.", false);
	}


	/**
	 * Add product to the correct section.
	 */
	public void addProduct(Section section, Product product, int aisleNum, String subSection){
		if(sectionList.contains(section)){
			sectionAndProduct.put(section, new ArrayList<>());
			sectionAndProduct.get(section).add(product);
			section.addSubsectionAndAisleNum(product, aisleNum, subSection);
			Logger.addEvent("LocationManager", "addProduct", "added product -"
							+ product.getName() + "- to aisle " + aisleNum + ", subsection '" + subSection +
							"' in section '" + section.getSectionName() + "',",false );
		}
		else{
			Logger.addEvent("LocationManager", "addProduct", "Could not add product -"
					+ product.getName() + "- to aisle " + aisleNum + ", subsection '" + subSection +
					"' in section '" + section.getSectionName() + "',",false );
		}
	}


	/**
	 * Print all products in inputted Section.
	 */
	public void printProductsInSection(Section sectionName) {
		FileWriter writer = writer("ProductsInSection",
				"Section-" + sectionName.getSectionName());
		try {
			writer.write("Products in section '" + sectionName.getSectionName() + "':\n");
			for (Product product : sectionName.getAllProducts()) {
				writer.write(product.getName());
			}
			Logger.addEvent("LocationManager", "printProductsInSection",
					"the products of section '" + sectionName.getSectionName() +
							"' was printed to a file", false);
			writer.close();
		} catch (IOException e) {
			Logger.addEvent("LocationManager", "printProductsInSection",
					"IOException: the products of aisle " + sectionName.getSectionName()
							+ " cannot be printed", false);
		}
	}


	/**
	 * printAisleNumByProduct's helper function.
	 */
	private void updateProduct(String textToWrite) {
		try {
			FileWriter writer = new FileWriter("Search-By-Product/Product-Search.txt", true);
			writer.write("Searched product: " + textToWrite + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Print the aisleNumber that contains the inputted product. Used by User.
	 */
	public void printAisleNumByProduct(Product product) {
		File file = new File("Search-By-Product");
		file.mkdirs();
		String textToWrite = product.getName() + " is at aisle " + product.getAisle();
		try {
			FileWriter write = new FileWriter("Search-By-Product/Product-Search.txt");
			BufferedWriter bw = new BufferedWriter(write);
			bw.close();
			updateProduct(textToWrite);
			Logger.addEvent("LocationManager", "printAisleNumByProduct",
					"printed aisle number and product",false);
		} catch (IOException e) {
			e.printStackTrace();
			Logger.addEvent("LocationManager", "printAisleNumByProduct",
					"Can't print aisle number or product",false);
		}
	}


	/**
	 * printProductsByAisleNum's helper function.
	 */

	private ArrayList<Product> ProductsByAisleNum(int aisleNum) {
		ArrayList<Product> all = new ArrayList<>();
		for (Section section : sectionList) {
			for (Product product : section.getProductForAisleNum(aisleNum)) {
				if (!all.contains(product)) {
					all.add(product);
				}
			}
		}
		return all;
	}


	/**
	 * Create file that writes all the products in the inputted section and aislenumber. Used by
	 * Manager or Reshelvers.
	 */
	public void printProductsByAisleNum(int aisleNum) {
		FileWriter writer = writer("ProductByAisleNum",
				"Aisle-Number-" + String.valueOf(aisleNum));
		try {
			writer.write("Products in aisle " + String.valueOf(aisleNum) + ":\n");
			for (Product product : ProductsByAisleNum(aisleNum)) {
				writer.write(product.getName());
			}
			Logger.addEvent("LocationManager", "printProductsByAisleNum",
					"the products of aisle '" + aisleNum + "' was printed to a file",
					false);
			writer.close();
		} catch (IOException e) {
			Logger.addEvent("LocationManager", "printProductsByAisleNum",
					"IOException: the products of aisle " + aisleNum + " cannot be printed",
					false);
		}
	}


	private FileWriter writer(String directory, String fileName) {
		try {
			//create the directory for the file
			File file = new File(directory);
			file.mkdirs();
			//create the file
			FileWriter writer = new FileWriter(directory + "/" + fileName + ".txt");
			return writer;
		} catch (IOException e) {
			Logger.addEvent("LocationManager", "writer", "file not made", false);
			return null;
		}
	}
}