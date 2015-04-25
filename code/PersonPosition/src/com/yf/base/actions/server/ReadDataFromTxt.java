package com.yf.base.actions.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadDataFromTxt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 
	}
	
	/**
	  * 读取数据
	  */
	 public  double[][] ReadData(String path,int xlength,int ylength){
	  try {
	   FileReader read = new FileReader(path);
	   BufferedReader br = new BufferedReader(read);
	   String row;
	   double[][] posarr=new double[xlength][ylength];
	   int i=0;
	   while((row = br.readLine())!=null){
		   if(i!=0)
		   {
			   String[] arr=row.split("\t");
			   posarr[i-1][0]=Double.parseDouble(arr[0]);
			   posarr[i-1][1]=Double.parseDouble(arr[1]);
			   posarr[i-1][2]=Double.parseDouble(arr[2]);
			   System.out.println(posarr[i-1][0]+";"+posarr[i-1][1]+";"+posarr[i-1][2]);
		   }
		   i++;
		   
	   }
	  
	   return posarr;
	  } catch (FileNotFoundException e) {
	   e.printStackTrace();
	  } catch (IOException e){
	   e.printStackTrace();
	  }
	   return null;
	 }

}
