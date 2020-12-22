package com.aed.demo.keepAlive;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;


import com.aed.demo.entity.Ambulance;
import com.aed.demo.entity.Event;
import com.aed.demo.processor.AmbulanceProcessor;
import com.aed.demo.processor.AsirmatistisProcessor;
import com.aed.demo.processor.CompletedProcessor;
import com.aed.demo.processor.Agios_Loukas_Processor;
import com.aed.demo.processor.Iaso_Thessalias_Processor;


@Service
public class ConsumeServerSentEvent {
	
	
	@Autowired
    private CompletedProcessor completedProcessor;
	
	@Autowired
    private AmbulanceProcessor ambulanceProcessor;
	
	@Autowired
    private AsirmatistisProcessor asirmatistisProcessor;
	
	@Autowired
	private Agios_Loukas_Processor agios_loukas_processor ;
	
	@Autowired
	private Iaso_Thessalias_Processor iaso_thessalias_processor ;
	

	
	public void KeepAliveProcessor()
	{
		ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
	    sseMvcExecutor.execute(() -> {
	        try {
	            while(true){
	            	Thread.sleep(180000);
	            	Event dead = new Event();
	            	
	            	completedProcessor.process(dead);
	            	ambulanceProcessor.process(new Ambulance());
	            	asirmatistisProcessor.process(dead);
	            	agios_loukas_processor.process(dead);
	            	iaso_thessalias_processor.process(dead);
	          }
	        } catch (Exception ex) {
				ex.printStackTrace();
	        }
	    });
	}
	
}
