package com.community.aem.core;
 
import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
   
import com.community.aem.core.HeroTextBean;
  
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
   
import javax.jcr.Node;
import javax.jcr.Session;
   
  
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
  
  
  
public class HeroTextComponent
extends WCMUsePojo
{
  
     /** The hero text bean. */
    private HeroTextBean heroTextBean = null;
       
    @Override
    public void activate() throws Exception {
           
         heroTextBean = new HeroTextBean();
          
        String heading;  
        String description ; 
         
        //Get the values that the author entered into the AEM dialog
       heading = getProperties().get("jcr:heading", "");
       description = getProperties().get("jcr:description","");
         
        heroTextBean.setHeadingText(heading);
        heroTextBean.setDescription(description); 
      }
       
  public HeroTextBean getHeroTextBean() {
        return this.heroTextBean;
    }
}