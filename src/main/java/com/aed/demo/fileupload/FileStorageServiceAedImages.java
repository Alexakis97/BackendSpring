package com.aed.demo.fileupload;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aed.demo.entity.AED;
import com.aed.demo.repositories.AEDRepository;

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
public class FileStorageServiceAedImages {
	
	@Autowired
	AEDRepository aedRepo;
	
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageServiceAedImages(FileStoragePropertiesAedImages fileStoragePropertiesAED) {
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

            // Copy file to the target location (Replacing existing file with the same name)
            String currentDate = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
            System.out.println(currentDate);
            System.out.println("FILENAME:"+fileName);
            
            int aedId=0;
            try{
            String[] parsed = fileName.split("\\.");
            aedId= Integer.parseInt(parsed[0]);
            }catch(Exception e)
            {
                e.printStackTrace();
                aedId= Integer.parseInt(fileName);
            }

            
			System.out.println(aedId);
			
			Optional<AED> aed = aedRepo.findById(aedId);
			AED aedObj = aed.get();
            
            System.out.println(aedObj);
			
			String photoString =aedObj.getPhoto();
            String oldTimestampString=null;
            
            System.out.println("aed photo:"+photoString);
			
			if(photoString!=null&&!photoString.equals("-"))
			{
			 oldTimestampString = ((photoString.split("-")[0]).split("/")[2]);
			
			Path targetLocation = this.fileStorageLocation.resolve(oldTimestampString+"-"+aedId+".png");    
		    File oldFile = new File(targetLocation.toString());
		    if(oldFile.exists())
		    {
		    	if(oldFile.delete())
		    	{
		    		  Path newtargetLocation = this.fileStorageLocation.resolve(currentDate+"-"+aedId+".png");
		              Files.copy(file.getInputStream(), newtargetLocation, StandardCopyOption.REPLACE_EXISTING);

		    	}
		    }
		    
			else {

	             targetLocation = this.fileStorageLocation.resolve(currentDate+"-"+aedId+".png");
	            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }
        }
			
			aedObj.setPhoto("/aedImages/"+currentDate+"-"+aedId+".png");
			aedRepo.save(aedObj);

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
