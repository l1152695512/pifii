/*    */ package com.ifidc.common;
/*    */ 
/*    */ public class ServerConfig
/*    */ {
/*    */   private String ipAddr;
/*    */   private int port;
/*    */   private String protocol;
/*    */   private String filter;
/*    */ 
/*    */   public String getFilter()
/*    */   {
/* 15 */     return this.filter;
/*    */   }
/*    */ 
/*    */   public void setFilter(String filter) {
/* 19 */     this.filter = filter;
/*    */   }
/*    */ 
/*    */   public String getIpAddr() {
/* 23 */     return this.ipAddr;
/*    */   }
/*    */ 
/*    */   public void setIpAddr(String ipAddr) {
/* 27 */     this.ipAddr = ipAddr;
/*    */   }
/*    */ 
/*    */   public int getPort() {
/* 31 */     return this.port;
/*    */   }
/*    */ 
/*    */   public void setPort(int port) {
/* 35 */     this.port = port;
/*    */   }
/*    */ 
/*    */   public String getProtocol()
/*    */   {
/* 40 */     return this.protocol;
/*    */   }
/*    */ 
/*    */   public void setProtocol(String protocol) {
/* 44 */     this.protocol = protocol;
/*    */   }
/*    */ }

/* Location:           D:\cache\windows\Desktop\logServer\
 * Qualified Name:     com.ifidc.common.ServerConfig
 * JD-Core Version:    0.6.0
 */