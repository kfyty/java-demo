package com.kfyty.demo.jump;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/*
 * CREATE BY Kfyty @2018-01-09 10:45:25
 * PROHIBITION OF PIRACY
 */
@Slf4j
public class JumpJump {
	private static final String SELFHEAD = "/img/selfhead.png";
	private static final String IMAGE_NAME = "screenCapture.png";
	private static final String IMAGE_NAME_BAK = "screenCaptureBak.png";
	private static final String IMAGE_PATH = "D:" + File.separator + "temp" + File.separator + "screenCapture";
	private static final String ADB_CAPTURE = "adb shell screencap -p /sdcard/" + IMAGE_NAME;
	private static final String ADB_PULL = "adb pull /sdcard/" + IMAGE_NAME + " " + IMAGE_PATH;
	private static final String ADB_SWIPE = "adb shell input swipe %s %s %s %s %s";
	
	private static final int SELF_PIXEL_PERMISSIBLE_ERROR = 3;
	private static final int TARGET_PIXEL_PERMISSIBLE_ERROR = 9;
	private static final int SHORTEST_EFFECTIVE_LENGTH = 45;
	private static final int SELF_WIDTH = 80;
	private static final int ANGLE = 30;
	private static final int MIN_HEIGHT = 520;
	private static final int MIN_PRESS_TIME = 275;
	private static final double TIME_COEFFICIENT = 1.385;
	
	private static int[] selfPixel = null;

	private static final Random random = new Random();
	
	public boolean init() {
		try {
			int temp = 0;
			log.info("Initialization ...");
			BufferedImage selfHeadImage = ImageIO.read(JumpJump.class.getResource(SELFHEAD));
			HashSet<Integer> pixelSet = new HashSet<>();
			for(int x = selfHeadImage.getMinX(); x <= selfHeadImage.getWidth() - 1; x++) {
				for(int y = selfHeadImage.getMinY(); y <= selfHeadImage.getHeight() - 1; y++) {
					pixelSet.add(selfHeadImage.getRGB(x, y));
				}
			}
			selfPixel = new int[pixelSet.size()];
			for(Integer i : pixelSet) {
				selfPixel[temp++] = i;
			}
			selfHeadImage = null;
			pixelSet = null;
			log.debug("success!");
			return true;
		} catch (Exception e) {
			log.debug("failed!");
			return false;
		}
	}
	
	public boolean checkFolder() {
		File imagePath = new File(IMAGE_PATH);
		log.debug("Check folder ...");
		if(!imagePath.exists() && !imagePath.mkdirs()) {
			log.debug("delete old file failed!");
			System.exit(-1);
		}
		imagePath = null;
		log.debug("success!");
		return true;
	}
	
	public boolean isSelfHead(int pixel) {
		for(int i = 0; i <= selfPixel.length - 1; i++) {
			if(Math.abs(selfPixel[i] - pixel) <= SELF_PIXEL_PERMISSIBLE_ERROR) {
				return true;
			}
		}
		return false;
	}

	public boolean isTarget(int firstPixel, int pixel) {
		int fr = (firstPixel >> 16) & 0xff;
		int fg = (firstPixel >> 8) & 0xff;
		int fb = firstPixel & 0xff;
		int tr = (pixel >> 16) & 0xff;
		int tg = (pixel >> 8) & 0xff;
		int tb = pixel & 0xff;
		boolean ir = Math.abs(fr - tr) >= TARGET_PIXEL_PERMISSIBLE_ERROR;
		boolean ig = Math.abs(fg - tg) >= TARGET_PIXEL_PERMISSIBLE_ERROR;
		boolean ib = Math.abs(fb - tb) >= TARGET_PIXEL_PERMISSIBLE_ERROR;
		return ir && ig || ir && ib || ig && ib;
	}
	
	public int getRandomNumber(int min, int max) {
		return random.nextInt(max) % (max - min + 1) + min;
	}
	
	public boolean executeCommand(String command) {
		Process process = null;
		try {
			log.info("Run command:{} ...", command);
			process = Runtime.getRuntime().exec("cmd /c " + command);
			process.waitFor();
			BufferedReader read = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String error = null;
			if((error = read.readLine()) != null) {
				log.debug("failed!\nError information:");
				log.debug(error);
				while((error = read.readLine()) != null) {
					log.debug(error);
				}
				return false;
			}
			log.info("success!");
			return true;
		} catch(Exception e) {
			log.debug("failed!\nError information:");
			e.printStackTrace();
			return false;
		} finally {
			if(process != null) {
				process.destroy();
			}
		}
	}
	
