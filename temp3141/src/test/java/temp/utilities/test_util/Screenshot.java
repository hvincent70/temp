package com.sprint.iice_tests.utilities.test_util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.codehaus.plexus.util.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;

import com.sprint.iice_tests.web.browser.Browser;

public class Screenshot {

	public static void takeScreenShot(ITestResult result, String ssPath) {
		if (ITestResult.FAILURE == result.getStatus()) {
			try {
				System.out.println(result.getThrowable());
				TakesScreenshot ss = (TakesScreenshot) Browser.getDriverInstance();
				File file = ss.getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(file, new File(ssPath + result.getInstanceName().replaceAll("(.*digital\\.)(.*)(Runner.*)", "$2") + "FinalScreenshot" + result.getEndMillis() + ".png"));
				System.out.println("Screenshot complete.");
			} catch (Exception e) {
				System.out.println("Screenshot was not populated.");
				System.out.println(e.getMessage());
			}
		}
	}

	public static void takeScreenShot(String ssPath, String name) {
		try {
			TakesScreenshot ss = (TakesScreenshot) Browser.getDriverInstance();
			File file = ss.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(file, new File(ssPath + name + ".png"));
		} catch (IOException e) {
			System.out.println("Screenshot was not populated.");
			System.out.println(e.getMessage());
		}
	}
	
	public static void takeFullscreenShot(String ssPath, String name) {
		try {
			int i = 0, y_pos = 0;
			WebDriver driver = Browser.getDriverInstance();
			WebElement e = driver.findElement(By.cssSelector("body"));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			int browserHeight = driver.manage().window().getSize().getHeight();
			int browserWidth = driver.manage().window().getSize().getWidth();
			int bodySize = e.getSize().getHeight();
			BufferedImage bi = new BufferedImage(browserWidth, bodySize, BufferedImage.TYPE_INT_ARGB);
			Graphics g = bi.getGraphics();
			for(i = 0; i < bodySize; i += browserHeight - 160) {
				String command = String.format("scroll(0, %d)", i - 10);
				js.executeScript(command);
				TakesScreenshot ss = (TakesScreenshot) driver;
				File file = ss.getScreenshotAs(OutputType.FILE);
				File img = new File(ssPath + name + i + ".png");
				FileUtils.copyFile(file, img);
				BufferedImage image = ImageIO.read(img);
				g.drawImage(image, 0, i, null);
				img.delete();
				y_pos = Math.toIntExact((long)js.executeScript("return window.pageYOffset"));
			}
			i -= browserHeight;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			int y_last = Math.toIntExact((long)js.executeScript("return window.pageYOffset"));
			int diff = y_last - y_pos;
			TakesScreenshot ss = (TakesScreenshot) driver;
			File file = ss.getScreenshotAs(OutputType.FILE);
			File img = new File(ssPath + name + "final.png");
			FileUtils.copyFile(file, img);
			BufferedImage image = ImageIO.read(img);		
			BufferedImage crop = new BufferedImage(browserWidth, browserHeight - diff, BufferedImage.TYPE_INT_ARGB);
			crop.getGraphics().drawImage(image, 0,  crop.getHeight() - browserHeight + diff, null);
			g.drawImage(crop, 0, y_last, null);
			img.delete();
			ImageIO.write(bi, "PNG", new File(ssPath + name));
		} catch (Exception e) {
			System.out.println("Screenshot was not populated.");
			System.out.println(e.getMessage());
		}	
	}
	
	public static void takePDFScreenShot(String filePath, String screenshotPath) throws InterruptedException {
		try {
			WebDriver driver = Browser.getDriverInstance();
			System.out.println(filePath);
			driver.navigate().to(filePath);
			Thread.sleep(30000);
			System.out.println(driver.getCurrentUrl());
			TakesScreenshot ss = (TakesScreenshot) Browser.getDriverInstance();
			File file = ss.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(file, new File(screenshotPath + ".png"));
		} catch (IOException e) {
			System.out.println("Screenshot was not populated.");
			System.out.println(e.getMessage());
		}
	}
}