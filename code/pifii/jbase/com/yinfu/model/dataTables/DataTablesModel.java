
package com.yinfu.model.dataTables;

import java.io.Serializable;
import java.util.List;
import com.alibaba.fastjson.JSONObject;

/**
 * Jquery dataTables
 * 
 * @author L.cm <url>http://www.oschina.net/code/snippet_186208_21569</url>
 */
public class DataTablesModel<M> implements Serializable{
 
    private static final long serialVersionUID = -7326751222887130277L;
     
    private long iTotalRecords;  
    private long iTotalDisplayRecords;  
    private String sEcho;
    private List<M> aaData;    // rows
    public long getiTotalRecords() {
        return iTotalRecords;
    }
    public void setiTotalRecords(long iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }
    public long getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }
    public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }
    public String getsEcho() {
        return sEcho;
    }
    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }
    public List<M> getAaData() {
        return aaData;
    }
    public void setAaData(List<M> aaData) {
        this.aaData = aaData;
    }
    public DataTablesModel() {}
    public DataTablesModel(long iTotalRecords, long iTotalDisplayRecords, String sEcho, List<M> aaData) {
        super();
        this.iTotalRecords = iTotalRecords;
        this.iTotalDisplayRecords = iTotalDisplayRecords;
        this.sEcho = sEcho;
        this.aaData = aaData;
    }
}