	public int[] getBothX(BufferedImage image) throws Exception {
		int minWidth = image.getMinX();
		int minHeight = MIN_HEIGHT;
		int width = image.getWidth();
		int height = image.getHeight();
		
		int selfSumX = 0;							// 符合条件的像素点的 X 坐标之和
		int selfCountX = 0;							// 符合条件的像素点的 X 坐标数目
		boolean isGetSelfX = false;					// 是否确定人物的 X 坐标
		
		int targetSumX = 0;
		int targetCountX = 0;
		boolean isGetTargetX = false;
		
		for(int y = minHeight; y <= height - 1; y++) {
			if(isGetSelfX && isGetTargetX) {
				if(Math.abs(selfSumX / selfCountX - targetSumX / targetCountX) <= SELF_WIDTH) {
					isGetTargetX = false;
					continue;
				}
				break;
			}
			int firstPixel = image.getRGB(minWidth, y);
			for(int x = minWidth; x <= width - 1; x++) {
				int pixel = image.getRGB(x, y);
				if(!isGetSelfX && isSelfHead(pixel)) {
					selfSumX += x;
					selfCountX++;
				} else if(!isGetTargetX && isTarget(firstPixel, pixel)) {
					targetSumX += x;
					targetCountX++;
				}
			}
			if(selfCountX >= SHORTEST_EFFECTIVE_LENGTH) {
				isGetSelfX = true;
			} else {
				selfSumX = 0;
				selfCountX = 0;
			}
			if(targetCountX >= SHORTEST_EFFECTIVE_LENGTH) {
				isGetTargetX = true;
			} else {
				targetSumX = 0;
				targetCountX = 0;
			}
		}
		return new int[] {selfSumX / selfCountX, targetSumX / targetCountX};
	}
	
	public double calcPressTime(File file) throws Exception {
		BufferedImage image = ImageIO.read(file);
		int[] bothX = getBothX(image);
		double distance = Math.abs(bothX[0] - bothX[1]) / Math.cos(ANGLE * Math.PI / 180);
		log.debug("FROM SELF: X = {} TO TARGET: X = {} AND DISTANCE = {} AND TIME_COEFFICIENT = {} AND ANGLE = {}", bothX[0], bothX[1], distance, TIME_COEFFICIENT, ANGLE);
		return distance * TIME_COEFFICIENT;
	}
	
	public boolean doJump(double PressTime) {
		int x = getRandomNumber(270, 540);
		int y = getRandomNumber(960, 1680);
		PressTime = PressTime > MIN_PRESS_TIME ? PressTime : MIN_PRESS_TIME;
		log.debug("Press information:[X = {}, Y = {}, PressTime = {} ms]", x, y, PressTime);
		return executeCommand(String.format(ADB_SWIPE, x, y, x, y, (int)PressTime));
	}
	
	public void backup(File file) {
		try {
			if(!file.exists()) {
				log.debug("File not found, backup failed!");
				return;
			}
			File fileBak = new File(IMAGE_PATH + File.separator + IMAGE_NAME_BAK);
			if(fileBak.exists() && !fileBak.delete()) {
				log.error("delete backup file failed !");
			}
			if(!file.renameTo(fileBak)) {
				log.debug("File rename failed!");
			}
		} catch(Exception e) {
			log.debug("Backup file failed!");
		}
	}
	
	public void startJump() {
		JumpJump jumpjump = new JumpJump();
		if(!jumpjump.init() || !jumpjump.checkFolder()) {
			return;
		}
		int count = 0;
		File file = new File(IMAGE_PATH + File.separator + IMAGE_NAME);
		while(jumpjump.executeCommand(ADB_CAPTURE) && jumpjump.executeCommand(ADB_PULL)) {
			log.debug("START PARSING IMAGE...");
			if(!file.exists()) {
				log.debug("The Image does not exist!");
				log.debug("Do jump failed, we will try again...");
				continue;
			}
			try {
				if(!jumpjump.doJump(jumpjump.calcPressTime(file))) {
					log.debug("Do jump failed, we will try again...");
					continue;
				}
				jumpjump.backup(file);
				int waitTime = jumpjump.getRandomNumber(1200, 2200);
				log.debug("PARSE END!");
				log.debug("The total number of executes is {}", (++count));
				log.debug("The next parse begins after {} milliseconds!", waitTime);
				TimeUnit.MILLISECONDS.sleep(waitTime);
			} catch(Exception e) {
				log.debug("Calculation Error And Game Over!");
				log.debug("Error information:");
				e.printStackTrace();
				log.info("You jumped {} times!", count);
				System.exit(-1);
			}
		}
	}
}
