package net.jsunit.model;

import net.jsunit.XmlRenderable;
import net.jsunit.utility.StringUtility;
import org.jdom.Element;

import java.util.List;

public class Browser implements XmlRenderable {

    public static final String DEFAULT_SYSTEM_BROWSER = "default";

    private String startCommand;
    private String killCommand;
    private String fullFileName;
    private int id;
    private String displayName;
    private BrowserType type;
    public static final String BROWSER = "browser";
    public static final String FULL_FILE_NAME = "fullFileName";
    public static final String ID = "id";

    public Browser(String fullFileName, int id) {
        this.fullFileName = fullFileName;
        this.id = id;
        List<String> list = StringUtility.listFromSemiColonDelimitedString(fullFileName);
        this.startCommand = list.size() >= 1 ? list.get(0) : null;
        this.killCommand = list.size() >= 2 ? list.get(1) : null;
        this.type = BrowserType.resolve(startCommand);
        if (list.size() >= 3)
            this.displayName = list.get(2);
        else if (type != BrowserType.UNKNOWN)
            this.displayName = type.getDisplayName();
        else
            this.displayName = startCommand;
    }

    public String getStartCommand() {
        return startCommand;
    }

    public String getKillCommand() {
        return killCommand;
    }

    public int getId() {
        return id;
    }

    public boolean hasId(int anId) {
        return id == anId;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Browser browser = (Browser) o;

        if (id != browser.id) return false;
        if (fullFileName != null ? !fullFileName.equals(browser.fullFileName) : browser.fullFileName != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (fullFileName != null ? fullFileName.hashCode() : 0);
        result = 29 * result + id;
        return result;
    }

    public boolean isDefault() {
        return startCommand.equals(DEFAULT_SYSTEM_BROWSER);
    }

    public String getDisplayName() {
        return displayName;
    }

    public BrowserType getType() {
        return type;
    }

    public String getFullFileName() {
        return fullFileName;
    }

    public boolean conflictsWith(Browser another) {
        return getType().conflictsWith(another.getType());
    }

    public String getLogoPath() {
        return getType().getLogoPath();
    }

    public Element asXml() {
        Element browserElement = new Element(BROWSER);
        Element fullFileNameElement = new Element(FULL_FILE_NAME);
        fullFileNameElement.setText(getFullFileName());
        browserElement.addContent(fullFileNameElement);
        Element idElement = new Element(ID);
        idElement.setText(String.valueOf(getId()));
        browserElement.addContent(idElement);
        Element displayNameElement = new Element("displayName");
        displayNameElement.setText(getDisplayName());
        browserElement.addContent(displayNameElement);
        Element logoElement = new Element("logoPath");
        logoElement.setText(getLogoPath());
        browserElement.addContent(logoElement);
        return browserElement;
    }

    public static Browser buildFrom(Element element) {
        String fullFileName = element.getChild(FULL_FILE_NAME).getTextTrim();
        int id = Integer.parseInt(element.getChild(ID).getTextTrim());
        return new Browser(fullFileName, id);
    }
}
