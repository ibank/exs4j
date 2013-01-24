package org.espressoOtr.exs.api.naver;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.espressoOtr.exs.api.ApiKey;
import org.espressoOtr.exs.api.SearchAPI; 
import org.espressoOtr.exs.api.result.SearchResult;
import org.espressoOtr.exs.api.result.TextSearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class NaverAPI implements SearchAPI
{
    
    private String uri = "";
    
    private NaverAPITarget target;
    
    private List<SearchResult> searchResultList = Collections.emptyList();
    
    private ApiKey apiKey = null;
    
    private String sort = "date";
    
    private int outputCountDefault = 10;
    private int outputCountLimit = 100;
    
    private int pageNoDefault = 1;
    private int pageNoLimit = 1000;
    
    private int outputCount = outputCountDefault;// default
    private int pageNo = pageNoDefault; // default
    
    Logger logger = LoggerFactory.getLogger(NaverAPI.class);
    
    public NaverAPI(ApiKey naverApiKey)
    {
        this.apiKey = naverApiKey;
    }
    
    public NaverAPI()
    {
        this.apiKey = new ApiKey(this);
    }
    
    public void setTarget(NaverAPITarget target)
    {
        
        this.target = target;
    }
    
    public String getTargetString()
    {
        String targetString = "";
        
        if (getTarget() == NaverAPITarget.BLOG)
        {
            targetString = "blog";
        }
        else if (getTarget() == NaverAPITarget.CAFEARTICLE)
        {
            targetString = "cafearticle";
        }
        else if (getTarget() == NaverAPITarget.NEWS)
        {
            targetString = "news";
        }
        else if (getTarget() == NaverAPITarget.WEBKR)
        {
            targetString = "webkr";
        }
        
        return targetString;
        
    }
    
    public NaverAPITarget getTarget()
    {
        return this.target;
        
    }
    
    public void request(String keyword)
    {
        
        logger.info(NaverAPI.class.getName() + " KEYWORD : " + "'" + keyword + "'");
        
        try
        {
            requestNaverAPI(keyword);
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void requestNaverAPI(String keyword) throws IOException, ParserConfigurationException, SAXException
    {
        
        uri = getURI(keyword);
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.parse(uri);
        
        this.searchResultList = parseDOM(doc);
    }
    
    private String getURI(String keyword) throws IOException
    {
        
        String createdUri = "http://openapi.naver.com/search?key=" + this.apiKey.getKey() + "&target=" + this.getTargetString() + "&query="
                + URLEncoder.encode(keyword, "UTF-8") + getSubURI();
        
        return createdUri;
    }
    
    private String getSubURI()
    {
        
        String subURI = "";
        if (getTarget() == NaverAPITarget.BLOG)
        {
            subURI = "&display=" + String.valueOf(this.outputCount) + "&start=" + String.valueOf(this.pageNo) + "&sort=" + this.sort;
        }
        
        else if (getTarget() == NaverAPITarget.CAFEARTICLE)
        {
            subURI = "&display=" + String.valueOf(this.outputCount) + "&start=" + String.valueOf(this.pageNo) + "&sort=" + this.sort;
            
        }
        else if (getTarget() == NaverAPITarget.NEWS)
        {
            subURI = "&display=" + String.valueOf(this.outputCount) + "&start=" + String.valueOf(this.pageNo) + "&sort=" + this.sort;
            
        }
        else if (getTarget() == NaverAPITarget.WEBKR)
        {
            subURI = "&display=" + String.valueOf(this.outputCount) + "&start=" + String.valueOf(this.pageNo);
            
        }
        
        return subURI;
    }
    
    private List<SearchResult> parseDOM(Document doc)
    {
        
        List<SearchResult> searchResultList = Collections.emptyList();
        
        if (getTarget() == NaverAPITarget.BLOG)
        {
            searchResultList = parseBlogDom(doc);
        }
        else if (getTarget() == NaverAPITarget.CAFEARTICLE)
        {
            searchResultList = parseCafeArticleDom(doc);
        }
        else if (getTarget() == NaverAPITarget.NEWS)
        {
            searchResultList = parseNewsDom(doc);
        }
        else if (getTarget() == NaverAPITarget.WEBKR)
        {
            searchResultList = parseWebKrDom(doc);
        }
        
        return searchResultList;
    }
    
    private List<SearchResult> parseBlogDom(Document doc)
    {
        List<SearchResult> searchResultList = new ArrayList<SearchResult>();
        
        Element root = doc.getDocumentElement();
        NodeList list = root.getElementsByTagName("item");
        
        for (int i = 0; i < list.getLength(); i++)
        {
            Element element = (Element) list.item(i);
            
            TextSearchResult searchResult = new TextSearchResult();
            
            searchResult.title = getContent(element, "title");
            searchResult.link = getContent(element, "link");
            searchResult.snippet = getContent(element, "description");
            
            searchResultList.add(searchResult);
            
        }
        
        return searchResultList;
    }
    
    private List<SearchResult> parseCafeArticleDom(Document doc)
    {
        List<SearchResult> searchResultList = new ArrayList<SearchResult>();
        
        Element root = doc.getDocumentElement();
        NodeList list = root.getElementsByTagName("item");
        
        for (int i = 0; i < list.getLength(); i++)
        {
            Element element = (Element) list.item(i);
            
            TextSearchResult searchResult = new TextSearchResult();
            
            searchResult.title = getContent(element, "title");
            searchResult.link = getContent(element, "link");
            searchResult.snippet = getContent(element, "description");
            
            searchResultList.add(searchResult);
        }
        
        return searchResultList;
        
    }
    
    private List<SearchResult> parseWebKrDom(Document doc)
    {
        List<SearchResult> searchResultList = new ArrayList<SearchResult>();
        
        Element root = doc.getDocumentElement();
        NodeList list = root.getElementsByTagName("item");
        
        for (int i = 0; i < list.getLength(); i++)
        {
            Element element = (Element) list.item(i);
            
            TextSearchResult searchResult = new TextSearchResult();
            
            searchResult.title = getContent(element, "title");
            searchResult.link = getContent(element, "link");
            searchResult.snippet = getContent(element, "description");
            
            searchResultList.add(searchResult);
        }
        
        return searchResultList;
    }
    
    private List<SearchResult> parseNewsDom(Document doc)
    {
        List<SearchResult> searchResultList = new ArrayList<SearchResult>();
        
        Element root = doc.getDocumentElement();
        NodeList list = root.getElementsByTagName("item");
        
        for (int i = 0; i < list.getLength(); i++)
        {
            Element element = (Element) list.item(i);
            TextSearchResult searchResult = new TextSearchResult();
            
            searchResult.title = getContent(element, "title");
            searchResult.link = getContent(element, "originallink");
            searchResult.snippet = getContent(element, "description");
            searchResultList.add(searchResult);
        }
        
        return searchResultList;
    }
    
    private String getContent(Element element, String tagName)
    {
        
        NodeList list = element.getElementsByTagName(tagName);
        Element cElement = (Element) list.item(0);
        
        if (cElement.getFirstChild() != null)
        {
            return cElement.getFirstChild().getNodeValue();
        }
        else
        {
            return "";
        }
    }
    
    public List<SearchResult> response()
    {
        logger.debug(NaverAPI.class.getName() + " result : " + this.searchResultList.size());
         
        return this.searchResultList;
    }
    
    public String getAPIName()
    {
        return this.getClass().getName();
        
    }
    
    public String getSort()
    {
        return this.sort;
    }
    
    public void setSort(String sort)
    {
        this.sort = sort;
    }
    
    @Override
    public int getOutputCount()
    {
        return this.outputCount;
    }
    
    @Override
    public void setOutputCount(int outputCount)
    {
        
        if (outputCount > this.outputCountLimit)
        {
            this.outputCount = this.outputCountDefault;
        }
        else
        {
            this.outputCount = outputCount;
        }
    }
    
    @Override
    public int getPageNo()
    {
        return this.pageNo;
    }
    
    @Override
    public void setPageNo(int pageNo)
    { 
        if (pageNo > this.pageNoLimit)
        {
            this.pageNo = this.pageNoDefault;
        }
        else
        {
            this.pageNo = pageNo;
        }
        
    }
}
