package com.anizy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;

	/**
	 * ͼƬ��������
	 * @author kcetry
	 *
	 */

public class ImageUtils {

    public static BufferedImage scale(BufferedImage bi, int height, int width, boolean bb) {
        double ratio = 0.0; // ���ű���
        BufferedImage sbi = null;
		// �������
		if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
		    if (bi.getHeight() > bi.getWidth()) {
		        ratio = (new Integer(height)).doubleValue()
		                / bi.getHeight();
		    } else {
		        ratio = (new Integer(width)).doubleValue() / bi.getWidth();
		    }
		    AffineTransformOp op = new AffineTransformOp(AffineTransform
		            .getScaleInstance(ratio, ratio), null);
		    sbi = op.filter(bi, null);
		}
		if (bb) {
			Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
		    BufferedImage image = new BufferedImage(width, height,
		            BufferedImage.TYPE_INT_RGB);
		    Graphics2D g = image.createGraphics();
		    g.setColor(Color.white);
		    g.fillRect(0, 0, width, height);
		    if (width == itemp.getWidth(null))
		        g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
		                itemp.getWidth(null), itemp.getHeight(null),
		                Color.white, null);
		    else
		        g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
		                itemp.getWidth(null), itemp.getHeight(null),
		                Color.white, null);
		    g.dispose();
		    sbi = image;
		}
		return sbi;
    }
   
   
    public static BufferedImage cut(BufferedImage bi, int x, int y, int width, int height) {
    	BufferedImage tag = null;
        int srcWidth = bi.getHeight(); 
        int srcHeight = bi.getWidth(); 
        if (srcWidth > 0 && srcHeight > 0) {
            Image image = bi.getScaledInstance(srcWidth, srcHeight,
                    Image.SCALE_DEFAULT);
            // �ĸ������ֱ�Ϊͼ���������Ϳ��
            // ��: CropImageFilter(int x,int y,int width,int height)
            ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
            Image img = Toolkit.getDefaultToolkit().createImage(
                    new FilteredImageSource(image.getSource(),
                            cropFilter));
            tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(img, 0, 0, width, height, null); // �����и���ͼ
            g.dispose();
        }
        return tag;
    }
}