package com.aed.demo.fileupload;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aed.demo.entity.User;
import com.aed.demo.repositories.UserRepository;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


@Service
public class FileStorageServiceMobileImages {
    private final Path fileStorageLocation;
    
    @Autowired
	UserRepository userRepo;


    @Autowired
    public FileStorageServiceMobileImages(FileStoragePropertiesMobileImages fileStoragePropertiesAED) {
        this.fileStorageLocation = Paths.get(fileStoragePropertiesAED.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
   

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            
            String currentDate = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
            System.out.println(currentDate);
            



            int userId=0;
            try{
            String[] parsed = fileName.split("\\.");
            userId= Integer.parseInt(parsed[0]);
            }catch(Exception e)
            {
                e.printStackTrace();
                userId= Integer.parseInt(fileName);
            }

			
			
			
			Optional<User> usr = userRepo.findById(userId);
			User userObj = usr.get();
			
			String photoString =userObj.getPhoto();
			String oldTimestampString=null;
			
			if(photoString!=null&&!photoString.equals("-"))
			{
			
			oldTimestampString = ((photoString.split("-")[0]).split("/")[2]);
			Path targetLocation = this.fileStorageLocation.resolve(oldTimestampString+"-"+userId+".png");    
		    File oldFile = new File(targetLocation.toString());
		    if(oldFile.exists())
		    {
		    	if(oldFile.delete())
		    	{
		    		  Path newtargetLocation = this.fileStorageLocation.resolve(currentDate+"-"+userId+".png");
		              Files.copy(file.getInputStream(), newtargetLocation, StandardCopyOption.REPLACE_EXISTING);

		    	}
		    
		    
			}else {
	            targetLocation = this.fileStorageLocation.resolve(currentDate+"-"+userId+".png");
	            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }
        }
			
			userObj.setPhoto("/mobileImages/"+currentDate+"-"+userId+".png");
			userRepo.save(userObj);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}
