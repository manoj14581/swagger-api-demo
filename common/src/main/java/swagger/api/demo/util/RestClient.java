package swagger.api.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RestClient {
    private RequestSpecification requestSpecification = null;
    private ObjectMapper mapper = new ObjectMapper();
    private Map<String, String> headers = new HashMap<>();
    final String[] accepts = {
            "application/json;odata.metadata&#x3D;minimal;odata.streaming&#x3D;true", "application/json;odata.metadata&#x3D;minimal;odata.streaming&#x3D;false", "application/json;odata.metadata&#x3D;minimal", "application/json;odata.metadata&#x3D;full;odata.streaming&#x3D;true", "application/json;odata.metadata&#x3D;full;odata.streaming&#x3D;false", "application/json;odata.metadata&#x3D;full", "application/json;odata.metadata&#x3D;none;odata.streaming&#x3D;true", "application/json;odata.metadata&#x3D;none;odata.streaming&#x3D;false", "application/json;odata.metadata&#x3D;none", "application/json;odata.streaming&#x3D;true", "application/json;odata.streaming&#x3D;false", "application/json", "application/xml", "text/plain", "application/octet-stream", "text/json"
    };
    private RestClient(String baseUri) {
        //Private constructor..
        requestSpecification = RestAssured.given().contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        this.requestSpecification.baseUri(baseUri);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public static RestClient getInstance(String baseUri) {
        return new RestClient(baseUri);
    }

    public RestClient addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public Response execute(String path, MultiValueMap<String, String> queryParams, HttpMethod method, Object body) throws ApiException {
        try {
            Response response;
            BaseTest.log("<b>Endpoint URL</b> \n" + path);
            BaseTest.log("<b>Request Headers</b> \n" + headers.toString());
            if (!headers.isEmpty()) {
                Set<String> headerKeySet = headers.keySet();
                for (String key : headerKeySet) {
                    this.requestSpecification.header(key, headers.get(key));
                }
            }
            StringBuilder resourcePath = new StringBuilder(path);
            if(queryParams!=null && !queryParams.isEmpty()) {
                boolean isFirst=true;
                resourcePath.append("?");
                Set<String> keySet = queryParams.keySet();
                for(String key: keySet) {
                    if(!isFirst)
                        resourcePath.append(String.format("%s=%s", key, queryParams.get(key)));
                    else
                        resourcePath.append(String.format(",%s=%s", key, queryParams.get(key)));
                    isFirst = false;
                }
            }
            if(queryParams!=null && !queryParams.isEmpty())
                BaseTest.log("<b>query params</b> \n" + queryParams);
            if (body != null)
                BaseTest.log("<b>Payload</b> \n" + body);
            switch (method) {
                case GET:
                    if(body!=null)
                        response = this.requestSpecification.when().body(body).request().get(resourcePath.toString()).then().log().all().extract().response();
                    else
                        response = this.requestSpecification.when().request().get(resourcePath.toString()).then().log().all().extract().response();
                    break;
                case POST:
                    if(body!=null)
                        response = this.requestSpecification.when().body(body).request().post(resourcePath.toString()).then().log().all().extract().response();
                    else
                        response = this.requestSpecification.when().request().post(resourcePath.toString()).then().log().all().extract().response();
                    break;
                case DELETE:
                    if(body!=null)
                        response = this.requestSpecification.when().body(body).request().delete(resourcePath.toString()).then().log().all().extract().response();
                    else
                        response = this.requestSpecification.when().request().delete(resourcePath.toString()).then().log().all().extract().response();
                    break;
                case PUT:
                    if(body!=null)
                        response = this.requestSpecification.when().body(body).request().put(resourcePath.toString()).then().log().all().extract().response();
                    else
                        response = this.requestSpecification.when().request().put(resourcePath.toString()).then().log().all().extract().response();
                    break;
                case PATCH:
                    if(body!=null)
                        response = this.requestSpecification.when().body(body).request().patch(resourcePath.toString()).then().log().all().extract().response();
                    else
                        response = this.requestSpecification.when().request().patch(resourcePath.toString()).then().log().all().extract().response();
                    break;
                default:
                    throw new ApiException("RestAssuredClient doesn't support given method - " + method);
            }
            BaseTest.log("<b>Response status</b> \n" + response.getStatusCode());
            BaseTest.log("<b>Response</b> \n" + response.asString());
            return response;
        } catch(Throwable ex) {
            BaseTest.log("<b>Response</b> \n" + ex.getMessage());
            throw ex;
        }

    }

}
