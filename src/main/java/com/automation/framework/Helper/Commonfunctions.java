package com.automation.framework.Helper;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class Commonfunctions

{
	public static WebDriver driver = null;
	static Wrapperdriver wrapperdriver;

	public Commonfunctions(WebDriver driver) {
		Commonfunctions.driver = driver;
		wrapperdriver = new Wrapperdriver(driver);

	}

	public boolean isFileDownloaded(String downloadPath, String fileName) {
		boolean flag = false;
		File dir = new File(downloadPath);
		File[] dir_contents = dir.listFiles();

		for (int i = 0; i < dir_contents.length; i++) {
			if (dir_contents[i].getName().contains(fileName))
				return flag = true;
		}

		return flag;
	}

	public String[] splitstring(String splittext, String splitchar) {
		String[] output = splittext.split(splitchar);
		return output;
	}

	public boolean comparelist(List<String> List1, List<String> List2) {
		boolean match = false;

		if (List1.equals(List2)) {
			System.out.println("Same");
			match = true;
		} else {
			System.out.println("Not Same");

		}
		return match;

	}

	// To generate a random String value
	public String getSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 4) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	public String getSaltStringBlock() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 4) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	public String getSaltStringSmall() {
		String SALTCHARS = "qwertyuiopasdfghjklzxcvbnm";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 4) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	public String getSaltStringNumber() {
		String SALTCHARS = "0123456789";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 4) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	public String getSaltStringSpecial() {
		String SALTCHARS = "@*()_-\\/.";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 4) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	public void Sort2Strings(String string1, String string2) {
		String[] array1 = { string1.toLowerCase(), string2.toLowerCase() };
		String[] array2 = { string1.toLowerCase(), string2.toLowerCase() };
		Arrays.sort(array2);
		Assert.assertTrue(Arrays.equals(array1, array2));
	}

	public void Sort2Dates(String Date1, String Date2) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date DateRes1 = df.parse(Date1);
		Date DateRes2 = df.parse(Date2);

		if (DateRes1.compareTo(DateRes2) < 0) {
			System.out.println("Sort By Date" + "Pass");
		} else {
			System.out.println("Sort By Date" + "Fail");
		}
	}

	public void SortByDateTime(String Date1, String Date2) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm a");
		Date DateRes1 = sdf.parse(Date1);
		Date DateRes2 = sdf.parse(Date2);

		if (DateRes1.compareTo(DateRes2) < 0) {
			System.out.println("Sort By Date" + "Pass");
		} else {
			System.out.println("Sort By Date" + "Fail");
		}

	}

	public String generateRandomAutoTestString(String postfix) {
		String prefix = "autoTest" + postfix;
		Random random = new Random();
		int randomNumber = random.nextInt(9999999) + 1;
		String randomString = prefix + randomNumber;
		return randomString;
	}

	public String[] createTestUser() {
		Random random = new Random();
		String firstName = "auto";
		String lastName = "Test" + (random.nextInt(9999) + 1);
		String username = firstName + lastName + (random.nextInt(9000000) + 1);
		String email = username + "@email.com";
		String cellNumber = String.valueOf(random.nextInt(900000000) + 1000000000);
		return new String[] { firstName, lastName, username, email, cellNumber };
	}

//.To Print Future Dates
	public String addDaysToDate(String date, String days) {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date myDate = df.parse(date.trim());
			c.setTime(myDate);
			c.add(Calendar.DATE, Integer.parseInt(days));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String toDate = df.format(c.getTime());
		return toDate;
	}

