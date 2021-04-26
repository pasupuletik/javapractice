package com.practice.java.test_rest.actuator;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

public class TestRest {

	public ResponseEntity invokeRESTService(String apiURL, String uriProp, HttpMethod httpMethod, String payLoadJson)
			throws Exception {
		String endPoint = apiURL + uriProp;
		HttpHeaders httpHeaders = new HttpHeaders();
		HttpEntity<String> requestEntity = new HttpEntity<String>(payLoadJson, httpHeaders);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity response = null;

		try {
			response = restTemplate.exchange(endPoint, httpMethod, requestEntity, String.class);
			if (response.getStatusCode().value() != 200) {
				throw new Exception("Service Failed with Error");
			}
			return response;
		} catch (RestClientException e) {
			throw e;
		}

	}

	private static void assertEmployee(ResponseEntity restResponse) throws Exception {
		JSONObject employeeJSON = new JSONObject(restResponse.getBody().toString());
		System.out.println("Status " + restResponse.getStatusCode().value());
		JSONArray arr = employeeJSON.getJSONArray("data");
		if (arr.length() > 0) {
			String empName = arr.getJSONObject(0).getString("employee_name");
			System.out.println("employee_name =" + empName);
			int salary = (Integer) arr.getJSONObject(0).getNumber("employee_salary");
			System.out.println("employee_salary =" + salary);
			int age = (Integer) arr.getJSONObject(0).getNumber("employee_age");
			System.out.println("employee_age =" + age);
		} else {
			throw new Exception("No Employee Returned");
		}
	}

	public static void assertDeleteJSON(ResponseEntity restResponse) throws Exception {
		JSONObject deleteJSON = new JSONObject(restResponse.getBody().toString());
		if (!(deleteJSON.get("message").equals("Successfully! Record has been deleted")))
			throw new Exception("Employee Not Deleted");
		System.out.println("Delete Body " + deleteJSON.get("message"));
	}

	public static void main(String[] args) throws Exception {
		String apiURL = "http://dummy.restapiexample.com/api/v1/";
		String getEmployee = "employees";
		String deleteEmployee = "delete/1";
		String payLoad = null;
		ResponseEntity restResponse = null;
		TestRest testRest = new TestRest();
		restResponse = testRest.invokeRESTService(apiURL, getEmployee, HttpMethod.GET, null);
		assertEmployee(restResponse);
		restResponse = testRest.invokeRESTService(apiURL, deleteEmployee, HttpMethod.DELETE, null);
		assertDeleteJSON(restResponse);

	}

}
