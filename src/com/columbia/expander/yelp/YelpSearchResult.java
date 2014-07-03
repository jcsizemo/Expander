package com.columbia.expander.yelp;

import java.util.List;

// the meat of the Yelp result. Parsed data will be turned into one of these. Used extensively in Expander.
public class YelpSearchResult {
  private YelpError error;
  private Region region;
  private int total;
  private List<Business> businesses;
  public YelpError getError() {
    return error;
  }
  public void setError(YelpError error) {
    this.error = error;
  }
  public Region getRegion() {
    return region;
  }
  public void setRegion(Region region) {
    this.region = region;
  }
  public int getTotal() {
    return total;
  }
  public void setTotal(int total) {
    this.total = total;
  }
  public List<Business> getBusinesses() {
    return businesses;
  }
  public void setBusinesses(List<Business> businesses) {
    this.businesses = businesses;
  }
  
}