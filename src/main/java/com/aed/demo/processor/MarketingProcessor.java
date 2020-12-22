package com.aed.demo.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.aed.demo.entity.MarketingObject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Service
public class MarketingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketingProcessor.class);

    private List<Consumer<MarketingObject>> listeners = new CopyOnWriteArrayList<>();

    public void register(Consumer<MarketingObject> listener) {
        listeners.add(listener);
        LOGGER.info("Added a listener, for a total of {} listener{}", listeners.size(), listeners.size() > 1 ? "s" : "");
    }


    public void process(MarketingObject event) {
    	System.out.println("Processing: "+event);
        listeners.forEach(c -> c.accept(event));
    }
    //Test Git
}
