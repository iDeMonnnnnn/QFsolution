## QFsolution - 适用于AndroidQ及以上的文件操作解决方案

[![](https://jitpack.io/v/iDeMonnnnnn/QFsolution.svg)](https://jitpack.io/#iDeMonnnnnn/QFsolution)

1. **适用于AndroidQ的简易图片选择器。**
2. **基于协程的系统文件选择，相册选择，系统拍照，系统裁剪。**
3. **Uri转为File的究极解决方案。**
4. **最新已兼容至Android12**
5. **兼容```Intent.ACTION_OPEN_DOCUMENT_TREE```选择文件夹Uri获取后路径**

### 开始使用
**使用详情可见[文档WIKI](https://github.com/iDeMonnnnnn/QFsolution/wiki)**

#### 添加依赖
```
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

[latest_version](https://github.com/iDeMonnnnnn/QFsolution/releases)
```
dependencies {
	implementation 'com.github.iDeMonnnnnn:QFsolution:$latest_version'
}
```

#### 添加权限
```
    <!--如果你使用相机相关功能必须要添加，否则可忽略-->
    <uses-permission android:name="android.permission.CAMERA" />
    <!--存储权限在低于AndroidQ的手机上还是需要的-->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <!--AndroidR及以上手机上访问sd卡需要存储管理的特殊权限，非文件管理类App不建议申请->
    <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
```


#### 更多使用

请见[Demo示例代码](https://github.com/iDeMonnnnnn/QFsolution/blob/master/app/src/main/java/com/demon/qf_app/MainActivity.kt)

可见[源码注释](https://github.com/iDeMonnnnnn/QFsolution/blob/master/solution/src/main/java/com/demon/qfsolution/utils/QFileExt.kt)，写的很详细。

### 使用体验

[demo.apk](https://github.com/iDeMonnnnnn/QFsolution/raw/master/QFsolution.apk)

<img src="https://github.com/iDeMonnnnnn/QFsolution/raw/master/demo.gif" alt="这是一张网络图片" height="600" width="300">


### 其他

如果你有问题或者建议，请[Issues](https://github.com/iDeMonnnnnn/QFsolution/issues).

[基于boxing的AndroidQ适配](https://github.com/iDeMonnnnnn/Qboxing)

### 致谢
[Android 10适配要点，作用域存储](https://blog.csdn.net/guolin_blog/article/details/105419420)

[boxing](https://github.com/bilibili/boxing)

### MIT License
```
Copyright (c) 2020 DeMon

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```


