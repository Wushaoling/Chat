package pro.util;
/**
 * 消息类型枚举
 * @author wushaoling
 *
 */
public enum EnumInfoType {
	LOGIN,//登录消息
	QUIT,//在登录界面退出
	SEND_INFO,//发送单聊的消息
	SEND_ALL,//发送群聊的消息
	ADD_USER,//向其他用户更新用户列表
	DELETE_USER,//用户下线，删除用户
	LOGIN_RESUALT,//注册结果
	LOAD_USER,//当前用户的用户列表加载
	REGISTER,//注册消息
	SYSTEMINFO;//系统消息
}
