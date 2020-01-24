import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

class SectionsLoader extends FileLoader {

  SectionsLoader(BufferedReader fileInput, LocationManager locationManager) {
    super(fileInput, locationManager);
  }

  @Override
  ArrayList<Section> loadItems() throws IOException {
    ArrayList<Section> sections = new ArrayList<>();

    String line = this.fileInput.readLine();

    /* Every line of the file is a different section formatted as:
        name | aisle numbers, separated by a whitespace | subsections, separated by a whitespace
     */

    while(line != null) {
      String[] sectionInfo = line.trim().split("\\|");

      for(String info : sectionInfo) {
        info = info.trim();
      }

      Section currentSection = new Section(sectionInfo[0]);
      sections.add(currentSection);

      String[] aisleNumbers = sectionInfo[1].split(" ");

      for(String num : aisleNumbers) {
        currentSection.addAisleNumber(Integer.parseInt(num));
      }

      String[] subsections = sectionInfo[2].split(" ");

      for(String subsection : subsections) {
        currentSection.addSubsection(subsection);
      }
    }

    return sections;
  }


  void sendSections(ArrayList<Section> loadedItems) {
    for(Section section : loadedItems) {
      locationManager.addSection(section);
    }

    Logger.addEvent("SectionsLoader", "sendSections",
        "Initial sections were loaded from .txt file.", false);
  }
}
