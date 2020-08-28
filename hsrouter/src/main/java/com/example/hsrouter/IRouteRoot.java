package com.example.hsrouter;

import java.util.Map;

public interface IRouteRoot {

    /**
     * Load routes to input
     * @param routes input
     */
    void loadInto(Map<String, Class<?>> routes);
}