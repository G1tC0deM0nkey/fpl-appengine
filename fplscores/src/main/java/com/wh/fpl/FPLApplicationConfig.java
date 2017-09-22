package com.wh.fpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jkaye on 21/09/17.
 */
@Configuration
public class FPLApplicationConfig {

    @Value("${fpl.players.url}")
    private String fplPlayersUrl;

    @Value("${data.root}")
    private String dataRoot;

    public String getFplPlayersUrl() {
        return fplPlayersUrl;
    }

    public void setFplPlayersUrl(String fplPlayersUrl) {
        this.fplPlayersUrl = fplPlayersUrl;
    }

    public String getDataRoot() {
        return dataRoot;
    }

    public void setDataRoot(String dataRoot) {
        this.dataRoot = dataRoot;
    }
}
