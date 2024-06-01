package swagger.api.demo.util;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonUtil {

    public static String convertMapToJsonString(Map<String, Object> dataMap) {
        final Pattern pattern = Pattern.compile("\\[[^\\[]*\\]", Pattern.MULTILINE);
        JSONObject payload = new JSONObject();
        if(dataMap==null || dataMap.isEmpty())
            return new JSONObject().toString();
        for(String key: dataMap.keySet()) {
            if(key.contains(".")) {
                JSONObject parent = payload;
                StringTokenizer st = new StringTokenizer(key,".");
                while (st.hasMoreTokens()) {
                    String nestedObjectKey = st.nextToken();
                    JSONObject nestedObject=parent;
                    if(pattern.matcher(nestedObjectKey).find()) {
                        String arrayName = nestedObjectKey.substring(0, nestedObjectKey.indexOf("["));
                        int arrayIndex = Integer.parseInt(nestedObjectKey.substring(nestedObjectKey.indexOf("[")+1, nestedObjectKey.length()-1));
                        JSONArray nestedArray = (JSONArray) parent.get(arrayName);
                        if(nestedArray==null) {
                            nestedArray = new JSONArray();
                            parent.put(arrayName, nestedArray);
                        }
                        if(!st.hasMoreTokens()) {
                            nestedArray.add(dataMap.get(key));
                        } else {
                            if(nestedArray.size()!=arrayIndex+1){
                                nestedObject  =new JSONObject();
                                nestedArray.add(nestedObject);
                            } else {
                                nestedObject = (JSONObject) nestedArray.get(arrayIndex);
                            }
                            parent.put(arrayName, nestedArray);
                        }
                    } else {
                        nestedObject = (JSONObject) parent.get(nestedObjectKey);
                        if(nestedObject==null)
                            nestedObject = new JSONObject();
                        if(!st.hasMoreTokens())
                            parent.put(nestedObjectKey, dataMap.get(key));
                        else
                            parent.put(nestedObjectKey, nestedObject);
                    }
                    parent = nestedObject;
                }
            } else {
                payload.put(key, dataMap.get(key));
            }
        }
        return payload.toString();
    }

    public static String convertListOfMapMapToJsonString(List<Map<String, Object>> dataMapList) {
        final Pattern pattern = Pattern.compile("\\[[^\\[]*\\]", Pattern.MULTILINE);
        JSONArray payload = new JSONArray();
        if(dataMapList==null || dataMapList.isEmpty())
            return new JSONObject().toString();
        for(Map<String, Object> dataMap: dataMapList) {
            JSONObject item = new JSONObject();
            for (String key : dataMap.keySet()) {
                if (key.contains(".")) {
                    JSONObject parent = item;
                    StringTokenizer st = new StringTokenizer(key, ".");
                    while (st.hasMoreTokens()) {
                        String nestedObjectKey = st.nextToken();
                        JSONObject nestedObject = parent;
                        if (pattern.matcher(nestedObjectKey).find()) {
                            String arrayName = nestedObjectKey.substring(0, nestedObjectKey.indexOf("["));
                            int arrayIndex = Integer.parseInt(nestedObjectKey.substring(nestedObjectKey.indexOf("[") + 1, nestedObjectKey.length() - 1));
                            JSONArray nestedArray = (JSONArray) parent.get(arrayName);
                            if (nestedArray == null) {
                                nestedArray = new JSONArray();
                                parent.put(arrayName, nestedArray);
                            }
                            if (!st.hasMoreTokens()) {
                                nestedArray.add(dataMap.get(key));
                            } else {
                                if (nestedArray.size() != arrayIndex + 1) {
                                    nestedObject = new JSONObject();
                                    nestedArray.add(nestedObject);
                                } else {
                                    nestedObject = (JSONObject) nestedArray.get(arrayIndex);
                                }
                                parent.put(arrayName, nestedArray);
                            }
                        } else {
                            nestedObject = (JSONObject) parent.get(nestedObjectKey);
                            if (nestedObject == null)
                                nestedObject = new JSONObject();
                            if (!st.hasMoreTokens())
                                parent.put(nestedObjectKey, dataMap.get(key));
                            else
                                parent.put(nestedObjectKey, nestedObject);
                        }
                        parent = nestedObject;
                    }
                } else {
                    item.put(key, dataMap.get(key));
                }
            }
            payload.add(item);
        }
        return payload.toString();
    }

    public static Object getPropertyValueFromResponseBody(ResponseBody responseBody, String propertyJsonPath) {
        return responseBody.jsonPath().get(propertyJsonPath);
    }

}
