package message;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 A simple class for wrapping a JSON string. The object passed is actually generic.
 @author Razvan
 @param <T> The type of the object being serialized
 */
public class JSONObject<T>
{
    
    /**
     Construct a JSON object in by serializing the object passed
     @param CrtObject The object meant to be serialized
     */
    public JSONObject(final T CrtObject)
    {
        this.JSONProperties = "";
        Gson JSONSerializer = new Gson();
        JSONProperties = JSONSerializer.toJson(CrtObject);
        genericObjectType = (Class<T>) CrtObject.getClass();
    }
    
    /**
     Create a JSON object from a source JSON
     @param source The source object
     */
    public JSONObject(final JSONObject source)
    {
        JSONProperties = source.JSONProperties;
    }
    
    // - Mutators
    
    /**
     Reads the data from the specified JSON file
     @param fileName The file name
     @throws java.io.FileNotFoundException
     */
    public void readFromFile(String fileName) throws FileNotFoundException, IOException
    {
        JSONProperties = "";
        if (fileName.matches(REGEX_EXT) == false)
            fileName += EXT;
        
        BufferedReader inputStream = new BufferedReader(new FileReader(fileName));
        
        String headerLine = inputStream.readLine();
        if (headerLine.equals(FILE_HEADER + getWrappedObjectClassDetails()))
        {
            String crtLine = "";
            while (null != (crtLine = inputStream.readLine()))
                JSONProperties += crtLine;
            inputStream.close();
        }
        else
        {
            inputStream.close();
            throw new IOException();
        }
    }
    
    // - Accessors

    /**
     Recreates an object of type T from the current JSON object
     @return The new object recreated from its JSON contents
     */
    public T recreateObject()
    {
        Gson JSONSerializer = new Gson();
        T newObject = JSONSerializer.fromJson(JSONProperties, genericObjectType);
        return newObject;
    }
    
    /**
     Serialize the contents into the file specified
     @param fileName The file name
     @throws java.io.IOException
     */
    public void writeToFile(String fileName) throws IOException
    {  
        if (fileName.matches(REGEX_EXT) == false)
            fileName += EXT;
        
        BufferedWriter outputStream = new BufferedWriter(new FileWriter(fileName));
        outputStream.write(FILE_HEADER + getWrappedObjectClassDetails() + "\n");
        outputStream.write(JSONProperties);
        
        outputStream.close();
    }
    
    /**
     Prints the contents to std out
     */
    public void printToStdOut()
    {
        System.out.println("JSON Object Type: " + genericObjectType.toString());
        System.out.print(JSONProperties);
        System.out.println("\n");
    }
    
    private String getWrappedObjectClassDetails()
    {
        String detailsObject = genericObjectType.toString();
        detailsObject = detailsObject + " [hash: " + detailsObject.hashCode() + "]";
        return detailsObject;
    }
    
    private String JSONProperties;
    private static final String FILE_HEADER = "JSON File -> Type: ";
    private static final String REGEX_EXT = ".+.json";
    private static final String EXT = ".json";
    private Class<T> genericObjectType;
}
