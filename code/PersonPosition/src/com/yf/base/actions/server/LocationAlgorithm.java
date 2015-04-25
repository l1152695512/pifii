package com.yf.base.actions.server;

import java.io.File;

import com.yf.tradecontrol.GlobalVar;

public class LocationAlgorithm {
	//指纹库：（位置 id,wifi id,rssi 数值）矩阵
	public double[][] locat_wifi_data;
	//位置信息详情矩阵：指引表示位置id （坐标x、坐标y、坐标z）或者（坐标x、坐标y、楼层z）
	public double[][] locat_data;
	//是否去掉与测试数据完全相同的指纹库记录
	public boolean isIgnoreSameRow;

	public LocationAlgorithm(){
		
	}
	public LocationAlgorithm(double[][] locat_wifi_data,double[][] locat_data){
		this.locat_wifi_data=locat_wifi_data;
		this.locat_data=locat_data;
	}
	//定位算法
	/*
	 * posi_rssi：测试位置的wifi信号序列（与指纹库中的序号一一对应）
	 * algorithmCode：算法编号，1表示最近邻，2表示K最近邻，3表示加权K最近邻
	 * kValue：算法参数，表示最近邻的个数
	 * testType：测试类型，1表示忽略指纹库中与测试数据相同的行，其他表示不忽略
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
	
//	加权K最近邻算法识别
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
	//K最近邻算法识别
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
	//寻找最小值位置点
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
//	最近邻算法识别
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
//	获取测试点与指纹库记录之间的欧式距离序列
	public double[] getDistBetweenDataList(double[][] dataList,double[] data2){
		double[] distList=new double[dataList.length];
		for(int i=0;i<distList.length;i++){
			distList[i]=getDistBetweenData(new double[]{dataList[i][1],dataList[i][2]},data2);
		}
		return distList;
	}
	
//	计算两个点之间的欧式距离
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
	//寻找与指纹库中记录完全相同的记录的下标
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
		System.out.println("位置坐标为：x="+lbs[1]+";y="+lbs[2]);
		
	}
}
