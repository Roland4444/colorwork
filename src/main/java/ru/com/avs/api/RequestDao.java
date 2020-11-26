package ru.com.avs.api;

import com.sun.jersey.api.client.ClientResponse;

interface RequestDao {

    ClientResponse requestToken();

    ClientResponse getMetals(Token token);

    ClientResponse getDepartments(Token token);

    ClientResponse exportWaybill(String json, Token token);

    ClientResponse deleteWeighing(String json, Token token);
}
