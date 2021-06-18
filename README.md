# Live Backend Production

#### Access Dashboard User Images
    domain.com/profileImages/user.png

#### Access Aed Images
    domain.com/aedImages/aed.png
    
#### Access Mobile User Images
    domain.com/mobileImages/mobileUser.png

### How to configure image paths on VM

add to MVC Resource Handlers

    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	       registry
	      .addResourceHandler("/profileImages/**")
	      .addResourceLocations("file:/home/src/main/resources/static/images/profileImages/")
	      .setCachePeriod(3600)
	      .resourceChain(true)
          .addResolver(new PathResourceResolver());
	       
	       
	       registry
           .addResourceHandler("/aedImages/**")
           .addResourceLocations("file:/home/src/main/resources/static/images/aedImages/")
           .setCachePeriod(3600)
           .resourceChain(true)
           .addResolver(new PathResourceResolver());
           
            registry
           .addResourceHandler("/mobileImages/**")
           .addResourceLocations("file:/home/src/main/resources/static/images/mobileImages/")
           .setCachePeriod(3600)
           .resourceChain(true)
           .addResolver(new PathResourceResolver());
	    }
