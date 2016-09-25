package com.zyx1011.mobilesafe002.constants;

import com.zyx1011.mobilesafe002.R;

import android.net.Uri;

/**
 * 存放程序中使用到的常量
 * 
 * @author zhongyuxin
 */
public interface Constants {

	/**
	 * 网络操作(升级)相关常量
	 * 
	 * @author zhongyuxin
	 */
	public interface HTTP_CONN {
		String HOST = "http://10.0.2.2:8080/"; // 主机地址
		String UPDATE_URL = HOST + "update.json"; // 主机中对应的更新检查的文件名
		int VERSION_UPDATE_Y = 0; // 版本升级
		int VERSION_UPDATE_N = -1; // 网络异常等导致不能升级
		int UPDATE_INSTALL_CODE = 1; // 安装返回请求码
	}

	/**
	 * 主界面相关常量
	 * 
	 * @author zhongyuxin
	 */
	public interface MAIN_MENU {
		// 主界面功能菜单
		// 主界面item标题
		String[] TITLE = { "手机防盗", "骚扰拦截", "软件管家", "进程管理", "流量统计", "手机杀毒", "缓存清理", "常用工具" };
		// 主界面item简述
		String[] DESC = { "远程定位", "全面拦截骚扰", "管理你的软件", "管理运行进程", "流量一目了然", "病毒无处藏身", "系统流畅如水", "工具大全" };
		// 主界面item图标资源id
		int[] PIC = { R.drawable.sjfd, R.drawable.srlj, R.drawable.rjgj, R.drawable.jcgl, R.drawable.lltj,
				R.drawable.sjsd, R.drawable.hcql, R.drawable.cygj };
		// 设置界面功能开关状态标记
		String VERSION_UPDATE = "version_update"; // 版本更新
		String INTERCEPT_HARRY = "intercept_harry"; // 骚扰拦截
		String PHONE_AREA = "phone_area"; // 归属地
		String[] METHODS = { "performPhoneProtect", "performInterceptHarry", "performSoftManager",
				"performProcessManager", "performFlowCount", "performAntiVirus", "performCacheClean",
				"performToolsManager" }; // 主界面GridView的item点击后执行的方法名(通过反射)
	}

	/**
	 * 手机防盗功能相关常量
	 * 
	 * @author zhongyuxin
	 */
	public interface PROTECT_PHONE {
		int REQ_ACTIVE_ADMIN = 1; // 激活设备管理员请求码
		String PASSWORD = "protect_password"; // 手机防盗密码
		String SIM_SERIAL_NUMBER = "sim_serial_number"; // sim卡序列号
		String SAFE_PHONE = "safe_phone"; // 安全手机号码
		String TELEPHONE = "telephone"; // 选择安全手机号码intent传递参数名
		String FLAG = "protect_phone_flag"; // 手机防盗是否开启标志
	}

	/**
	 * 数据库操作相关常量
	 * 
	 * @author zhongyuxin
	 */
	public interface DB {
		String DB_NAME = "InPhone.db"; // 数据库名
		int DB_VERSION = 1; // 数据库版本号
		String TABLE_NAME = "phonelist"; // 表名
		// 建表SQL语句
		String CREATE_TABLE_SQL = "create table " + TABLE_NAME + "(id integer primary key autoincrement,"
				+ "phone text unique not null," + "type integer not null)";
		String DROP_TABLE_SQL = "drop table " + TABLE_NAME; // 删表SQL语句

		String APP_DB_NAME = "applock.db";
		int APP_DB_VERSION = 1;
		String APP_TABLE_NAME = "lock";
		String APP_CREATE_TABLE_SQL = "create table " + APP_TABLE_NAME + "(id integer primary key autoincrement,"
				+ "package_name text unique not null)";
		String APP_DROP_TABLE_SQL = "drop table " + APP_TABLE_NAME;
	}

	/**
	 * 归属地显示风格相关常量
	 * 
	 * @author zhongyuxin
	 */
	public interface ADDRDIALOG {
		String SELECTED = "background_selected";
		String[] TITLES = { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" }; // 颜色文本
		int[] ICONS = { R.drawable.iv_addrtoast_normal, R.drawable.iv_addrtoast_orange, R.drawable.iv_addrtoast_blue,
				R.drawable.iv_addrtoast_gray, R.drawable.iv_addrtoast_green }; // 颜色图标
	}

	String PROTECT_SERVICE = "protect_service"; // 保护服务
	String SHOW_SYSTEM = "show_system_process"; // 系统进程
	String LOCK_SCREEN_CLEAN = "lock_screen_clean"; // 锁屏清理
	String WIDGET_SERVICE = "widget_service"; // widget控件

	String WATCH_DOG1 = "watch_dog1"; // 电子狗服务1
	String WATCH_DOG2 = "watch_dog2"; // 电子狗服务2

	String BROADCAST_ACTION = "com.zyx1011.mobilesafe002.watchdog.receiver"; // 电子狗传递参数

	Uri APPLOCK_URI = Uri.parse("content://com.zyx1011.mobilesafe002.watchdog.content");
}
