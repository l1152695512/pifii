/*    */ package com.ifidc.persistent;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.sql.Timestamp;
/*    */ 
/*    */ public class BpBaseLogTbl
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -1126492166326690339L;
/*    */   private Integer id;
/*    */   private String deviceNo;
/*    */   private String type;
/*    */   private String inputMac;
/*    */   private String ip;
/*    */   private String link;
/*    */   private Timestamp createDate;
/*    */ 
/*    */   public BpBaseLogTbl()
/*    */   {
/*    */   }
/*    */ 
/*    */   public BpBaseLogTbl(String deviceNo, String type, String inputMac, String ip, String link, Timestamp createDate)
/*    */   {
/* 34 */     this.deviceNo = deviceNo;
/* 35 */     this.type = type;
/* 36 */     this.inputMac = inputMac;
/* 37 */     this.ip = ip;
/* 38 */     this.link = link;
/* 39 */     this.createDate = createDate;
/*    */   }
/*    */ 
/*    */   public Integer getId()
/*    */   {
/* 45 */     return this.id;
/*    */   }
/*    */ 
/*    */   public void setId(Integer id) {
/* 49 */     this.id = id;
/*    */   }
/*    */ 
/*    */   public String getDeviceNo() {
/* 53 */     return this.deviceNo;
/*    */   }
/*    */ 
/*    */   public void setDeviceNo(String deviceNo) {
/* 57 */     this.deviceNo = deviceNo;
/*    */   }
/*    */ 
/*    */   public String getType() {
/* 61 */     return this.type;
/*    */   }
/*    */ 
/*    */   public void setType(String type) {
/* 65 */     this.type = type;
/*    */   }
/*    */ 
/*    */   public String getInputMac() {
/* 69 */     return this.inputMac;
/*    */   }
/*    */ 
/*    */   public void setInputMac(String inputMac) {
/* 73 */     this.inputMac = inputMac;
/*    */   }
/*    */ 
/*    */   public String getIp() {
/* 77 */     return this.ip;
/*    */   }
/*    */ 
/*    */   public void setIp(String ip) {
/* 81 */     this.ip = ip;
/*    */   }
/*    */ 
/*    */   public String getLink() {
/* 85 */     return this.link;
/*    */   }
/*    */ 
/*    */   public void setLink(String link) {
/* 89 */     this.link = link;
/*    */   }
/*    */ 
/*    */   public Timestamp getCreateDate() {
/* 93 */     return this.createDate;
/*    */   }
/*    */ 
/*    */   public void setCreateDate(Timestamp createDate) {
/* 97 */     this.createDate = createDate;
/*    */   }
/*    */ }

/* Location:           D:\cache\windows\Desktop\logServer\
 * Qualified Name:     com.ifidc.persistent.BpBaseLogTbl
 * JD-Core Version:    0.6.0
 */