# knn算法实现的数字手写识别

###界面 :
![](http://7xqhly.com1.z0.glb.clouddn.com/hhhs.png)

###实现功能 :
阿拉伯手写体的识别,正确率在80%以上。

###实现原理 : 
在写字板写出字体点击识别按钮后,字体会先被转为base64字符串然后发送到服务器端,服务器取得数据后对字符串解码并转为Java的图片缓冲区(BufferedImage),对图片进行裁剪,缩放之后转为01的点阵文本并保存,然后调用shell命令执行Python脚本,获取结果返回给客户端。

###关键点 :

+ 手写板是用HTML5的canvas实现的,兼容移动端,代码来自
>html5 canvas作的手写板【兼容手机】
http://powertech.iteye.com/blog/2069207

+ 识别算法是用Python实现的,代码来自《机器学习实战》第二章,图片转点阵算法是用Java实现的,来自。
>文字图片转成点阵的小工具
http://milker.iteye.com/blog/1326218

+ Tomcat作为后台服务器,本来打算使用NodeJS的,但由于需要调用Python脚本,要使用thrift,也许也可以调用shell命令。但因为图像转点阵的程序是用Java实现的,为了方便于是使用Tomcat(我懒)。


###有待完善 :

+ 样本每个数字100个样本,有点少,更多更好的样本也许会提高正确率
+ 算法效率低,对每个样本都需要进行矩阵运算。

PS:该项目已在Github开源,BSD许可。
>https://github.com/Kcetry/handwriting




