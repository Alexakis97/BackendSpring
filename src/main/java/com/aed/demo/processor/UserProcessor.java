package com.aed.demo.processor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.aed.demo.entity.UserLocation;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Service
public class UserProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProcessor.class);

    private List<Consumer<UserLocation>> listeners = new CopyOnWriteArrayList<>();

    public void register(Consumer<UserLocation> listener) {
        listeners.add(listener);
        LOGGER.info("Added a listener, for a total of {} listener{}", listeners.size(), listeners.size() > 1 ? "s" : "");
    }


    public void process(UserLocation userLocation) {
    	System.out.println("Processing: "+userLocation);
        listeners.forEach(c -> c.accept(userLocation));
    }
}
