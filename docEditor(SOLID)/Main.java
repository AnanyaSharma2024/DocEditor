
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

// Interface define karta hai ki har DocumentElement mein render() method hona chahiye.
// Jo bhi class is interface ko implement karegi, usko render() method define karna hoga.
interface DocumentElement {

    // Abstract method: iska implementation child classes provide karengi.
    // String return karega, yaani element ka content.
    public abstract String render();
}

// TextElement class normal text ko represent karti hai.
// Ye DocumentElement interface ko implement kar rahi hai.
class TextElement implements DocumentElement {

    // Private variable: text ko directly class ke bahar access nahi kar sakte.
    // Ye Encapsulation ka example hai.
    private String text;

    // Constructor: object create hote time text ki value initialize karega.
    public TextElement(String text) {
        this.text = text;
    }

    // Interface ke render() method ka implementation.
    // @Override batata hai ki hum parent interface ka method implement kar rahe hain.
    @Override
    public String render() {

        // Stored text return kar diya.
        return text;
    }
}

// ImageElement class document mein image ko represent karti hai.
class ImageElement implements DocumentElement {

    // Image ka path store karne ke liye private variable.
    private String imagePath;

    // Constructor: image ka path receive karke initialize karega.
    public ImageElement(String imagePath) {
        this.imagePath = imagePath;
    }

    // ImageElement ke liye render() method ka implementation.
    @Override
    public String render() {

        // Image ka path ek readable format mein return kar rahe hain.
        return "[Image: " + imagePath + "]";
    }
}

// NewLineElement class document mein new line add karne ke liye hai.
class NewLineElement implements DocumentElement {

    // Constructor ki zaroorat nahi hai kyunki is class mein
    // koi variable initialize nahi karna.
    
    @Override
    public String render() {

        // \n ka meaning hai next line par jaana.
        return "\n";
    }
}

// TabSpaceElement class document mein tab space add karne ke liye hai.
class TabSpaceElement implements DocumentElement {

    @Override
    public String render() {

        // \t ka meaning hai tab space dena.
        return "\t";
    }
}

// Document class poore document ke elements ko manage karti hai.
class Document {

    // List mein different types ke DocumentElement objects store honge.
    // ArrayList actual implementation provide karta hai.
    // Ek hi list mein TextElement, ImageElement, NewLineElement etc. aa sakte hain.
    private List<DocumentElement> documentElements = new ArrayList<>();

    // Document mein ek naya element add karne ka method.
    // Parameter interface type ka hai, isliye koi bhi implementing class pass kar sakte hain.
    public void addElement(DocumentElement element) {

        // Received element ko list mein add kar diya.
        documentElements.add(element);
    }

    // Poore document ka content ek String mein convert karne ka method.
    public String render() {

        // StringBuilder efficiently multiple Strings ko combine karta hai.
        StringBuilder result = new StringBuilder();

        // List ke har element par loop chalega.
        for (DocumentElement element : documentElements) {

            // Har object ka apna render() method call hoga.
            // Runtime par decide hoga ki TextElement, ImageElement ya koi aur method chalega.
            // Ye Runtime Polymorphism ka example hai.
            result.append(element.render());
        }

        // StringBuilder ko normal String mein convert karke return kar diya.
        return result.toString();
    }
}

// Persistence interface batata hai ki data save karne ka method hona chahiye.
// Data kahan save hoga, ye implementing class decide karegi.
interface Persistence {

    // Abstract method: save karne ka implementation child classes provide karengi.
    void save(String data);
}

// FileStorage class document ko file mein save karne ki responsibility rakhti hai.
// Ye Persistence interface implement karti hai.
class FileStorage implements Persistence {

    @Override
    public void save(String data) {

        // try block mein file writing ka operation perform hoga.
        try {

            // FileWriter document.txt file ko writing ke liye open/create karta hai.
            FileWriter outFile = new FileWriter("document.txt");

            // Received document data ko file mein write kar diya.
            outFile.write(data);

            // FileWriter ko close karna zaroori hai.
            // Isse resources release ho jaate hain.
            outFile.close();

            // Successful saving ka message.
            System.out.println("Document saved to document.txt");

        } catch (IOException e) {

            // Agar file open/write karte time error aaye,
            // toh catch block execute hoga.
            System.out.println("Error: Unable to open file for writing.");
        }
    }
}

// DBStorage class database mein document save karne ke liye hai.
class DBStorage implements Persistence {

    @Override
    public void save(String data) {

        // Future mein yahan database connection aur insert query likhenge.
        // Abhi ye sirf placeholder implementation hai.
    }
}

// DocumentEditor main controller class hai.
// Ye Document aur Persistence objects ko coordinate karti hai.
class DocumentEditor {

    // Document object: document ke saare elements manage karega.
    private Document document;

    // Persistence object: decide karega ki document kahan save hoga.
    // Ye FileStorage ya DBStorage ka object ho sakta hai.
    private Persistence storage;

    // Rendered document ka output store karne ke liye variable.
    // Initially empty String hai.
    private String renderedDocument = "";

    // Constructor Dependency Injection ka example hai.
    // Document aur Persistence objects bahar se receive ho rahe hain.
    public DocumentEditor(Document document, Persistence storage) {

        // Received Document object ko instance variable mein store kiya.
        this.document = document;

        // Received Persistence implementation ko store kiya.
        this.storage = storage;
    }

    // Document mein text add karne ka method.
    public void addText(String text) {

        // TextElement ka object create karke document mein add kar diya.
        document.addElement(new TextElement(text));
    }

    // Document mein image add karne ka method.
    public void addImage(String imagePath) {

        // ImageElement ka object create karke document mein add kar diya.
        document.addElement(new ImageElement(imagePath));
    }

    // Document mein new line add karne ka method.
    public void addNewLine() {

        // NewLineElement ka object document mein add kiya.
        document.addElement(new NewLineElement());
    }

    // Document mein tab space add karne ka method.
    public void addTabSpace() {

        // TabSpaceElement ka object document mein add kiya.
        document.addElement(new TabSpaceElement());
    }

    // Document ka final content generate karne ka method.
    public void renderDocument() {

        // Document class ka render() method call karke output generate kiya.
        renderedDocument = document.render();

        // Final rendered document print kar diya.
        System.out.println(renderedDocument);
    }

    // Document ko selected storage system mein save karne ka method.
    public void saveDocument() {

        // Storage interface ka save() method call hoga.
        // Actual implementation FileStorage ya DBStorage decide karegi.
        storage.save(renderedDocument);
    }
}

// Main class: Java program execution yahin se start hota hai.
public class Main {

    public static void main(String[] args) {

        // Document class ka object create kiya.
        Document document = new Document();

        // FileStorage ka object create kiya.
        // Iska use document ko text file mein save karne ke liye hoga.
        Persistence storage = new FileStorage();

        // Document aur storage objects DocumentEditor ko pass kiye.
        DocumentEditor editor = new DocumentEditor(document, storage);

        // Document mein different elements add kar rahe hain.
        editor.addText("Hello, World!");
        editor.addNewLine();
        editor.addText("This is my document editor.");
        editor.addNewLine();
        editor.addTabSpace();
        editor.addText("This text starts after a tab.");
        editor.addNewLine();
        editor.addImage("images/photo.png");

        // Document ka final content generate aur print kar rahe hain.
        editor.renderDocument();

        // Rendered document ko file mein save kar rahe hain.
        editor.saveDocument();
    }
}