//.To Print Past Dates
	public String subtractDaysFromDate(String date, String days) {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date myDate = df.parse(date.trim());
			c.setTime(myDate);
			c.add(Calendar.DATE, (Integer.parseInt(days) * -1));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String toDate = df.format(c.getTime());
		return toDate;
	}

	// .To Print Future Dates
	public String addDaysToDate(String days) {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("MMddyyyy"); // Modified format for MMDDYYYY

		try {
			c.add(Calendar.DATE, Integer.parseInt(days));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		String toDate = df.format(c.getTime());
		return toDate;
	}

	// .To Print Past Dates
	public String subtractDaysFromDate(String days) {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("MMddyyyy"); // Modified format for MMDDYYYY

		try {
			c.add(Calendar.DATE, -Integer.parseInt(days)); // Subtract days
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		String fromDate = df.format(c.getTime());
		return fromDate;
	}

	public static void toLowerCase(List<String> strings) {
		ListIterator<String> iterator = strings.listIterator();
		while (iterator.hasNext()) {
			iterator.set(iterator.next().toLowerCase());
		}
	}

	public void waitForPageLoaded() {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		try {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			wait.until(expectation);
		} catch (Throwable error) {
			Assert.fail("Timeout waiting for Page Load Request to complete.");
		}
	}

	public void waitForDropDownPopulate(WebDriver driver, final Select dropDown, int minOptions) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		try {
			wait.until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					List<WebElement> list = dropDown.getOptions();
					return list.size() >= minOptions;
				}
			});
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public String GetCurrentDate() {
		Format f = new SimpleDateFormat("MM/dd/yyyy");
		String strDate = f.format(new Date());
		System.out.println("Current Date = " + strDate);
		return strDate;
	}

	public static String GetCurrentTimeStamp() {
		Format f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String strDate = f.format(new Date());
		System.out.println("Current Date = " + strDate);
		return strDate;
	}

	public static <K, V> Set<K> getKeys(Map<K, V> map, V value) {
		Set<K> keys = new HashSet<>();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				keys.add(entry.getKey());
			}
		}
		return keys;
	}

	public int[] convertStringToIntArray(String s) {
		String[] values = s.split(",");
		int[] arr = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			arr[i] = Integer.parseInt(values[i]);
		}
		return arr;
	}

	public String getCurrentUTCTime() {
		ZonedDateTime currentUtcTime = ZonedDateTime.now(java.time.ZoneOffset.UTC);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		String formattedUtcTime = currentUtcTime.format(formatter);
		return formattedUtcTime;
	}

	public void deleteDirectory(String filepath) throws Exception {
		File file = new File(filepath);
		FileUtils.deleteDirectory(file);
		file.delete();
		System.out.println("Deleted Directory: " + filepath);
	}

	public boolean validateElapsedTime(String elapsedTime, long baselineTime) {
		try {
			String[] parts = elapsedTime.split(":");
			int hours = Integer.parseInt(parts[0]);
			int minutes = Integer.parseInt(parts[1]);
			int seconds = Integer.parseInt(parts[2]);
			int milliseconds = Integer.parseInt(parts[3]);
			long totalTimeMillis = (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000) + milliseconds;
			return totalTimeMillis <= baselineTime;
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method waits for the most recent file to be fully downloaded in the
	 * specified directory. It checks for new files in the directory and ensures
	 * that the file is fully downloaded (i.e., not in a temporary state with `.tmp`
	 * or `.crdownload` extensions). It returns the most recently downloaded file,
	 * even if it has a dynamic name (e.g., with "(1)" added).
	 *
	 * @param downloadPath   The directory where the file is being downloaded (e.g.,
	 *                       "./Downloads" or "C:\\Downloads").
	 * @param timeOutSeconds The maximum time (in seconds) to wait for the file to
	 *                       be fully downloaded.
	 * @return The most recently downloaded file once it is no longer in a temporary
	 *         state (e.g., `.tmp` or `.crdownload`). Returns null if no file is
	 *         fully downloaded within the given timeout.
	 */

	public File waitForNewFileToDownload(String downloadPath, int timeOutSeconds) throws InterruptedException {
		Thread.sleep(2000);
		File directory = new File(downloadPath);
		long startTime = System.currentTimeMillis();

		File latestFile = null;
		long latestModifiedTime = 0;

		// Loop until the timeout is reached
		while ((System.currentTimeMillis() - startTime) < timeOutSeconds * 1000) {
			File[] files = directory.listFiles(); // Get all files and directories

			if (files != null) {
				for (File file : files) {
					// Check if the file is a regular file and doesn't have `.crdownload` or `.tmp`
					// extensions
					if (file.isFile() && !file.getName().endsWith(".crdownload") && !file.getName().endsWith(".tmp")) {
						// Track the most recently modified file (likely the newly downloaded file)
						if (file.lastModified() > latestModifiedTime) {
							latestModifiedTime = file.lastModified();
							latestFile = file;
						}
					}
				}
			}

			// If the latestFile is found and has finished downloading (no `.crdownload` or
			// `.tmp` file), return it
			if (latestFile != null && !new File(latestFile.getAbsolutePath() + ".crdownload").exists()
					&& !new File(latestFile.getAbsolutePath() + ".tmp").exists()) {
				return latestFile;
			}

			try {
				Thread.sleep(1000); // Poll every 1 second to check for new files
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return null; // Timeout reached with no fully downloaded file found
	}

	public List<String> getFileNamesInZip(String zipFilePath) throws Exception {
		Thread.sleep(2000);
		List<String> allFiles = new ArrayList<String>();

		try (FileInputStream fis = new FileInputStream(zipFilePath); ZipInputStream zis = new ZipInputStream(fis)) {
			ZipEntry zipEntry;
			while ((zipEntry = zis.getNextEntry()) != null) {
				allFiles.add(zipEntry.getName());
			}
		}

		System.out.println(":----- All Contents captured from Zip File ----- :");

		for (String files : allFiles) {
			System.out.println(files);
		}

		return allFiles;
	}

	public void verifyFilesInZip(String zipFilePath, String... fileNames) throws Exception {
		Thread.sleep(1000);
		int allContensInZip = fileNames.length;
		ArrayList<String> allFilesInZip = new ArrayList<String>(getFileNamesInZip(zipFilePath));
		Thread.sleep(2000);
		int matchCount = 0;

		for (String fileName : fileNames) {

			innerloop: for (int i = 0; i < allFilesInZip.size(); i++) {
				if (allFilesInZip.get(i).contains(fileName)) {
					matchCount++;
					break innerloop;
				}
			}
		}

		Assert.assertTrue(allContensInZip == matchCount);
		System.out.println(":----- Successfully verified the contents from the zip file -----:");
	}
}
