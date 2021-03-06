package org.espressoOtr.exs.test;

import static org.junit.Assert.*;

import org.espressoOtr.exs.api.ApiManager;
import org.junit.Before;
import org.junit.Test;

public class TestNaverApi
{
  ApiManager apiManager = null; 
    
    @Before
    public void setUp() throws Exception
    { 
        System.setProperty("naver_key", "776db691996584df9385aa576bd4dcef");
        System.setProperty("daum_key", "ffb014a6c553ddd4e93ce7af11ba075a75dd8e34");
        System.setProperty("bing_key", "XewnW370WT5kdz9ECFie99d5miv/2777Hg0dt0kBp10=");
        
        apiManager = new ApiManager();
    }
    
    @Test
    public void test_naver_blog() throws Exception
    {
        apiManager.request("NAVER.BLOG", "twitter log", 3, 1);
        assertEquals(3, apiManager.response().size()); 
    }
    
    @Test
    public void test_naver_cafe_article() throws Exception
    {
        apiManager.request("NAVER.CAFEARTICLE", "twitter log", 3, 1);
        assertEquals(3, apiManager.response().size()); 
    }
    
    
    
    @Test
    public void test_naver_webkr() throws Exception
    {
        apiManager.request("NAVER.WEBKR", "twitter log", 3, 1);
        assertEquals(3, apiManager.response().size()); 
    }
    
    
    
    
    @Test
    public void test_naver_news() throws Exception
    {
        apiManager.request("NAVER.NEWS", "twitter log", 3, 1);
        assertEquals(3, apiManager.response().size()); 
    }
}
