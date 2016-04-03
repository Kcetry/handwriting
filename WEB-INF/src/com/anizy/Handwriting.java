package com.anizy;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Handwriting extends HttpServlet {
	
	/**
	 * ����Ŀ����windows��linuxƽ̨,ֻ��ȥ����Ӧ��ע�ͼ���
	 * @author kcetry
	 *
	 */
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		//��ȡ��Ŀ��·��
		String proPath = req.getSession().getServletContext().getRealPath("/");
		
		//��д�����ݻ���01���ı���ʽ������outpath
		String outPath = "C:\\Users\\kcetry\\Desktop\\M\\"; /* windows */
		//String outPath = "/home/ml/project/handwriting/"; /* linux */
		//�ͻ��˻���post��ʽ�ύ��ΪimgData,ֵΪͼƬ��base64�ַ���
		String result = saveConvertedData(req.getParameter("imgData"),outPath,proPath);
		
		//����ʶ����
		res.setContentType("text/HTML; charset=UTF-8");
		try {
			PrintWriter out = res.getWriter();
			out.println(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public static String saveConvertedData(String base64Str,String outPath,String proPath){
    	//��ȡ��Ч����
    	base64Str = base64Str.substring(base64Str.indexOf(",")+1, base64Str.length());
    	//��base64�ַ���תΪBufferedImage
    	BufferedImage bi = 	HwUtils.base64ToImg(base64Str);
    	//��ȡ���ȡ������
		HashMap<String, Integer> cr = HwUtils.getClipRect(bi);
		//��ʼ�ü�
		bi = ImageUtils.cut(bi,cr.get("reX"),cr.get("reY"),cr.get("side"),cr.get("side"));
		//������������ɺڱ�,���Ҫȥ��
		bi = HwUtils.cutBlackSide(bi, cr.get("cutX"), cr.get("cutY"));
		//���ŵ��߳�32������������
		bi = ImageUtils.scale(bi, 32, 32, false);
		//��BufferedImageתΪ01��ɵ��ַ���
		String istr = HwUtils.imgToStrings(bi);
		//��ȡ����һ���ļ���
		ArrayList<String> names = HwUtils.getFileNameList(outPath);
		String fileName = Integer.parseInt(names.get(names.size()-1))+1 + "";
		//��01��ɵ��ַ������浽outpath,�ļ�����������ʽ����,�޺�׺
		HwUtils.stringsToFile(outPath+fileName,istr);

        pyCollector tp = new pyCollector(); /* windows */
        String fullPath = proPath+"WEB-INF\\classes\\com\\anizy"; /* windows */
		String result = tp.execIt("python "+fullPath+"\\run.py "+outPath+fileName+" "+fullPath+"\\trainingDigits"); /* windows */
        
		//class�ļ�����Ŀ¼
		//String fullPath = proPath+"WEB-INF/classes/com/anizy"; /* linux */
		//ִ��shell����,����python�ű�,��ȡ���
        //String result = HwUtils.execIt("python "+fullPath+"/run.py "+outPath+fileName+" "+fullPath+"/trainingDigits"); /* linux */
		return result;
    }
}