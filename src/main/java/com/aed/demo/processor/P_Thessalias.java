package com.aed.demo.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.aed.demo.entity.Event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Service
public class P_Thessalias {

    private static final Logger LOGGER = LoggerFactory.getLogger(P_Thessalias.class);

    private List<Consumer<Event>> listeners = new CopyOnWriteArrayList<>();

    public void register(Consumer<Event> listener) {
        listeners.add(listener);
        LOGGER.info("Added a listener, for a total of {} listener{}", listeners.size(), listeners.size() > 1 ? "s" : "");
    }


    public void process(Event event) {
    	System.out.println("Processing: "+event);
        listeners.forEach(c -> c.accept(event));
    }
    //Test Git
}
