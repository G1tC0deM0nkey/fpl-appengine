package com.wh.fpl.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by johnk on 03/11/2017.
 */
@Controller(value = "/server")
public class ServerController {

    private ScheduledExecutorService exec = null;

    @RequestMapping(value = "/health", method = RequestMethod.GET, produces = "text/html")
    public @ResponseBody
    String health() {
        return "UP";
    }

    @RequestMapping(value = "/shutdown", method = RequestMethod.GET, produces = "text/html")
    public @ResponseBody String shutdown(@RequestParam String key) {
        if("Cheddars".equals(key)) {
            ShutdownTask task = new ShutdownTask();
            exec = Executors.newSingleThreadScheduledExecutor();
            exec.schedule(task, 10000, TimeUnit.MILLISECONDS);
        }

        return "OK";
    }

    private class ShutdownTask implements Runnable {

        public void run() {
            try {
                Thread.sleep(10000);
            } catch(Exception e) {
            }

            System.exit(0);
        }

    }
}
