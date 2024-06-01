package swagger.api.demo.wrapper;

import swagger.api.demo.generated.handler.ApiClient;
import swagger.api.demo.util.ApiException;
import swagger.api.demo.util.BaseTest;
import swagger.api.demo.util.CustomObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApiClientWrapper extends ApiClient {
    private ApiClient apiClient = null;
    private RequestSpecification requestSpecification = null;
    private ObjectMapper mapper = null;
    private final Map<String, String> headers = new HashMap<>();
    final String[] accepts = {
            "application/json;odata.metadata&#x3D;minimal;odata.streaming&#x3D;true", "application/json;odata.metadata&#x3D;minimal;odata.streaming&#x3D;false", "application/json;odata.metadata&#x3D;minimal", "application/json;odata.metadata&#x3D;full;odata.streaming&#x3D;true", "application/json;odata.metadata&#x3D;full;odata.streaming&#x3D;false", "application/json;odata.metadata&#x3D;full", "application/json;odata.metadata&#x3D;none;odata.streaming&#x3D;true", "application/json;odata.metadata&#x3D;none;odata.streaming&#x3D;false", "application/json;odata.metadata&#x3D;none", "application/json;odata.streaming&#x3D;true", "application/json;odata.streaming&#x3D;false", "application/json", "application/xml", "text/plain", "application/octet-stream", "text/json"
    };

    public void injectCustomRestTemplate(ObjectMapper objectMapper) {
        try {
            RestTemplate customRestTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setObjectMapper(objectMapper);
            customRestTemplate.getMessageConverters().clear();
            customRestTemplate.getMessageConverters().add(converter);
            Class <?> clazz = this.apiClient.getClass();
            Field restTemplate = clazz.getDeclaredField("restTemplate");
            restTemplate.setAccessible(true);
            restTemplate.set(this.apiClient, customRestTemplate);
        } catch (Throwable ex) {
            //DO nothing..
        }
    }

    private ApiClientWrapper(String baseUri, ApiClient apiClient) throws IllegalAccessException, NoSuchFieldException {
        //Private constructor..
        ObjectMapper objectMapper = CustomObjectMapper.generateMapper();
        if(apiClient!=null) {
            this.apiClient = apiClient;
            this.apiClient.setBasePath(baseUri);
            //Comment out below line to disable API Contract verification
           injectCustomRestTemplate(objectMapper);
        }
        requestSpecification = RestAssured.given().contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        this.requestSpecification.baseUri(baseUri);
        this.mapper = objectMapper;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public static ApiClientWrapper getInstance(String baseUri, ApiClient apiClient) {
        try {
            return new ApiClientWrapper(baseUri, apiClient);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ApiClientWrapper addHeader(String key, String value) {
        headers.put(key, value);
        if(this.apiClient!=null)
            this.apiClient.addDefaultHeader(key, value);
        return this;
    }

    public Response invokeAPI(String path, Map<String, Object> queryParams, HttpMethod method, Object body) throws ApiException {
        return invokeAPI(path, queryParams, method,body, null);
    }

    @SafeVarargs
    public final Response invokeAPI(String path, Map<String, Object> queryParams, HttpMethod method, Object body, Map<String, File>... multipartDetails) throws ApiException {
        RequestSpecification specification = this.requestSpecification.when();
        try {
            Response response;
            StringBuilder resourcePath = new StringBuilder(path);
            if(queryParams!=null && !queryParams.isEmpty()) {
                boolean isFirst=true;
                resourcePath.append("?");
                Set<String> keySet = queryParams.keySet();
                for(String key: keySet) {
                    if(isFirst)
                        resourcePath.append(String.format("%s=%s", key, queryParams.get(key)));
                    else
                        resourcePath.append(String.format("&%s=%s", key, queryParams.get(key)));
                    isFirst = false;
                }
                BaseTest.log("<b>Endpoint URL</b> \n" + resourcePath);
            } else {
                BaseTest.log("<b>Endpoint URL</b> \n" + path);
            }
            //BaseTest.log("<b>Request Headers</b> \n" + headers.toString());
            if (!headers.isEmpty()) {
                Set<String> headerKeySet = headers.keySet();
                for (String key : headerKeySet) {
                    specification.header(key, headers.get(key));
                }
            }

            if(queryParams!=null && !queryParams.isEmpty())
                BaseTest.log("<b>query params</b> \n" + queryParams);
            if (body != null) {
                BaseTest.log("<b>Payload</b> \n" + body);
                specification.body(body);
            }

            if(multipartDetails!=null && multipartDetails.length>0) {
                for(String key: multipartDetails[0].keySet()) {
                    specification.multiPart(key, multipartDetails[0].get(key), "multipart/form-data");
                }
            }
            switch (method) {
                case GET:
                        response = specification.request().get(resourcePath.toString()).then().log().all().extract().response();
                    break;
                case POST:
                        response = specification.request().post(resourcePath.toString()).then().log().all().extract().response();
                    break;
                case DELETE:
                        response = specification.request().delete(resourcePath.toString()).then().log().all().extract().response();
                    break;
                case PUT:
                        response = specification.request().put(resourcePath.toString()).then().log().all().extract().response();
                    break;
                case PATCH:
                        response = specification.request().patch(resourcePath.toString()).then().log().all().extract().response();
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

    public <T> ResponseEntity<T> invokeAPI(String path, HttpMethod method, MultiValueMap<String, String> queryParams, Object body, HttpHeaders headerParams, MultiValueMap<String, Object> formParams, ParameterizedTypeReference<T> returnType) throws RestClientException {
        if(this.apiClient==null)
            throw new RestClientException("Api client is not initialized!");
        try {
            BaseTest.log("<b>Endpoint URL</b> \n" + path);
            //BaseTest.log("<b>Request Headers</b> \n" + headers.toString());
            if(queryParams!=null && !queryParams.isEmpty())
                BaseTest.log("<b>query params</b> \n" + queryParams);
            if (body != null && !(body instanceof String)) {
                body = mapper.writeValueAsString(body);
                BaseTest.log("<b>Payload</b> \n" + body);
            }
            List<MediaType> accept2 =this.apiClient.selectHeaderAccept(accepts);
            String[] contentTypes2 = {  };
            MediaType contentType2 = this.apiClient.selectHeaderContentType(contentTypes2);
            String[] authNames2 = new String[] { };
            if(queryParams == null)
                queryParams = new LinkedMultiValueMap<>();
            if(headerParams == null)
                headerParams = new HttpHeaders();
            if(formParams == null)
                formParams = new LinkedMultiValueMap<>();
            ResponseEntity<T> responseEntity = this.apiClient.invokeAPI(path, method, queryParams, body, headerParams, formParams, accept2, contentType2, authNames2, returnType);
            BaseTest.log("<b>Response status</b> \n" + responseEntity.getStatusCode());
            BaseTest.log("<b>Response</b> \n" + responseEntity.getBody());
            return responseEntity;
        } catch(Throwable ex) {
            BaseTest.log("<b>Response</b> \n" + ex.getMessage());
            throw new RestClientException(ex.getMessage());
        }

    }

    public <T> ResponseEntity<T> invokeAPI(String path, HttpMethod method, MultiValueMap<String, String> queryParams, Object body, HttpHeaders headerParams, MultiValueMap<String, Object> formParams, List<MediaType> accept, MediaType contentType, String[] authNames, ParameterizedTypeReference<T> returnType) throws RestClientException {
        if(this.apiClient==null)
            throw new RestClientException("Api client is not initialized!");
        try {
            BaseTest.log("<b>Endpoint URL</b> \n" + path);
            //BaseTest.log("<b>Request Headers</b> \n" + headers.toString());
            if(queryParams!=null && !queryParams.isEmpty())
                BaseTest.log("<b>query params</b> \n" + queryParams);
            if (body != null && !(body instanceof String)) {
                body = mapper.writeValueAsString(body);
                BaseTest.log("<b>Payload</b> \n" + body);
            }
            ResponseEntity<T> responseEntity = this.apiClient.invokeAPI(path, method, queryParams, body, headerParams, formParams, accept, contentType, authNames, returnType);
            BaseTest.log("<b>Response status</b> \n" + responseEntity.getStatusCode());
            BaseTest.log("<b>Response</b> \n" + responseEntity.getBody());
            return responseEntity;
        } catch(Throwable ex) {
            BaseTest.log("<b>Response</b> \n" + ex.getMessage());
            throw new RestClientException(ex.getMessage());
        }
    }

}
