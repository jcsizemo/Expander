package com.columbia.expander.yelp;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

// interface to Yelp, handles calls and returned data
public class YelpService {
  public final static String CONSUMER_KEY = "PbSjw9p08To_HZnFYqpRZg";
  public final static String CONSUMER_SECRET = "_raqYwj6njZ15sXj4z-CYEDHiHQ";
  public final static String TOKEN = "eD5hAer1WOGQCtev0G7fQNsnuag2ohyH";
  public final static String TOKEN_SECRET = "ZHdFNXgEuNbF2-rvCkCuAmtyfsM";

  private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

  public static BusinessDetail findBusiness(String id) {
    String endpoint = "http://api.yelp.com/v2/business/" + id;
    OAuthService service = new ServiceBuilder().provider(YelpApi2.class).apiKey(CONSUMER_KEY).apiSecret(CONSUMER_SECRET).build();
    Token accessToken = new Token(TOKEN, TOKEN_SECRET);
    OAuthRequest request = new OAuthRequest(Verb.GET, endpoint);
    service.signRequest(accessToken, request);
    Response response = request.send();
    String jsonResult = response.getBody();
    Log.d(YelpService.class.getName(), "Response: " + jsonResult);
    BusinessDetail bd = null;
    try {
      bd = gson.fromJson(jsonResult, BusinessDetail.class);
    } catch (JsonSyntaxException ex) {
      Log.e(YelpService.class.getName(), ex.getCause() + " : " + ex.getLocalizedMessage());
    }
    return bd;
  }

  public static YelpSearchResult search(Double lat, Double lng, String location, String term, String category_filter, int radius, int limit, int offset,
      String sort) {
    String endpoint = "http://api.yelp.com/v2/search";
    OAuthService service = new ServiceBuilder().provider(YelpApi2.class).apiKey(CONSUMER_KEY).apiSecret(CONSUMER_SECRET).build();
    Token accessToken = new Token(TOKEN, TOKEN_SECRET);
    OAuthRequest request = new OAuthRequest(Verb.GET, endpoint);
    if (lat != null && lng != null) {
      request.addQuerystringParameter("ll", lat + "," + lng);
    }
    if (location != null && location.trim().length() > 0) {
      request.addQuerystringParameter("location", location.replaceAll("\n", ", "));
    }
    if (term != null) {
      request.addQuerystringParameter("term", term);
    }
    if (category_filter != null) {
      request.addQuerystringParameter("category_filter", category_filter);
    }
    request.addQuerystringParameter("radius", Integer.toString(radius));
    request.addQuerystringParameter("limit", Integer.toString(limit));
    request.addQuerystringParameter("offset", Integer.toString(offset));
    request.addQuerystringParameter("sort", sort);
    service.signRequest(accessToken, request);
    Response response = request.send();
    String jsonResult = response.getBody();
    Log.d(YelpService.class.getName(), "Response: " + jsonResult);
    YelpSearchResult places = null;
    try {
      places = gson.fromJson(jsonResult, YelpSearchResult.class);
    } catch (JsonSyntaxException ex) {
      Log.e(YelpService.class.getName(), ex.getCause() + " : " + ex.getLocalizedMessage());
    }
    return places;
  }

}