/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package tech.innisfree.assignments.servlet.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.codehaus.jackson.map.ObjectMapper;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import tech.innisfree.assignments.servlet.core.models.Beverage;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

import static com.day.cq.personalization.offerlibrary.usebean.OffercardPropertiesProvider.LOGGER;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Beverage Servlet",
                "sling.servlet.methods=GET",
                "sling.servlet.methods=PUT",
                "sling.servlet.methods=POST",
                "sling.servlet.resourceTypes=" + "ServletAssignment/servlet/beverage"
        })
public class BeverageServlet extends SlingAllMethodsServlet {
    private static final Logger log = LoggerFactory.getLogger(BeverageServlet.class);
    private static final long serialVersionUid = 1L;

        @Override
        protected void doPost(final SlingHttpServletRequest req,
                              final SlingHttpServletResponse resp) throws ServletException, IOException {

            LOGGER.info("Beverage servlet invoked. Method = POST");
            String beverageName = "";
            int servletResponseCode = 0;
            String servletResponseBody = "asd";
            //LOGGER.info("Beverage Name = " + beverageName);
            try {
                RequestParameter beverageNameParameter = req.getRequestParameter("beverageName");
                if (beverageNameParameter == null || beverageNameParameter.toString().isEmpty()) {
                    servletResponseBody = "Incorrect input message. You must supply a valid HTTP form with a parameter called beverageName";
                    LOGGER.error(servletResponseBody);
                    servletResponseCode = 400;
                }else {
                    beverageName = beverageNameParameter.toString();

                    ResourceResolver resourceResolver = req.getResourceResolver();
                    Resource resources = resourceResolver.getResource("/content/ServletAssignment/beverages");

                    LOGGER.info("Resource is at path {}", resources.getPath());

                    Node node = resources.adaptTo(Node.class);
                    Node newNode = null;
                    try {
                        // If the relevant beverage node already exists, the servlet must:
                        if (node.hasNode(beverageName)) {
                            servletResponseBody = "Uniqueness constraint violation. The beverage already exists in the JCR";
                            LOGGER.error(servletResponseBody);
                            servletResponseCode = 409;
                        } else {
                            newNode = node.addNode(beverageName, "nt:unstructured");
                            newNode.setProperty("name", beverageName);
                            servletResponseCode = 200;
                            servletResponseBody = "Wow you have done it!!! Node added";
                            //13 If the requested beverage name is “Coffee”, the servlet should
                            if (beverageName.equals("Coffee")) {
                                servletResponseCode = 418;
                                servletResponseBody = "Coffee is for the weak and timid - Prepare to be annihilated";
                                LOGGER.warn(servletResponseBody);
                            }
                        }
                        resourceResolver.commit();
                    } catch (RepositoryException e) {
                        //servletResponseCode = 500 || HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                        servletResponseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                        servletResponseBody = "Ohhh no you have reached 500 again";
                    }finally {
                        if(resourceResolver.isLive()) {
                            servletResponseCode = 200;
                            servletResponseBody = "Wow you have done it!!! Node added";
                            resourceResolver.close();
                        }
                    }
                }
            } catch (RuntimeException e) {
                //servletResponseCode = 500 || HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                servletResponseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                servletResponseBody = "Ohhh no you have reached 500 again";
            }
            resp.getWriter().println(servletResponseBody);
            resp.setStatus(servletResponseCode);
        }
//
//    @Override
//    protected void doGet(final SlingHttpServletRequest req,
//                         final SlingHttpServletResponse resp) throws ServletException, IOException {
//
//        log.info("Beverage servlet invoked. Method = GET");
//        int servletResponseCode = 0;
//        String servletResponseBody = "";
//
//        String requestedBeverageName = "";
//        String manufacturerName = "";
//        RequestParameter requestedParam = null;
//        Beverage beverage = null;
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            requestedParam = req.getRequestParameter("beverageName");
//
//            if (requestedParam == null || requestedParam.toString().isEmpty()) {
//                servletResponseCode = HttpServletResponse.SC_BAD_REQUEST;
//                log.error("Incorrect input message. You must supply a valid HTTP form with a parameter called beverageName");
//
//            } else {
//                ResourceResolver resourceResolver = null;
//                try {
//                    requestedBeverageName =  req.getRequestParameter("beverageName").toString();
//                    manufacturerName = req.getRequestParameter("manufacturerName").toString();
//
//                    resourceResolver = req.getResourceResolver();
//                    Resource resources = resourceResolver.getResource("/content/ServletAssignment/beverage");
//
//                    log.info("Resource is at path {}", resources.getPath());
//
//                    Node node = resources.adaptTo(Node.class);
//
//
//                    if (node.hasNode(requestedBeverageName)) {
//                        beverage.setName(node.getName());
//                        beverage.setManufactureName(manufacturerName);
//
//                    }else{
//                        servletResponseCode = 404;
//                        objectMapper.writeValue(new File("target/beverage.json"), servletResponseCode);
//                    }
//                }catch (RepositoryException e) {
//                        //servletResponseCode = 500 || HttpServletResponse.SC_INTERNAL_SERVER_ERROR
//                        servletResponseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
//                    }finally {
//                        if(resourceResolver.isLive()) {
//                            resourceResolver.close();
//                        }
//                    }
//
//        }
//        } catch (RuntimeException e) {
//            servletResponseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
//            objectMapper.writeValue(new File("target/beverage.json"), servletResponseCode);
//        }
//        resp.setContentType("application/json");
//        resp.getWriter().println(objectMapper.writeValueAsString(beverage));
//        resp.setStatus(servletResponseCode);
//    }
//
//    @Override
//    protected void doPut(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
//
//        log.info("Beverage servlet invoked. Method = PUT");
//        int servletResponseCode = 0;
//        String servletResponseBody = "";
//
//
//        String putBeverageName = "";
//        String putManufacturerName = "";
//        RequestParameter requestedParam = null;
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            putBeverageName = req.getRequestParameter("beverageName").toString();
//            putManufacturerName = req.getRequestParameter("manufactureName").toString();
//
//            if ((putBeverageName == null || putBeverageName.isEmpty())||(putManufacturerName == null || putManufacturerName.isEmpty())) {
//                servletResponseCode = HttpServletResponse.SC_BAD_REQUEST;
//                log.error("Incorrect input message. You must supply a valid HTTP form with a parameter called beverageName and manufacture name");
//            }else{
//
//                ResourceResolver resourceResolver = null;
//                try {
//
//                    resourceResolver = req.getResourceResolver();
//                    Resource resources = resourceResolver.getResource("/content/ServletAssignment/beverage");
//
//                    log.info("Resource is at path {}", resources.getPath());
//
//                    Node node = resources.adaptTo(Node.class);
//
//                    if (node.hasNode(putBeverageName)) {
//
//                        node.getNode(putBeverageName).setProperty("manufacturerName", putManufacturerName);
//                        resourceResolver.commit();
//                        servletResponseCode = 200;
//
//                    }else{
//                        servletResponseCode = 404;
//                    }
//                }catch (RepositoryException e) {
//                    servletResponseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
//                }finally {
//                    if(resourceResolver.isLive()) {
//                        resourceResolver.close();
//                    }
//                }
//            }
//            }catch (RuntimeException e) {
//            servletResponseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
//        }
//         resp.setContentType("application/json");
//        resp.getWriter().println(objectMapper.writeValueAsString(servletResponseCode));
//        resp.setStatus(servletResponseCode);
//    }


}



