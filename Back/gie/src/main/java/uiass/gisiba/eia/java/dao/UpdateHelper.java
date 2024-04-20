package uiass.gisiba.eia.java.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// This class is not currently used but I liked the idea behind it and I might use it later
public class UpdateHelper {

    // Method that calls setter methods in a generic way based on the attribute's name
    public static void callSetter(Object obj, String attributeName, Object value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Capitalize the first letter of the attribute name to match Java convention for setters
        String methodName = "set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
        
        // Get the method object for the setter
        Method method = obj.getClass().getMethod(methodName, value.getClass());
        
        // Call the setter method
        method.invoke(obj, value);
    }
}
