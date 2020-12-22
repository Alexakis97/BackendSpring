package com.aed.demo.cluster;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class InitServiceCluster {
	Cluster cluster = Cluster.getInstance();
	
	ClusterNomoi clusterNomoi = ClusterNomoi.getInstance();
	
	ClusterDimoi clusterDimoi = ClusterDimoi.getInstance();
	
	
	

	public void loadCluster1()
	{
		cluster.loadCluster();
	}
	
	
	public void loadCluster2()
	{
		
		clusterNomoi.loadCluster();
	}
	
	
	public void loadCluster3()
	{

		clusterDimoi.loadCluster();
	}

	
	
	public String returnClusterValue(Point p,String lang)
	{
		return cluster.getClusterFeature(p, lang);
		
	}
	
	public String returnClusterValueNomoi(Point p,String lang)
	{
		return clusterNomoi.getClusterFeature(p, lang);
		
	}
	
	public String returnClusterValueDimoi(Point p,String lang)
	{
		return clusterDimoi.getClusterFeature(p, lang);
		
	}
}
