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
	 * 该项目兼容windows和linux平台,只需去掉对应的注释即可
	 * @author kcetry
	 *
	 */
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		//获取项目的路径
		String proPath = req.getSession().getServletContext().getRealPath("/");
		
		//手写的数据会以01的文本方式保存在outpath
		String outPath = "C:\\Users\\kcetry\\Desktop\\M\\"; /* windows */
		//String outPath = "/home/ml/project/handwriting/"; /* linux */
		//客户端会以post方式提交键为imgData,值为图片的base64字符串
		String result = saveConvertedData(req.getParameter("imgData"),outPath,proPath);
		
		//返回识别结果
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
    	//截取有效数据
    	base64Str = base64Str.substring(base64Str.indexOf(",")+1, base64Str.length());
    	//把base64字符串转为BufferedImage
    	BufferedImage bi = 	HwUtils.base64ToImg(base64Str);
    	//获取需截取的区域
		HashMap<String, Integer> cr = HwUtils.getClipRect(bi);
		//开始裁剪
		bi = ImageUtils.cut(bi,cr.get("reX"),cr.get("reY"),cr.get("side"),cr.get("side"));
		//超出的区域会变成黑边,因此要去掉
		bi = HwUtils.cutBlackSide(bi, cr.get("cutX"), cr.get("cutY"));
		//缩放到边长32的正方形区域
		bi = ImageUtils.scale(bi, 32, 32, false);
		//把BufferedImage转为01组成的字符串
		String istr = HwUtils.imgToStrings(bi);
		//获取最后的一个文件名
		ArrayList<String> names = HwUtils.getFileNameList(outPath);
		String fileName = Integer.parseInt(names.get(names.size()-1))+1 + "";
		//把01组成的字符串保存到outpath,文件名以数字形式递增,无后缀
		HwUtils.stringsToFile(outPath+fileName,istr);

        pyCollector tp = new pyCollector(); /* windows */
        String fullPath = proPath+"WEB-INF\\classes\\com\\anizy"; /* windows */
		String result = tp.execIt("python "+fullPath+"\\run.py "+outPath+fileName+" "+fullPath+"\\trainingDigits"); /* windows */
        
		//class文件所在目录
		//String fullPath = proPath+"WEB-INF/classes/com/anizy"; /* linux */
		//执行shell命令,调用python脚本,获取结果
        //String result = HwUtils.execIt("python "+fullPath+"/run.py "+outPath+fileName+" "+fullPath+"/trainingDigits"); /* linux */
		return result;
    }
}