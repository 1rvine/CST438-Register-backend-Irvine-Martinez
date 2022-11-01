package com.cst438;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.cst438.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
 * This EndToEndStudentTest shows how to use selenium testing using the web driver 
 * with Chrome browser to verify a new student can be added to the database using 
 * front-end and back-end of the registration service 
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 *      
 *    Make sure that TEST_COURSE_ID is a valid course for TEST_SEMESTER.
 *    
 *    URL is the server on which Node.js is running.
 */


@SpringBootTest

public class EndToEndScheduleTest {

	public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/irvine/Downloads/chromedriver";
	
	public static final String CHROME_DRIVER = "webdriver.chrome.driver";

	public static final String URL = "http://localhost:3000";

	public static final String TEST_STUDENT_EMAIL = "mytest000@csumb.edu";
	public static final String TEST_STUDENT_NAME = "Test Username";

	public static final String TEST_USER_EMAIL = "test@csumb.edu";

	public static final int TEST_COURSE_ID = 40443;

	public static final String TEST_SEMESTER = "2021 Fall";

	public static final int SLEEP_DURATION = 1000; // 1 second.


	/*
	 * When running in @SpringBootTest environment, database repositories can be used
	 * with the actual database.
	 */
	
	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Test
	public void addStudent() throws Exception {
		var student = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
		if (student != null) {
			studentRepository.delete(student);
		}

		System.setProperty(CHROME_DRIVER, CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();

		try {
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

			// Fill in pop-up with test data
			var we = driver.findElement(By.xpath("//button[@id='begin_add_student']"));
			we.click();
			we = driver.findElement(By.name("name"));
			we.sendKeys(TEST_STUDENT_NAME);
			we = driver.findElement(By.name("email"));
			we.sendKeys(TEST_STUDENT_EMAIL);
			we = we.findElement(By.xpath("//button[@id='student_add']"));
			we.click();

			// Check student was added
			Assertions.assertNotNull(studentRepository.findByEmail(TEST_STUDENT_EMAIL));
		}
		catch (Exception error) {
			Assertions.fail(error.getMessage());
		}
		finally {
			// Clean database
			student = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			if (student != null) {
				studentRepository.delete(student);
			}

			driver.quit();
		}
	}
}
