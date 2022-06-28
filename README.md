# coolweather
《第一行代码android（第二版）》书上的练习项目，查询天气APP，使用LitePal数据库存储本地天气数据，使用Okhttp进行网络通讯获取服务器端的json格式数据并解析。
最终效果如下：

![搜狗截图20220628110246](https://user-images.githubusercontent.com/98386278/176083721-3aecb23e-caf8-43be-ba04-0405a4adcadc.jpg)
![搜狗截图20220628110235](https://user-images.githubusercontent.com/98386278/176083771-999d3226-10ce-416c-96fd-48b7eb0e98d3.jpg)
![搜狗截图20220628110312](https://user-images.githubusercontent.com/98386278/176083754-d325d0ba-c911-4d4f-a01c-bccdab0b92d7.jpg)

由于该书出版较早一些方法和库等已被弃用。主要需要修改的地方有：
1.数据库操作中的DataSupport类已弃用
换成LitePalSupport，添加依赖变成
implementation 'org.litepal.guolindev:core:3.2.3'
注意如果报错无法添加依赖，可能需要去setting.gradle文件添加jcenter，因为这玩意被弃用而litepal又没有更新。
![image](https://user-images.githubusercontent.com/98386278/176084463-9e0e9dc7-a9f0-4133-9dd1-046839a711ef.png)

2.http网络请求问题
因为新版android默认只能采用加密连接默认不支持明码传输，所以需要我们自己设置允许明码传输，新建一个xml文件
![image](https://user-images.githubusercontent.com/98386278/176085330-3989abd0-9984-4803-a7b4-6aad5d890b90.png)
并在注册文件里说明即可
android:networkSecurityConfig="@xml/network_security_config"

3.侧滑下拉等功能需要的support.v4不再使用
换成
androidx.drawerlayout.widget.DrawerLayout
androidx.swiperefreshlayout.widget.SwipeRefreshLayout

4.
