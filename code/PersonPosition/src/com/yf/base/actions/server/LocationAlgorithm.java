package com.yf.base.actions.server;

import java.io.File;

import com.yf.tradecontrol.GlobalVar;

public class LocationAlgorithm {
	//ָ�ƿ⣺��λ�� id,wifi id,rssi ��ֵ������
	public double[][] locat_wifi_data;
	//λ����Ϣ�������ָ����ʾλ��id ������x������y������z�����ߣ�����x������y��¥��z��
	public double[][] locat_data;
	//�Ƿ�ȥ�������������ȫ��ͬ��ָ�ƿ��¼
	public boolean isIgnoreSameRow;

	public LocationAlgorithm(){
		
	}
	public LocationAlgorithm(double[][] locat_wifi_data,double[][] locat_data){
		this.locat_wifi_data=locat_wifi_data;
		this.locat_data=locat_data;
	}
	//��λ�㷨
	/*
	 * posi_rssi������λ�õ�wifi�ź����У���ָ�ƿ��е����һһ��Ӧ��
	 * algorithmCode���㷨��ţ�1��ʾ����ڣ�2��ʾK����ڣ�3��ʾ��ȨK�����
	 * kValue���㷨��������ʾ����ڵĸ���
	 * testType���������ͣ�1��ʾ����ָ�ƿ��������������ͬ���У�������ʾ������
	 */
	public double[] location(double[] posi_rssi,int algorithmCode,int kValue,int testType){
		double[] resultData=new double[1];
		if(locat_wifi_data==null){
			return null;
		}
		int ignoreIndex=-1;
		if(testType==0){
			isIgnoreSameRow=false;
		}else{
			isIgnoreSameRow=true;
			ignoreIndex=secrchIndex(locat_wifi_data,posi_rssi);
		}
		if(ignoreIndex==-1){
			isIgnoreSameRow=false;
		}
		if(kValue<1){
			kValue=1;
		}
		if(kValue>10){
			kValue=10;
		}
		if(algorithmCode==1){
			resultData=recognitionByNN(locat_wifi_data,locat_data,posi_rssi,ignoreIndex);
		}
		if(algorithmCode==2){
			resultData=recognitionByKNN(locat_wifi_data,locat_data,posi_rssi,kValue,ignoreIndex);
		}
		if(algorithmCode==3){
			resultData=recognitionByWKNN(locat_wifi_data,locat_data,posi_rssi,kValue,ignoreIndex);
		}
		return resultData;
	}
	
//	��ȨK������㷨ʶ��
	public double[] recognitionByWKNN(double[][] dataList,double[][] poList,double[] testData,int kValue,int pointIndex){
		double[] distList=getDistBetweenDataList(dataList,testData);
		double[][] KPoint=new double[kValue][4];
		double[] KDistValues=new double[kValue];
		double[] KDistValuesPre=new double[kValue];
		double[] aimPoint=new double[4];
		double maxKDistValues=0;
		for(int i=0;i<kValue;i++){
			int index=fineBigValuePoint(distList,pointIndex);
			KPoint[i]=poList[index];
			KDistValues[i]=distList[index];
			distList[index]=Double.MAX_VALUE;
		}
		maxKDistValues=KDistValues[kValue-1];
		for(int i=0;i<kValue;i++){
			KDistValues[i]=maxKDistValues-KDistValues[i];
		}
		double sumDist=0;
		for(int i=0;i<kValue;i++){
			sumDist+=KDistValues[i];
		}
		if(sumDist==0){
			for(int i=0;i<kValue;i++){
				KDistValuesPre[i]=1/kValue;
			}
		}else{
			for(int i=0;i<kValue;i++){
				KDistValuesPre[i]=KDistValues[i]/sumDist;
			}
		}
		aimPoint[0]=0;
		aimPoint[1]=0;
		aimPoint[2]=0;
		for(int i=0;i<kValue;i++){
			aimPoint[0]+=KPoint[i][0]*KDistValuesPre[i];
			aimPoint[1]+=KPoint[i][1]*KDistValuesPre[i];
			aimPoint[2]+=KPoint[i][2]*KDistValuesPre[i];
		}
		return aimPoint;
	}
	//K������㷨ʶ��
	public double[] recognitionByKNN(double[][] dataList,double[][] poList,double[] testData,int kValue,int pointIndex){
		double[] distList=getDistBetweenDataList(dataList,testData);
		double[][] KPoint=new double[kValue][4];
		double[] aimPoint=new double[4];
		for(int i=0;i<kValue;i++){
			int index=fineBigValuePoint(distList,pointIndex);
			KPoint[i]=poList[index];
			distList[index]=Double.MAX_VALUE;
		}
		double sum_x=0;
		double sum_y=0;
		double sum_z=0;
		for(int i=0;i<kValue;i++){
			sum_x+=KPoint[i][0];
			sum_y+=KPoint[i][1];
			sum_z+=KPoint[i][2];
		}
		aimPoint[0]=sum_x/kValue;
		aimPoint[1]=sum_y/kValue;
		aimPoint[2]=sum_z/kValue;
		return aimPoint;
	}
	//Ѱ����Сֵλ�õ�
	public int  fineBigValuePoint(double[] distList,int pointIndex){
		int index=0;
		double minDist=Double.MAX_VALUE;
		for(int i=0;i<distList.length;i++){
			if(minDist>distList[i]&&i!=pointIndex){
				minDist=distList[i];
				index=i;
			}
		}
		return index;
	}
//	������㷨ʶ��
	public double[] recognitionByNN(double[][] dataList,double[][] poList,double[] testData,int pointIndex){
		int index=0;
		double minDist=Double.MAX_VALUE;
		double[] distList=getDistBetweenDataList(dataList,testData);
		for(int i=0;i<distList.length;i++){
			if(minDist>distList[i]&&i!=pointIndex){
				minDist=distList[i];
				index=i;
			}
		}
		return poList[index];
	}
//	��ȡ���Ե���ָ�ƿ��¼֮���ŷʽ��������
	public double[] getDistBetweenDataList(double[][] dataList,double[] data2){
		double[] distList=new double[dataList.length];
		for(int i=0;i<distList.length;i++){
			distList[i]=getDistBetweenData(new double[]{dataList[i][1],dataList[i][2]},data2);
		}
		return distList;
	}
	
//	����������֮���ŷʽ����
	public double getDistBetweenData(double[] data1,double[] data2){
		double dist=0;
		if(data1.length!=data2.length){
			return dist;
		}
		for(int i=0;i<data1.length;i++){
			dist+=(data1[i]-data2[i])*(data1[i]-data2[i]);
		}
		if(data1.length>0){
			dist=dist/data1.length;
		}
		dist=Math.sqrt(dist);
		return dist;
	}
	//Ѱ����ָ�ƿ��м�¼��ȫ��ͬ�ļ�¼���±�
	public int secrchIndex(double[][] dataList,double[] testData){
		int index=-1;
		boolean isFine=true;
		for(int i=0;i<dataList.length;i++){
			isFine=true;
			for(int j=0;j<dataList[i].length;j++){
				if(dataList[i][j]!=testData[j]){
					isFine=false;
					continue;
				}
			}
			if(isFine){
				index=i;
				continue;
			}
		}
		return index;
	}
	
	public static void main(String[] args) {
		ReadDataFromTxt rDataFromTxt=new ReadDataFromTxt();
		double[][] locat_wifi_data=rDataFromTxt.ReadData(GlobalVar.WORKPATH+File.separator+"config"+File.separator+"wifi.txt", 33, 3);
		double[][] locat_data=rDataFromTxt.ReadData(GlobalVar.WORKPATH+File.separator+"config"+File.separator+"pos.txt", 33, 3);
		LocationAlgorithm lAlgorithm=	new LocationAlgorithm(locat_wifi_data, locat_data);
		double[] posi_rssi={-628.0,-37.0};
		double[] lbs=lAlgorithm.location(posi_rssi, 3, 3,0);
		System.out.println("λ������Ϊ��x="+lbs[1]+";y="+lbs[2]);
		
	}
}
