package com.xframe.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseNews<M extends BaseNews<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}
	
	public java.lang.String getId() {
		return getStr("id");
	}

	public void setUuid(java.lang.String uuid) {
		set("uuid", uuid);
	}
	
	public java.lang.String getUuid() {
		return getStr("uuid");
	}

	public void setTitle(java.lang.String title) {
		set("title", title);
	}
	
	public java.lang.String getTitle() {
		return getStr("title");
	}

	public void setTime(java.lang.String time) {
		set("time", time);
	}
	
	public java.lang.String getTime() {
		return getStr("time");
	}

	public void setContent(java.lang.String content) {
		set("content", content);
	}
	
	public java.lang.String getContent() {
		return getStr("content");
	}

	public void setLink(java.lang.String link) {
		set("link", link);
	}
	
	public java.lang.String getLink() {
		return getStr("link");
	}

}
