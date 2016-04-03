package com.anizy;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javax.imageio.ImageIO;

import sun.misc.BASE64Decoder;

	/**
	 * ��дʶ�𹤾���
	 * @author kcetry
	 *
	 */

public class HwUtils {
	/**
	 * �ü�BufferedImage��
	 * @param base64Strings base64���ַ���
	 * @return BufferedImage ת�����BufferedImage
	 */
	public static BufferedImage base64ToImg(String base64Strings) {
		BufferedImage bi = null;
		ByteArrayInputStream bais = null;
		BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
		byte[] bytes1;
		try {
			bytes1 = decoder.decodeBuffer(base64Strings);
			bais = new ByteArrayInputStream(bytes1);    
			bi = ImageIO.read(bais); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bi;
	}
	
	/**
	 * BufferedImageת01�ַ���
	 * @param BufferedImage
	 * @return BufferedImage ת������ַ�
	 */
	public static String imgToStrings(BufferedImage bi) {
		String str = "", YES = "1", NO = "0";
		long pixel2 = Long.parseLong("ff202020", 16);
        long pixel3 = Long.parseLong("ff000000", 16);
        for (int i = 0; i < bi.getHeight(); i += 1) {  
            for (int j = 0; j < bi.getWidth(); j += 1) {  
                long pixel = Long.parseLong(Integer.toHexString(bi.getRGB(j, i)), 16);
                if(Math.abs(pixel2-pixel) <= Math.abs(pixel2-pixel3))  {  
                    str += YES;  
                } else {  
                    str += NO;  
                }
            }  
            //str += "\r\n";  
            str += "\n"; 
        } 
        System.out.println(str); 
        return str;
	}

	
	/**
	 * ��String����Ϊ�ı���
	 * @param filename �ļ���
	 * @param strBuffer �ַ���
	 */
	public static void stringsToFile(final String filename, final String strBuffer) {  
		try {      
			System.out.println(filename);
			File fileText = new File(filename);  
			FileWriter fileWriter = new FileWriter(fileText);  
			fileWriter.write(strBuffer);  
			fileWriter.close();  
		} catch (IOException e) {    
			e.printStackTrace();  
		}  
	}

	
	/**
	 * ʹ�õ�ǰ��ϵͳʱ���������ļ���
	 * @param nameFormat Ҫ�������ļ������ڸ�ʽ �磺"yyyyMMdd_HHmmss"
	 * @param fileType �ļ������� �磺".mp3"
	 * @return ���ĺ���ļ���
	 */
	public static String dateToFileName(String nameFormat) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(nameFormat);
		String fileName = dateFormat.format(date);
		return fileName;
	}

	
	
	/**
	 * ��bufferImage��ȡ�����������ݵ�����
	 * @param BufferedImage ͼ�񻺳���
	 * @param HashMap reX,reY:��ȡ�����ʼ����; side:�߳�
	 */
	public static HashMap<String, Integer> getClipRect(BufferedImage bi) {
		int height = bi.getHeight();
		int width = bi.getWidth();
		int stX = width, stY = 0, enX = 0, enY = 0, side=0, reX = 0, reY = 0, adsX = 0, adeX = 0, adsY = 0, adeY, cutX = 0, cutY = 0;
		//��������,�����ұ�������
        for (int i = 0; i < height; i += 1) {
            for (int j = 0; j < width; j += 1) { 
            	int tmX = 0, tmY = 0;
            	//�жϸõ��Ƿ�Ϊ��ɫ
                if(Integer.toHexString(bi.getRGB(j, i)).equals("ff000000")) {
                	//ȡ��ͼ����߿�ʼ�к�ɫ���x����
                	stX = j < stX? j:stX;
                	//ȡ��ͼ���ϱ߿�ʼ�к�ɫ���Y����
                	if(stY == 0) stY = i;
                	tmY = i;
                	tmX = j;
                }
                //ȡ��ͼ���±߿�ʼ�к�ɫ���x��Y����
                enX = tmX > enX? tmX : enX;
                enY = tmY > enY? tmY : enY;
            }  
        }
        //��ȡ������������ı߳�ΪenY-stY��enX-stX�����ֵ
        side = (enY-stY) > (enX-stX)? (enY-stY+1):(enX-stX+1);
        
		if((enY-stY+1) == side) {
			//ȡ��enX��stX�м��,��side��һ�뼴��ȡ�ÿ�ʼ��ȡλ�õ�x����,y����ͬ��
			int miX = (int)((enX-stX)*0.5+stX);
			reX = (int)(miX-(int)(0.5*side));
			adsX = reX;
			adeX = miX+(int)(side*0.5)-width;
			if(adsX < 0) {
				cutX = adsX;
			}else if(adeX >= 0) {
				cutX = adeX;
			}
			reY = stY;
		}else if((enX-stX+1) == side) {
			int miY = (int)((enY-stY)*0.5+stY);
			reY = (int)(miY-(int)(0.5*side));
			adsY = reY;
			adeY = miY+(int)(side*0.5)-width;
			if(adsY < 0) {
				cutY = adsY;
			}else if(adeY >= 0) {
				cutY = adeY;
			}
			reX = stX;
		}
		HashMap<String, Integer> cr =new  HashMap<String, Integer>(); 
		cr.put("reX", reX);
		cr.put("cutX", cutX);
		cr.put("reY", reY);
		cr.put("cutY", cutY);
		cr.put("side",side);
		return cr;
	}

	public static BufferedImage cutBlackSide(BufferedImage bi, int cuX, int cuY) {
        //��ɫ
        int rgb = -1;
        int si = 0, ei = 0, sj = 0, ej = 0;
        int height = bi.getHeight();
        int width = bi.getWidth();
        
        sj = cuX < 0 ? 0 : width-cuX;
        ej = cuX < 0 ? -cuX : width;
        for (int i = 0; i < height; i++) {  
        	for (int j = sj; j < ej; j++) {
            	bi.setRGB(j, i, rgb);
        	}
        }
        
        si = cuY < 0 ? 0 : height-cuY;
        ei = cuY < 0 ? -cuY : height;
        for (int i = si; i < ei; i++) {  
        	for (int j = 0; j < width; j++) {
            	bi.setRGB(j, i, rgb);
        	}
        }

        return bi;
	}
	
	/**
	 * ��ȡĿ¼�����е��ļ���,��������ĸ��С��������
	 * @param path Ŀ¼·��
	 * @param ArrayList<String> Ŀ¼�б�
	 */
	public static ArrayList<String> getFileNameList(String path) {
		File file=new File(path);
		File[] tempList = file.listFiles();
		ArrayList<Integer>  tnames = new ArrayList<Integer> ();
		ArrayList<String>  names = new ArrayList<String> ();
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				tnames.add(Integer.valueOf(tempList[i].getName()));
			}
		}
		Collections.sort(tnames); 
		for (int i = 0; i < tnames.size(); i++) {
			names.add(tnames.get(i)+"");
		}
		if(names.size()==0) names.add("-1");
		return names;
	}

	/**
	 * ִ��shellָ����ؽ��
	 * @param cmd shellָ��
	 * @param lines cmd��������
	 */
	public static String execIt(String cmd) {
        Process process;
        String result = "";
		try {
			//cmd = "cmd /c dir";
			process = Runtime.getRuntime().exec(cmd);
			BufferedInputStream in = new BufferedInputStream(process.getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String lineStr;
			while ((lineStr = br.readLine()) != null) {
				result += lineStr;
			}
			br.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return result;
	}
}
