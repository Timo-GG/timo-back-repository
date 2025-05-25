package com.tools.seoultech.timoproject.riot;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url = "https://ddragon.leagueoflegends.com")
public interface DataDragonClient {

    @GetExchange("/api/versions.json")
    List<String> getVersions();

    @GetExchange("/cdn/{version}/data/en_US/runesReforged.json")
    String getRuneData(@PathVariable String version);

}