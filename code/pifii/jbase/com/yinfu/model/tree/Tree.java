
package com.yinfu.model.tree;

import java.util.ArrayList;
import java.util.List;

/**自定义tree模型
 * @author JiaYongChao
 * 2014年7月24日
 */
public class Tree implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;//
	private String parentId;//父ID
	private String level;//级别
	private String menuType;//菜单类型
	private String name;//菜单名称
	private String url;//菜单路径
	private Integer sort;//排序
	private String description;//描述
	public List<Tree> childrens =new ArrayList<Tree>();
	public Tree() {
		super();
	}

	public Tree(String id, String parentId, String level,String menuType, String name, String url, Integer sort,
			String description, List<Tree> childrens) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.level = level;
		this.menuType = menuType;
		this.name = name;
		this.url = url;
		this.sort = sort;
		this.description = description;
		this.childrens = childrens;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Tree> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<Tree> childrens) {
		this.childrens = childrens;
	}
	
}
