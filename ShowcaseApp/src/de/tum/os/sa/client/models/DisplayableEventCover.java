package de.tum.os.sa.client.models;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class DisplayableEventCover extends BaseModelData {

	private static final String coverName = "name";
	private static final String coverUrl = "url";
	
	public DisplayableEventCover(String name, String url){
		setCoverName(name);
		setCoverUrl(url);
	}
	
	
	public String getCoverName(){
		return get(coverName);
	}
	
	public void setCoverName(String newCoverName){
		set(coverName, newCoverName);
	}
	
	public String getCoverUrl(){
		return get(coverUrl);
	}
	
	public void setCoverUrl(String newCoverUrl){
		set(coverUrl, newCoverUrl);
	}
	
	
	
}
