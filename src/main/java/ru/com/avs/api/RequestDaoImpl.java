package ru.com.avs.api;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ru.com.avs.service.PropertyService;

@Repository("RequestDao")
class RequestDaoImpl implements RequestDao {

    private Client client;

    @Autowired
    private PropertyService property;

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);
    }

    @Override
    public ClientResponse requestToken() {
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("login", property.getProperty("endpoint.api.login"));
        formData.add("password", property.getProperty("endpoint.api.password"));

        return client
                .resource(property.getProperty("endpoint.api"))
                .path(property.getProperty("endpoint.api.token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(ClientResponse.class, formData);
    }

    @Override
    public ClientResponse getMetals(Token token) {
        return client
                .resource(property.getProperty("endpoint.api"))
                .path(property.getProperty("endpoint.api.metal"))
                .header("Authorization", token.getToken())
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(ClientResponse.class);
    }

    @Override
    public ClientResponse getDepartments(Token token) {
        return client
                .resource(property.getProperty("endpoint.api"))
                .path(property.getProperty("endpoint.api.department"))
                .header("Authorization", token.getToken())
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(ClientResponse.class);
    }

    @Override
    public ClientResponse exportWaybill(String json, Token token) {
        return client
                .resource(property.getProperty("endpoint.api"))
                .path(property.getProperty("endpoint.api.export"))
                .header("Authorization", token.getToken())
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, json);
    }

    @Override
    public ClientResponse deleteWeighing(String weighingId, Token token) {
        return client
                .resource(property.getProperty("endpoint.api"))
                .path(property.getProperty("endpoint.api.export"))
                .queryParam("weighingId", weighingId)
                .header("Authorization", token.getToken())
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .delete(ClientResponse.class);
    }
}
