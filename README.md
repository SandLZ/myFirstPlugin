# myFirstPlugin
ionic first test plugin

##cordova-plugin

目录

* [1.  概述](#1)
* [2.  插件](#2)
	* [2.1 新建插件](#2.1)
	* [2.2 安装插件](#2.2)
	* [2.3 修改并调试插件](#2.3)
* [3. 下载示例](#3)

----
<h3 id="1">概述</h3>

	由于有些功能受限于硬件，需要使用各个平台的特有功能，我们使用插件的方式来实现这些功能。
	
----

<h3 id="2"></h3>


<h3 id="2.1">新建插件</h3>

1. 准备工作

	* 环境

		`安装plugman`
	
		步骤：
	
		```
		npm install -g plugman
	
		注：报错请使用管理员权限安装
		```
2. 新建一个插件

	* `cd ~/your_plugin_folder 进入你想保存插件的目录`
	
	* 新建命令

		```
		plugman create --name <pluginName> --
		plugin_id <pluginID> --plugin_version 
		<version> [--path <directory>] [--
		variable NAME=VALUE]
		
		example:plugman create --name myFirstPlugin --plugin_id com.handsmap.myFirstPlugin --plugin_version 0.0.1 
		
		参数解释：
		pluginName  插件名称
		pluginID	插件ID
		version		版本号
		directory	路径（可选）
		```
		
	* 新建成功，如下图所示:
	
	![](http://i4.tietuku.com/25f8e7613147c84a.png)


		
3. 添加平台

	```
	cd myPlugin
	
	plugman platform add --platform_name <platform>
	
	注：platform->  ios android windows etc.
	
	```
	
4. 插件目录

	![](http://i4.tietuku.com/cfd22f5b0f00c5d4.png)


		src 对应不同的平台
		www 放我们的 javascript 文件
		plugin.xml 是插件的配置文件

	* plugin.xml

	```
	id: 插件的标识，即一开始我们新建插件输入的 ID: com.handsmap.myPlugin
	name：插件的名称，新建插件时输入的名称，myPlugin
	description：描述信息
	js-module：对应我们的 javascript 文件，src 属性指向 www/myPlugin.js
	platform：支持的平台，这里仅有一个 android，这是刚才我们通过“ plugman platform add --platform_name android ”添加进来的。

	config-file：当安装该插件时，会添加到目标平台android下的 res/xml/config.xml 文件中，并将 src/android/myEcho.java，复制到 android 的 package 包中
	```

	![](http://i4.tietuku.com/a8b13b9f10689428.png)


	`注：format code -> cmd + option + L`

* myFirstPlugin.js
	
	```
文件内容很简单，第一句是引入cordova下的exec库
第二句是我们插件的执行插件方法
exec(success, error, "myPlugin", "coolMethod", [arg0]);
success:调用成功时的回调函数
error:调用出错时的回调函数
"myPlugin"：插件名称
"coolMethod"：执行插件里的方法
[arg0]：可选参数，执行方法的参数数组。
	```
* myFirstPlugin.java

	```
可以看到myEcho继承自CordovaPlugin，并重载了execute方法。
在execute方法里有个判断，action.equals("coolMethod")，这表示如果执行的是我们的方法那就继续执行内部的逻辑并返回true，如果不是则直接返回false结束。
	```

	```
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {}
    
    参数解释：
	actions:        方法名
	args:           参数
	callbackContext：回调
	```
	
	
	插件新建好了，赶紧安装试试吧~
	

----

<h3 id="2.2">安装插件</h3>

* 新建一个测试工程（[如何新建](http://jingyan.baidu.com/article/ff42efa93185c0c19e2202b1.html)）
* 安装本插件
		`cordova plugin add /xxx/myFirstPlugin`
		
	安装成功后，如下图所示：
	![](http://i4.tietuku.com/6716393657fac937.png)

	系统会自动添加到已有的平台，如android:
	![](http://i4.tietuku.com/8c481e13b0deae0d.png)


	
----


<h3 id="2.3">修改并调试插件</h3>


* 修改
    
   功能：弹出对话框

1. myFirstPlugin.js

	```
	var exec = require('cordova/exec');

	var alert = function(arg0, success, error) {
    exec(success, error, "myFirstPlugin", "showAlert", [arg0]);
};
window.plugins = window.plugins || {};
window.plugins.myFirstPlugin = {showAlert:alert};
	```

2. myFirstPlugin.java

```
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
	        if (action.equals("showAlert")) {
	           String msg = args.get[0];//test
            return true;
        }
        return false;
    }
```
`args.get[0] 观察其数据类型，用于进一步处理`

######注： 修改完之后需重新安装插件
	```
	cordova plugin remove plugin_id
	cordova plugin add /xxx/
	```

----

3. 测试工程
	* 模拟数据
	
	```
	 window.plugins.myFirstPlugin.showAlert({title:"提示",
        message:"是否确认退出?",
        sureTitle:"确认",
        cancelTitle:"退出"},
      function(data){
        console.log(data);
      },
      function(error){
        console.log(error);
      });
	```

* 调试

	1. 打开chrome浏览器，输入chrome://inspect/#devices
		

	2. 打开android工程，在args参数处设置断点
		查看其数据类型，根据需求处理。

		注：耗时线程请开启子线程处理，cordova提供如下方式：
	```
	cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    
                }
            });
	```
		UI线程：
		```
		cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    
                }
            });
		```

* 查看数据类型
		
		![](http://i4.tietuku.com/a2a48c540c7a2908.jpg)
		
		由图可知，需要处理的数据类型为JsonObject.
		修改myFirstPlugin.java,处理数据.
		`            
		JSONObject jsonObject = args.getJSONObject(0);
		`

		```
		cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CharSequence sure = null,cancel = null;
                    AlertDialog.Builder builder=new AlertDialog.Builder(cordova.getActivity());  //先得到构造器
                    try {
                        String title = message.getString("title");
                        String msg   = message.getString("message");
                        sure         = message.getString("sureTitle");
                        cancel       = message.getString("cancelTitle");
                        builder.setTitle(title); //设置标题
                        builder.setMessage(msg); //设置内容

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    builder.setPositiveButton(sure, new DialogInterface.OnClickListener() { //设置确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); //关闭dialog
                            Toast.makeText(cordova.getActivity(), "确认" + which, Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() { //设置取消按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(cordova.getActivity(), "取消" + which, Toast.LENGTH_SHORT).show();
                        }
                    });
                    //参数都设置完成了，创建并显示出来
                    builder.create().show();
                }
            });
		```

		对话框如下图所示：
		
		![](http://i4.tietuku.com/066b20956c7bc652.png)

----

####注意事项

编写插件的同时最好编写一份插件的用法，放入插件目录中（README.md）

----
<h3 id="3">下载示例</h3>


[插件](https://github.com/SandLZ/myFirstPlugin/tree/master)：https://github.com/SandLZ/myFirstPlugin.git

[测试工程](https://github.com/SandLZ/myFirstApp): https://github.com/SandLZ/myFirstApp.git


	


