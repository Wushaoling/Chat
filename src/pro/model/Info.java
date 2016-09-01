package pro.model;

import java.io.Serializable;

import pro.util.EnumInfoType;
/**
 * 消息对象
 * @author wushaoling
 *
 */
public class Info implements Serializable{
	
	private final static long serialVersionUID = 1L;
	
	private String fromUser;
	private String toUser;
	private String sendTime;
	private String content;
	private EnumInfoType infotype;
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public EnumInfoType getInfotype() {
		return infotype;
	}
	public void setInfotype(EnumInfoType infotype) {
		this.infotype = infotype;
	}
	@Override
	public String toString() {
		return "Info [fromUser=" + fromUser + ", toUser=" + toUser
				+ ", sendTime=" + sendTime + ", content=" + content
				+ ", infotype=" + infotype + "]";
	}
	
	public Info() {
		super();
	}
	
	public Info(String fromUser, String toUser, String sendTime,
			String content, EnumInfoType infotype) {
		super();
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.sendTime = sendTime;
		this.content = content;
		this.infotype = infotype;
	}
	
}
  