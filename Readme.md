## QFsolution - 适用于AndroidQ的文件操作解决方案

[![](https://jitpack.io/v/iDeMonnnnnn/QFsolution.svg)](https://jitpack.io/#iDeMonnnnnn/QFsolution)

1. **适用于AndroidQ的简易图片选择器。**
2. **基于协程的系统文件选择，系统拍照，系统裁剪。**
3. **Uri转为File的究极解决方案。**

**[文档WIKI](https://github.com/iDeMonnnnnn/QFsolution/wiki)**

### 开始使用
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
```xml
    <!--如果你使用相机相关功能必须要添加，否则可忽略-->
    <uses-permission android:name="android.permission.CAMERA" />
    <!--存储权限在低于AndroidQ的手机上还是需要的-->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

### 效果动图
[下载demo.apk体验](https://github.com/iDeMonnnnnn/QFsolution/raw/master/demo.apk)

![xxx](https://github.com/iDeMonnnnnn/QFsolution/blob/master/ezgif.gif?raw=true)
### 使用方法

#### 初始化


```kotlin
//供一个全局的Context
QFHelper.init(this)
```

#### FileProvider

```kotlin
        /**
         *考虑到不同项目中FileProvider的authorities可能不一样
         *因此这里改成可以根据自己项目FileProvider的authorities自由设置
         *如:android:authorities="${applicationId}.file.provider",你只需要传入“file.provider”即可
         */
        QFHelper.setFileProvider("file.provider")
```

```xml
        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.file.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>
```

#### 图片选择器

1.在启动图片选择器之前，你需要初始化图片加载器,你可以参考示例代码的[GlideLoader](https://github.com/iDeMonnnnnn/QFsolution/blob/master/app/src/main/java/com/demon/qf_app/GlideLoader.kt)
，实现IQFImgLoader接口，此举是为了解决不同项目的使用不同图片加载库的问题和减少库体积。

```kotlin
QFHelper.initImgLoader(GlideLoader()) //初始化图片加载器
```

2.配置参数，启动图片选择库。
```kotlin
QFHelper.isNeedGif(false) //是需要gif，默认false
        .isNeedCamera(true)  //是否需要拍照选项，默认true
        .setSpanCount(3) //每行显示多少张图片，默认&建议：3, 可根据手机分辨率实际情况大小进行调整
        .setLoadNum(30) //设置分页加载每次加载多少张图片，默认&建议：30，可根据手机分辨率实际情况大小进行调整，注意：该值最少应该保证首次加载充满全屏，否则无法加载更多
        .setMaxNum(9) //设置可选择最多maxNum张图片，默认&最小值：1
        .start(this, 0x001) //第一个参数：activity或者fragment，第二个参数requestCode
```

3.获取选取图片后的结果

```kotlin
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                0x001 -> {
                    val uris = QFHelper.getResult(data)
                    uris?.run {
                        uri = this[0]
                        Log.i(TAG, "onActivityResult: $uri")
                        binding.img.setImageURI(uri)
                    }
                }
            }
        }
    }
```


#### 文件操作
本库的文件操作包括系统文件选择，系统拍照，系统裁剪三种。

都是基于**携程+GhostFragment**的方式，因此需要在**协程**中使用，操作完成后可直接获取到返回值。

返回值根据泛型类型返回对应类型的结果：Uri：文件的Uri对象，File：文件对象，String：文件的绝对路径。

更多使用细节，可见[源码注释](https://github.com/iDeMonnnnnn/QFsolution/blob/master/solution/src/main/java/com/demon/qfsolution/utils/QFileExt.kt)，写的很详细。

1.系统文件选择
```kotlin
GlobalScope.launchUI {
uri = openFile<String>()?.run {  File(this).toUri() }
img.setImageURI(uri)
}
```

2.系统拍照
```kotlin
GlobalScope.launchUI {
     uri = gotoCamera(fileName = "DeMon-${System.currentTimeMillis()}.jpg")
     img.setImageURI(uri)
}
```

3.系统比例裁剪

```kotlin
GlobalScope.launchUI {
    uri?.run {
       uri = startCrop(this, 300, 600)
       img.setImageURI(uri)
  }
}
```

4.系统自由裁剪

```kotlin
GlobalScope.launchUI {
    uri?.run {
       uri = startCrop(this)
       img.setImageURI(uri)
  }
}
```

### Uri转File
如何获取```content://URI```格式的Uri文件获取对应的File对象，一直是日常开发中的痛点。

网上各种解决方案都不尽人意，本库旨在适配各种可能存在的情况，如果还是获取不到会将该Uri写入沙盒环境，再获取对应的File对象，这样基本能100%保证获取到Uri的File对象。

AndroidQ开始无法访问非作用域存储内的文件（沙盒环境），只能通过Uri去访问文件，因此访问外部存储文件只能，按照如上方法通过将Uri写入沙盒环境成新文件，再获取File。

使用本库的```uriToFile```扩展方法即可，如下：
```kotlin
  val file: File? = uri.uriToFile()
```


**更多使用详情，请见[Demo示例](https://github.com/iDeMonnnnnn/QFsolution/tree/master/app)及[WIKI](https://github.com/iDeMonnnnnn/QFsolution/wiki)**

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


