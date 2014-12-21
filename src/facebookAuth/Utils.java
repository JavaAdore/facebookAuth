package facebookAuth;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Utils {

	public static Map<String , String> objectToMap(Object object) {
		Map <String , String> map = new TreeMap();
		if (object != null) {
			for (Field field : object.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				try {
					Object obj = field.get(object);
					if(obj!=null)
					{
						map.put(camelCasingStylingToNormal(field.getName()) , String.valueOf(obj));
						
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
		}
		return map;
	}
	
	
	public static String camelCasingStylingToNormal(String camelStyledString) {
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 0;
        if (camelStyledString != null) {
            char[] chars = camelStyledString.toCharArray();
            for (int i = 0; i < camelStyledString.toCharArray().length; i++) {
                char currentChar = chars[i];
                if (i == 0) {
                    stringBuilder.append(String.valueOf(currentChar).toUpperCase());
                    continue;
                }
                if (((int) currentChar) >= 65 && ((int) currentChar) <= 90) {
                    stringBuilder.append(" ");
                    stringBuilder.append(String.valueOf(currentChar).toUpperCase());
                    counter++;
                } else {
                    stringBuilder.append(String.valueOf(currentChar).toLowerCase());

                }
            }
        }
        return stringBuilder.toString();
    }
}
